package com.jocmp.mercury.dsl

import org.jsoup.nodes.Comment
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.select.Elements

class Selection internal constructor(
    val doc: Doc,
    val elements: Elements,
) {
    val length: Int get() = elements.size

    fun first(): Selection = if (elements.isEmpty()) this else doc.wrap(elements.first()!!)

    fun get(index: Int): Element? = elements.getOrNull(index)

    // Cheerio's .text() concatenates descendant text nodes with no separator (unlike
    // Jsoup's text() which inserts spaces between block elements). Faithful port matters
    // for linkDensity/text-length comparisons.
    fun text(): String =
        buildString {
            elements.forEach { el ->
                collectText(el, this)
            }
        }

    private fun collectText(
        el: Element,
        out: StringBuilder,
    ) {
        el.childNodes().forEach { node ->
            when (node) {
                is org.jsoup.nodes.TextNode -> out.append(node.wholeText)
                is Element -> collectText(node, out)
                else -> Unit
            }
        }
    }

    fun html(): String = elements.html()

    fun outerHtml(): String = elements.joinToString("") { it.outerHtml() }

    fun attr(name: String): String? = elements.firstOrNull()?.attr(name)?.ifEmpty { null }

    fun attr(
        name: String,
        value: String,
    ): Selection = apply { elements.attr(name, value) }

    fun removeAttr(name: String): Selection = apply { elements.removeAttr(name) }

    fun hasAttr(name: String): Boolean = elements.firstOrNull()?.hasAttr(name) == true

    fun children(): Selection = doc.wrap(elements.flatMap { it.children() }.let(::Elements))

    fun find(selector: String): Selection = doc.wrap(elements.select(selector))

    fun parent(): Selection = doc.wrap(elements.mapNotNull { it.parent() }.let(::Elements))

    fun remove(): Selection = apply { elements.remove() }

    fun each(block: (index: Int, element: Element) -> Unit): Selection {
        elements.forEachIndexed { index, el -> block(index, el) }
        return this
    }

    fun contents(): List<Node> = elements.flatMap { it.childNodes() }

    fun removeComments(): Selection =
        apply {
            elements.forEach { el ->
                el.childNodes().filterIsInstance<Comment>().forEach { it.remove() }
                el.select("*").forEach { child ->
                    child.childNodes().filterIsInstance<Comment>().forEach { it.remove() }
                }
            }
        }
}
