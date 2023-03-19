package com.webcrawler.webcrawlerapp.io;

public enum Tag {
    HTML("html", true),
    HEADER("header", false),
    BODY("body", true),
    FOOTER("footer", false),
    NAV("nav", false),
    DIV("div", true),
    TABLE("table", true),
    HYPER_LINK("a", true);

    private static final String startBracket = "<";
    private static final String endBracket = ">";

    private final String tagName;
    final boolean isAttributable;

    Tag(String tagName, boolean isAttributable) {
        this.tagName = tagName;
        this.isAttributable = isAttributable;
    }

    static String getStartBracket() {
        return startBracket;
    }

    static String getEndBracket() {
        return endBracket;
    }

    private boolean isAttributable() {
        return this.isAttributable;
    }

    String getStartTag() {
        // build tag.
        // if no possible attributes in tag, end with endBracket. i.e. <html>
        // else if attributes possible in tag, end without endBracket. ie <a
        StringBuilder buildTag = new StringBuilder();
        buildTag.append(startBracket);
        buildTag.append(tagName);
        if (!this.isAttributable) {
            buildTag.append(endBracket);
        }
        return buildTag.toString();
    }

    String getEndTag() {
        //build closingTag. ie. </html>
        String buildTag =
                startBracket +
                        "/" +
                        this.tagName +
                        endBracket;

        return buildTag;
    }
}