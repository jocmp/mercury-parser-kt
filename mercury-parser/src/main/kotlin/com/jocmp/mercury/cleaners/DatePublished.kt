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

    if (stringHasTimezone) {
        parseLenient(dateString)?.let { return it }
    }

    if (timezone != null && !stringHasTimezone) {
        val zone =
            try {
                ZoneId.of(timezone)
            } catch (_: Throwable) {
                null
            }
        if (zone != null) {
            if (format != null) {
                val cleanedFormat = stripTimezoneFromFormat(format)
                runCatching {
                    val javaPattern = dayjsToJavaPattern(cleanedFormat)
                    val ldt = LocalDateTime.parse(cleanedDateString, DateTimeFormatter.ofPattern(javaPattern, Locale.ENGLISH))
                    return ldt.atZone(zone)
                }
            }
            parseLenient(cleanedDateString)?.let {
                return it.withZoneSameLocal(zone)
            }
            return null
        }
    }

    if (format != null) {
        val cleanedFormat = stripTimezoneFromFormat(format)
        runCatching {
            val javaPattern = dayjsToJavaPattern(cleanedFormat)
            val ldt = LocalDateTime.parse(cleanedDateString, DateTimeFormatter.ofPattern(javaPattern, Locale.ENGLISH))
            return ldt.atZone(ZoneOffset.UTC)
        }
    }
    return parseLenient(cleanedDateString)
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

private fun parseLenient(input: String): ZonedDateTime? {
    val s = input.trim()
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

    val localCandidates =
        listOf(
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd HH:mm",
            "MMM d yyyy h:mm a",
            "MMM d yyyy H:mm",
            "MMMM d yyyy h:mm a",
            "MMMM d yyyy H:mm",
            "d MMM yyyy H:mm",
            "d MMMM yyyy H:mm",
            "M/d/yyyy H:mm",
            "yyyy M d H:mm",
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
            "M/d/yyyy",
            "MMM d yyyy",
            "MMMM d yyyy",
            "d MMM yyyy",
            "d MMMM yyyy",
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
            "HH" to "HH",
            "H" to "H",
            "hh" to "hh",
            "h" to "h",
            "mm" to "mm",
            "ss" to "ss",
            "A" to "a",
            "a" to "a",
            "ZZ" to "xx",
            "Z" to "xxx",
        )
    var result = format
    val placeholderMap = mutableMapOf<String, String>()
    tokens.forEachIndexed { i, (from, to) ->
        val placeholder = "$i"
        result = result.replace(from, placeholder)
        placeholderMap[placeholder] = to
    }
    placeholderMap.forEach { (placeholder, replacement) ->
        result = result.replace(placeholder, replacement)
    }
    return result
}
