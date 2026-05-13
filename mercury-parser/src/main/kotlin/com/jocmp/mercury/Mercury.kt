package com.jocmp.mercury

object Mercury {
    suspend fun parse(
        @Suppress("UNUSED_PARAMETER") url: String,
        @Suppress("UNUSED_PARAMETER") options: ParseOptions = ParseOptions(),
    ): ParseResult = throw NotImplementedError("Mercury.parse is not yet implemented")
}
