package com.example.technicalissuemanager.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HtmlEscaperTest {

    @Test
    void escapesHtmlSpecialCharacters() {
        String input = "Tom & \"Jerry\" <tag> " + (char) 39 + "value" + (char) 39;

        assertEquals(
                "Tom &amp; &quot;Jerry&quot; &lt;tag&gt; &#39;value&#39;",
                HtmlEscaper.escape(input));
    }

    @Test
    void returnsEmptyStringForNull() {
        assertEquals("", HtmlEscaper.escape(null));
    }

    @Test
    void keepsJapaneseAndPlainTextUnchanged() {
        assertEquals("技術課題管理システム ABC 123", HtmlEscaper.escape("技術課題管理システム ABC 123"));
    }
}
