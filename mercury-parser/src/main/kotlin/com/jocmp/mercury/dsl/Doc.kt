package com.jocmp.mercury.dsl

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class Doc internal constructor(val document: Document) {
    operator fun invoke(selector: String): Selection = Selection(this, document.select(selector))

    fun root(): Selection = Selection(this, document.children())

    fun load(
        html: String,
        baseUri: String? = null,
    ): Doc = Doc(Jsoup.parseBodyFragment(html, baseUri ?: ""))

    fun html(): String = document.outerHtml()

    internal fun wrap(elements: org.jsoup.select.Elements): Selection = Selection(this, elements)

    internal fun wrap(element: Element): Selection = Selection(this, org.jsoup.select.Elements(element))

    companion object {
        fun load(
            html: String,
            baseUri: String? = null,
        ): Doc = Doc(Jsoup.parse(html, baseUri ?: ""))
    }
}
