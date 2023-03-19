package com.webcrawler.webcrawlerapp.io;

public enum Attribute {
    HREF("href=", " ");

    private final String startAttribute;
    private final String endAttribute;

    Attribute(String startAttribute, String endAttribute) {
        this.startAttribute = startAttribute;
        this.endAttribute = endAttribute;
    }

    String getStartAttribute() {
        return this.startAttribute;
    }

    String getEndAttribute() {
        return this.endAttribute;
    }
}
