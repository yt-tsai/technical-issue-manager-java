package com.example.technicalissuemanager.util;

/**
 * Escapes text before placing it in HTML output.
 */
public final class HtmlEscaper {

    private HtmlEscaper() {
    }

    public static String escape(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("\047", "&#39;");
    }
}
