package com.jocmp.mercury.cleaners

import java.net.URI

fun cleanLeadImageUrl(leadImageUrl: String): String? =
    try {
        URI.create(leadImageUrl.trim()).toString()
    } catch (_: Throwable) {
        null
    }
