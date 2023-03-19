package com.webcrawler.webcrawlerapp.io;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WebDocument extends WebCrawler {

    private String htmlDocument;

    public WebDocument(String url) {
        super(url);
        this.htmlDocument = super.getHTML();
    }

    public String getHtmlDocument() {
        return this.htmlDocument;
    }

    public List<String> getElementList(Tag tag, int maxHits) {
        return getElementList(tag, htmlDocument);
    }

    public List<String> getElementList(Tag tag) {
        return getElementList(tag, htmlDocument);
    }

    public List<String> getElementListFromElementHTML(Tag getTag, Tag fromTag) {
        return getElementList(getTag, getElementHTML(fromTag));
    }

    public boolean containElement(Tag tag, String html) {
        return html.contains(tag.getStartTag());
    }

    public List<String> getElementContent(Tag tag, String truncatedHTML) {

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

    private List<String> getElementList(Tag tag, String truncatedHTML) {
        List<String> collectHTMLList = new ArrayList<>();

        if (truncatedHTML == null)
            return collectHTMLList;


        int stringIndex = 0;

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

    private String getElementHTML(Tag tag) {
        int stringIndex = 0;
        StringBuilder HTMLCollection = new StringBuilder();
        while (true) {

            int startIndex = htmlDocument.indexOf(tag.getStartTag(), stringIndex);

            if (startIndex < 0)
                break;
            int endIndex = htmlDocument.indexOf(tag.getEndTag(), startIndex);
            int correctedEndIndex = endIndex + tag.getEndTag().length();

            String foundHTML;
            if (endIndex < 0) {
                foundHTML = htmlDocument.substring(startIndex);
            } else {
                foundHTML = htmlDocument.substring(startIndex, correctedEndIndex);
            }

            HTMLCollection.append(foundHTML);
            stringIndex = (endIndex > 0) ? correctedEndIndex : (startIndex + 1);
        }

        return HTMLCollection.toString();
    }

    /////////////////////////////
    public String reformatUrl(String urlDomain, String subUrl) {
        String reformatUrl = subUrl;
        if(subUrl.charAt(0) == '/')
            reformatUrl = urlDomain + subUrl;
        return reformatUrl;
    }

    public String getContentAndSubtractAllElements(Tag tag) {
        List<String> elementList = getElementList(tag);
        String trunHTML = truncateListToString(elementList);
        String start = tag.getStartBracket();
        String end = tag.getEndBracket();
        trunHTML = trunHTML.replaceAll(start+".*?"+end, "\t");
        return trunHTML;
    }

    private String truncateListToString(List<String> htmlList) {
        StringBuilder truncateHTML = new StringBuilder();
        for (String html : htmlList) {
            truncateHTML.append(html).append("\t");
        }
        return truncateHTML.toString();
    }

    /* Below are Attribute methods */

    public boolean containAttribute(Attribute attribute, String truncatedHTML) {
        return truncatedHTML.contains(attribute.getStartAttribute());
    }

    public List<String> getElementList(Attribute attribute, Tag tag) {
        return getElementList(tag, htmlDocument)
                .stream()
                .map(e -> getAttributeContent(attribute, e)).toList();
    }

    public String getAttributeContent(Attribute attribute, String truncatedHTML) {

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
}