package com.webcrawler.webcrawlerapp.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Element {
    private static String HTML;

    public static void setWebDocument(WebDocument document) {
        HTML = document.getHTML();
    }

    private static boolean isWebDocumentPresent() {
        return HTML != null;
    }

    public static List<String> getElementList(Tag tag) {
        return getElementList(tag, HTML);
    }

    public static List<String> getElementListFromElementHTML(Tag getTag, Tag fromTag) {
        return getElementList(getTag, getElementHTML(fromTag));
    }

    public static boolean containElement(Tag tag, String truncatedHTML) {
        return truncatedHTML.contains(tag.getStartTag());
    }

    public static List<String> getElementContent(Tag tag, String truncatedHTML) {

        int stringIndex = 0;
        List<String> collectHTMLList = new ArrayList<>();
        while (true) {

            // in case of potential attributes in tag (ie. <a href="/example/"> or <div id="example">) find content after first >
            // if no possible attributes in tag, find content after full tag (ie. <html>).
            String startTag = (tag.isAttributable) ? tag.getEndBracket() : tag.getStartTag();
            int startIndex = truncatedHTML.indexOf(startTag, stringIndex) + startTag.length();

            if (startIndex < 0)
                break;

            String endTag = tag.getEndTag();
            int endIndex = truncatedHTML.indexOf(endTag, startIndex);

            String foundContent;
            if (endIndex < 0) {
                foundContent = truncatedHTML.substring(startIndex);
            } else {
                foundContent = truncatedHTML.substring(startIndex, endIndex);
            }
            collectHTMLList.add(foundContent);
            stringIndex = (endIndex > 0) ? endIndex : (startIndex + 1);
        }
        return collectHTMLList;
    }

    private static List<String> getElementList(Tag tag, String truncatedHTML) {
        int stringIndex = 0;
        List<String> collectHTMLList = new ArrayList<>();
        while (true) {
            int startIndex = truncatedHTML.indexOf(tag.getStartTag(), stringIndex);
            if (startIndex < 0)
                break;

            int endIndex = truncatedHTML.indexOf(tag.getEndTag(), startIndex);
            int correctedEndIndex = endIndex + tag.getEndTag().length();

            String foundHTML;
            if (endIndex < 0) {
                foundHTML = truncatedHTML.substring(startIndex);
            } else {
                foundHTML = truncatedHTML.substring(startIndex, correctedEndIndex);
            }

            collectHTMLList.add(foundHTML);
            stringIndex = (endIndex > 0) ? correctedEndIndex : (startIndex + 1);
        }
        return collectHTMLList;
    }

    private static String getElementHTML(Tag tag) {
        int stringIndex = 0;
        StringBuilder HTMLCollection = new StringBuilder();
        while (true) {

            int startIndex = HTML.indexOf(tag.getStartTag(), stringIndex);

            if (startIndex < 0)
                break;
            int endIndex = HTML.indexOf(tag.getEndTag(), startIndex);
            int correctedEndIndex = endIndex + tag.getEndTag().length();

            String foundHTML;
            if (endIndex < 0) {
                foundHTML = HTML.substring(startIndex);
            } else {
                foundHTML = HTML.substring(startIndex, correctedEndIndex);
            }

            HTMLCollection.append(foundHTML);
            stringIndex = (endIndex > 0) ? correctedEndIndex : (startIndex + 1);
        }

        return HTMLCollection.toString();
    }

    public static String reformatUrl(String urlDomein, String subUrl) {
        String reformatUrl = subUrl;
        if(subUrl.charAt(0) == '/')
            reformatUrl = urlDomein + subUrl;
        return reformatUrl;
    }

    public static String getContentAndSubtractAllElements(Tag tag) {
        List<String> elementList = getElementList(tag);
        String trunHTML = truncateListToString(elementList);
        String start = Tag.startBracket;
        String end = Tag.endBracket;
        trunHTML = trunHTML.replaceAll(start+".*?"+end, "\t");
        return trunHTML;
    }

    private static String truncateListToString(List<String> htmlList) {
        StringBuilder truncateHTML = new StringBuilder();
        for (String html : htmlList) {
            truncateHTML.append(html).append("\t");
        }
        return truncateHTML.toString();
    }

    /* Below are Attribute methods */

    public static boolean containAttribute(Attribute attribute, String truncatedHTML) {
        return truncatedHTML.contains(attribute.getStartAttribute());
    }

    public static String getAttributeContent(Attribute attribute, String truncatedHTML) {
        if (truncatedHTML.contains("\"") || truncatedHTML.contains("'"))
            truncatedHTML = truncatedHTML.replace(" ", "");

        String startAttribute = attribute.getStartAttribute();
        int startIndex = truncatedHTML.indexOf(startAttribute) + startAttribute.length();
        if (startIndex < 0)
            return null;

        String endAttribute = attribute.getEndAttribute();
        int endIndex = truncatedHTML.indexOf(endAttribute, startIndex);
        int endTagIndex = truncatedHTML.indexOf(Tag.getEndBracket(), startIndex);


        if (endTagIndex < endIndex || (endTagIndex >= 0) && (endIndex < 0))
            endIndex = endTagIndex;

        // [href=]www.example.nl[>]
        // [href=]"www.example.nl"[>]
        // [href=]"www.example.nl"[ ]xxx>
        // [href=]www.example.nl[ ]xxx>

        String foundContent;
        if (endIndex < 0) {
            foundContent = truncatedHTML.substring(startIndex);
        } else {
            foundContent = truncatedHTML.substring(startIndex, endIndex);
        }

        if (foundContent.contains("\""))
            foundContent = foundContent.replace("\"","");

        if (foundContent.contains("'"))
            foundContent = foundContent.replace("'","");

        return foundContent;
    }



    /* Below are Tag ENUM's written down.
     *  Tag ENUM's will be used to get Elements
     *  from the truncated HTML String */

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
        private final boolean isAttributable;

        Tag(String tagName, boolean isAttributable) {
            this.tagName = tagName;
            this.isAttributable = isAttributable;
        }

        private static String getEndBracket() {
            return endBracket;
        }

        private boolean isAttributable() {
            return this.isAttributable;
        }

        private String getStartTag() {
            // build tag.
            // if no possible attributes in tag, end with endBracket. i.e. <html>
            // else if attributes possible in tag, end without endBracket. ie <a
            StringBuilder buildTag = new StringBuilder();
            buildTag.append(startBracket);
            buildTag.append(this.tagName);
            if (!this.isAttributable) {
                buildTag.append(endBracket);
            }
            return buildTag.toString();
        }

        private String getEndTag() {
            //build closingTag. ie. </html>
            String buildTag =
                    startBracket +
                    "/" +
                    this.tagName +
                    endBracket;

            return buildTag;
        }
    }


    /* Below are Attribute ENUM's written down.
     *  Attribute ENUM's are attributes inside
     *  HTML Tags and will be used to check if
     *  the attributes is present or get it's content */

    public enum Attribute {
        HREF("href=", " ");

        private final String startAttribute;
        private final String endAttribute;

        Attribute(String startAttribute, String endAttribute) {
            this.startAttribute = startAttribute;
            this.endAttribute = endAttribute;
        }

        private String getStartAttribute() {
            return this.startAttribute;
        }

        private String getEndAttribute() {
            return this.endAttribute;
        }
    }
}