package com.summer.framework.webmvc.servlet;

public enum ContentType {

    defaultContentType("html", "text/html;charset=utf-8"),
    jsonContentType("json", "text/json;charset=utf-8");

    public String key;
    public String value;

    ContentType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static void main(String[] args) {
        System.out.println(ContentType.jsonContentType.value);
    }

}
