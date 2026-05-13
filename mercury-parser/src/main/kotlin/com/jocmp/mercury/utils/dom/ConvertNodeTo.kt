package com.jocmp.mercury.utils.dom

import com.jocmp.mercury.dsl.Doc
import com.jocmp.mercury.dsl.Selection

fun convertNodeTo(
    node: Selection,
    doc: Doc,
    tag: String = "p",
): Doc {
    val raw = node.elements.firstOrNull() ?: return doc
    val attrs = getAttrs(raw)

    val attribString = attrs.entries.joinToString(" ") { "${it.key}=${it.value}" }

    // On the node side, use html() to preserve nested structure.
    val html = if (raw.tagName().lowercase() == "noscript") node.html() else node.html()
    raw.replaceWith(org.jsoup.parser.Parser.parseBodyFragment("<$tag $attribString>$html</$tag>", "").body().child(0))
    return doc
}
