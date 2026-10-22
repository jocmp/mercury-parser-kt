package com.jocmp.mercury.utils.dom

import com.jocmp.mercury.dsl.Doc
import kotlin.test.Test
import kotlin.test.assertEquals

class MakeLinksAbsoluteTest {
    @Test
    fun `resolves links against the root url`() {
        val doc = Doc.load("""<html><body><div><a href="/foo/bar">Link</a></div></body></html>""")

        makeLinksAbsolute(doc("div"), doc, "https://example.com/article")

        assertEquals("https://example.com/foo/bar", doc("a").attr("href"))
    }

    @Test
    fun `resolves links against an absolute base href`() {
        val doc =
            Doc.load(
                """<html><head><base href="https://cdn.example.com/assets/"></head><body><div><img src="pic.jpg"></div></body></html>""",
            )

        makeLinksAbsolute(doc("div"), doc, "https://example.com/article")

        assertEquals("https://cdn.example.com/assets/pic.jpg", doc("img").attr("src"))
    }

    @Test
    fun `resolves a relative base href against the root url`() {
        val doc =
            Doc.load(
                """<html><head><base href="/media/"></head><body><div><img src="pic.jpg"></div></body></html>""",
            )

        makeLinksAbsolute(doc("div"), doc, "https://example.com/article")

        assertEquals("https://example.com/media/pic.jpg", doc("img").attr("src"))
    }
}
