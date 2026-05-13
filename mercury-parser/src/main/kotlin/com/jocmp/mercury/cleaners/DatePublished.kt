package com.jocmp.mercury.cleaners

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoUnit
import java.util.Locale

private val TIMEZONE_ABBR_RE: Regex = Regex("""\b(EST|EDT|CST|CDT|MST|MDT|PST|PDT|ET|CT|MT|PT|GMT|UTC)\b""", RegexOption.IGNORE_CASE)

// Check if string contains timezone offset info (e.g., +0000, GMT+0000, Z)
private val HAS_TIMEZONE_RE: Regex = Regex("""([+-]\d{2}:?\d{2}|Z|\bGMT[+-]\d+|\bUTC\b)""", RegexOption.IGNORE_CASE)

private fun hasTimezoneInfo(str: String): Boolean = HAS_TIMEZONE_RE.containsMatchIn(str)

private fun stripTimezoneAbbr(str: String): String =
    str
        .replace(TIMEZONE_ABBR_RE, "")
        .replace(Regex("""\s+"""), " ")
        .trim()

// Remove timezone tokens (zz, z, ZZ, Z) from format string
private fun stripTimezoneFromFormat(format: String): String =
    format
        .replace(Regex("""\s*z+""", RegexOption.IGNORE_CASE), "")
        .replace(Regex("""\s+"""), " ")
        .trim()

fun cleanDateString(dateString: String): String =
    (SPLIT_DATE_STRING.findAll(dateString).map { it.value }.toList())
        .joinToString(" ")
        .replace(TIME_MERIDIAN_DOTS_RE, "m")
        .replace(TIME_MERIDIAN_SPACE_RE, "$1 $2 $3")
        .replace(CLEAN_DATE_STRING_RE, "$1")
        .trim()

fun createDate(
    dateString: String,
    timezone: String? = null,
    format: String? = null,
): ZonedDateTime? {
    if (TIME_WITH_OFFSET_RE.containsMatchIn(dateString)) {
        return parseLenient(dateString)
    }

    val agoMatch = TIME_AGO_STRING.find(dateString)
    if (agoMatch != null) {
        val amount = agoMatch.groupValues[1].toLong()
        val unit = agoMatch.groupValues[2].lowercase()
        return ZonedDateTime.now(ZoneOffset.UTC).minus(amount, dayjsUnitToChrono(unit))
    }

    if (TIME_NOW_STRING.containsMatchIn(dateString)) {
        return ZonedDateTime.now(ZoneOffset.UTC)
    }

    val stringHasTimezone = hasTimezoneInfo(dateString)
    val cleanedDateString = stripTimezoneAbbr(dateString)

    // Always try zone-bearing patterns on the original first — they may match
    // RFC_1123 (with bare "GMT") or ISO variants even when HAS_TIMEZONE_RE
    // missed the trailing abbreviation.
    parseZoned(dateString)?.let { return it }

    if (timezone != null && !stringHasTimezone) {
        val zone = resolveZone(timezone)
        if (zone != null) {
            if (format != null) {
                parseWithFormat(cleanedDateString, format)?.let { return it.atZone(zone) }
            }
            parseLenient(cleanedDateString)?.let {
                return it.withZoneSameLocal(zone)
            }
            return null
        }
    }

    if (format != null) {
        parseWithFormat(cleanedDateString, format)?.let { return it.atZone(ZoneOffset.UTC) }
    }
    return parseLenient(cleanedDateString)
}

// Resolve a timezone string, falling back to SHORT_IDS for aliases like
// "EST", "PST", "GMT", "UTC" that ZoneId.of rejects by default.
private fun resolveZone(timezone: String): ZoneId? {
    runCatching { return ZoneId.of(timezone, ZoneId.SHORT_IDS) }
    runCatching { return ZoneId.of(timezone) }
    return null
}

// Try LocalDateTime first, falling back to LocalDate-at-start-of-day for
// date-only formats like 'YYYY/M/D' or 'YYYY年M月D日'. Uses ParsePosition so
// the format only needs to match the leading portion of the input — matches
// dayjs's customParseFormat behaviour with trailing noise from selectors.
private fun parseWithFormat(
    input: String,
    format: String,
): LocalDateTime? {
    val cleaned = stripTimezoneFromFormat(format)
    val javaPattern = dayjsToJavaPattern(cleaned)
    val fmt = DateTimeFormatter.ofPattern(javaPattern, Locale.ENGLISH)
    // Scan positions so leading text (e.g. "Written by ... on ") doesn't
    // prevent a match — mirrors dayjs's regex-anywhere semantics.
    for (start in 0..input.length) {
        val pos = java.text.ParsePosition(start)
        val accessor = runCatching { fmt.parse(input, pos) }.getOrNull() ?: continue
        // Prefer a full LocalDateTime (date + time). Only fall back to
        // start-of-day when the format didn't include time at all.
        runCatching { return LocalDateTime.from(accessor) }
    }
    for (start in 0..input.length) {
        val pos = java.text.ParsePosition(start)
        val accessor = runCatching { fmt.parse(input, pos) }.getOrNull() ?: continue
        runCatching { return java.time.LocalDate.from(accessor).atStartOfDay() }
    }
    return null
}

// Take a date published string, and hopefully return a date out of
// it. Return none if we fail.
fun cleanDatePublished(
    dateString: String,
    timezone: String? = null,
    format: String? = null,
): String? {
    // If string is in milliseconds or seconds, convert to int and return
    if (MS_DATE_STRING.containsMatchIn(dateString)) {
        return Instant.ofEpochMilli(dateString.toLong()).toString()
    }
    if (SEC_DATE_STRING.containsMatchIn(dateString)) {
        return Instant.ofEpochSecond(dateString.toLong()).toString()
    }

    var date = createDate(dateString, timezone, format)
    if (date == null) {
        date = createDate(cleanDateString(dateString), timezone, format)
    }
    return date?.withZoneSameInstant(ZoneOffset.UTC)?.toInstant()?.toString()
}

private fun dayjsUnitToChrono(unit: String): ChronoUnit =
    when (unit.removeSuffix("s")) {
        "second" -> ChronoUnit.SECONDS
        "minute" -> ChronoUnit.MINUTES
        "hour" -> ChronoUnit.HOURS
        "day" -> ChronoUnit.DAYS
        "week" -> ChronoUnit.WEEKS
        "month" -> ChronoUnit.MONTHS
        "year" -> ChronoUnit.YEARS
        else -> ChronoUnit.SECONDS
    }

// Pad 3-digit offsets like "-500" / "+800" to "-0500" / "+0800". Mercury fixtures
// occasionally have these (e.g. washingtonpost.com) where moment.js was permissive.
private val MALFORMED_OFFSET_RE = Regex("""([+-])(\d)(\d{2})$""")

// Try patterns that include genuine timezone information. Returns null when
// the input has no zone — leaves it to the caller to apply a fallback zone.
private fun parseZoned(input: String): ZonedDateTime? {
    val s =
        MALFORMED_OFFSET_RE.replace(input.trim()) { m ->
            "${m.groupValues[1]}0${m.groupValues[2]}${m.groupValues[3]}"
        }
    val zonedCandidates =
        listOf<DateTimeFormatter>(
            DateTimeFormatter.ISO_ZONED_DATE_TIME,
            DateTimeFormatter.ISO_OFFSET_DATE_TIME,
            DateTimeFormatter.RFC_1123_DATE_TIME,
        )
    for (fmt in zonedCandidates) {
        runCatching { return ZonedDateTime.parse(s, fmt) }
    }
    runCatching { return Instant.parse(s).atZone(ZoneOffset.UTC) }

    // Common variants that ISO_OFFSET_DATE_TIME doesn't accept:
    // 4-digit offsets without colon ("+0000"), space-separated date/time,
    // and missing seconds. `X` accepts "Z"; `Z` accepts "±HHMM".
    val zonedExtraPatterns =
        listOf(
            "yyyy-MM-dd'T'HH:mm:ssZ",
            "yyyy-MM-dd'T'HH:mmZ",
            "yyyy-MM-dd HH:mm:ssZ",
            "yyyy-MM-dd HH:mm:ss Z",
            "yyyy-MM-dd HH:mm:ss XXX",
            "yyyy-MM-dd HH:mm:ssX",
            "yyyy-MM-dd HH:mm:ss X",
            "yyyy-MM-dd HH:mmZ",
            "yyyy-MM-dd'T'HH:mm:ss XXX",
        )
    for (pattern in zonedExtraPatterns) {
        runCatching {
            val fmt =
                DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern(pattern)
                    .toFormatter(Locale.ENGLISH)
            return ZonedDateTime.parse(s, fmt)
        }
    }
    return null
}

private fun parseLenient(input: String): ZonedDateTime? {
    val s = input.trim()
    parseZoned(s)?.let { return it }

    val localCandidates =
        listOf(
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd HH:mm",
            "yyyy/MM/dd HH:mm:ss",
            "yyyy/MM/dd HH:mm",
            "yyyy/M/d HH:mm",
            "MMM d yyyy h:mm a",
            "MMM d yyyy H:mm",
            "MMMM d yyyy h:mm a",
            "MMMM d yyyy H:mm",
            "d MMM yyyy H:mm",
            "d MMM yyyy h:mm a",
            "d MMMM yyyy H:mm",
            "d MMMM yyyy h:mm a",
            "H:mm d MMM yyyy",
            "H:mm d MMMM yyyy",
            "EEE, d MMM yyyy H:mm:ss",
            "M/d/yyyy H:mm",
            "M/d/yyyy h:mm a",
            "yyyy M d H:mm",
            "yyyy年M月d日 H:mm",
            "yyyy年M月d日 HH:mm",
            "yyyy年M月d日H時m分",
        )
    for (pattern in localCandidates) {
        runCatching {
            val fmt =
                DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern(pattern)
                    .toFormatter(Locale.ENGLISH)
            return LocalDateTime.parse(s, fmt).atZone(ZoneOffset.UTC)
        }
    }
    val dateOnly =
        listOf(
            "yyyy-MM-dd",
            "yyyy/M/d",
            "yyyy/MM/dd",
            "yyyy.M.d",
            "yyyy.MM.dd",
            "M/d/yyyy",
            "M-d-yyyy",
            "MM-dd-yyyy",
            "MMM d yyyy",
            "MMMM d yyyy",
            "d MMM yyyy",
            "d MMMM yyyy",
            "yyyy年M月d日",
            "yyyy年MM月dd日",
            "yyyy M d",
        )
    for (pattern in dateOnly) {
        runCatching {
            val fmt =
                DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern(pattern)
                    .toFormatter(Locale.ENGLISH)
            return java.time.LocalDate.parse(s, fmt).atStartOfDay(ZoneOffset.UTC)
        }
    }
    return null
}

// Convert a dayjs/moment format string (YYYY-MM-DD HH:mm:ss) into a
// java.time pattern (yyyy-MM-dd HH:mm:ss). Only the tokens that appear
// in upstream extractor configs are translated.
internal fun dayjsToJavaPattern(format: String): String {
    // dayjs interprets `hh`/`h` as 24-hour when no AM/PM token is present in
    // the format. Java's `hh` is strictly clock-hour-of-am-pm and refuses to
    // resolve without `a`. Promote to 24-hour to match dayjs's looser
    // behaviour (Mercury upstream relies on this for "... at hh:mm" formats).
    val converted = convertTokens(format)
    // After conversion, an `a` outside single-quoted literals means a real
    // AM/PM token from dayjs (which already became Java's `a`). If absent,
    // promote `hh`/`h` to 24-hour so the clock-hour-of-am-pm field doesn't
    // get stranded without a marker.
    return if (hasUnquotedAmPm(converted)) converted else converted.replace("hh", "HH").replace("h", "H")
}

private fun hasUnquotedAmPm(pattern: String): Boolean {
    var inQuote = false
    for (c in pattern) {
        if (c == '\'') {
            inQuote = !inQuote
        } else if (!inQuote && c == 'a') {
            return true
        }
    }
    return false
}

private fun convertTokens(format: String): String {
    val tokens =
        listOf(
            "YYYY" to "yyyy",
            "YY" to "yy",
            "MMMM" to "MMMM",
            "MMM" to "MMM",
            "MM" to "MM",
            "DDDD" to "DDD",
            "DD" to "dd",
            "Do" to "d",
            "D" to "d",
            "dddd" to "EEEE",
            "ddd" to "EEE",
            "M" to "M",
            "HH" to "HH",
            "H" to "H",
            "hh" to "hh",
            "h" to "h",
            "mm" to "mm",
            "m" to "m",
            "ss" to "ss",
            "s" to "s",
            "A" to "a",
            "a" to "a",
            "ZZ" to "xx",
            "Z" to "xxx",
        )

    fun isAsciiLetter(c: Char) = c in 'a'..'z' || c in 'A'..'Z'

    fun quote(s: String) = "'" + s.replace("'", "''") + "'"

    val out = StringBuilder()
    var i = 0
    while (i < format.length) {
        val c = format[i]
        if (isAsciiLetter(c)) {
            // Take the full ASCII-letter run, then try to match it against
            // the token map as a whole. If no exact match, the whole run is
            // a literal (e.g. "at" in "D MMMM YYYY at hh:mm").
            var j = i
            while (j < format.length && isAsciiLetter(format[j])) j++
            val run = format.substring(i, j)
            val matched = tokens.firstOrNull { (from, _) -> from == run }
            if (matched != null) out.append(matched.second) else out.append(quote(run))
            i = j
        } else if (c.isLetter()) {
            // Non-ASCII letter (CJK, etc.) — Java treats unreserved letters
            // as literals already, but quoting is safer.
            out.append(quote(c.toString()))
            i++
        } else {
            out.append(c)
            i++
        }
    }
    return out.toString()
}
