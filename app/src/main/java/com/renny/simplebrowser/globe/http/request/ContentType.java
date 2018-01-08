package com.renny.simplebrowser.globe.http.request;


import com.renny.simplebrowser.globe.http.constants.Charsets;

import java.nio.charset.Charset;

/**
 */
public enum ContentType {
    APP_FORM_URLENCODED("application/x-www-form-urlencoded"),
    APP_JSON("application/json"),
    APP_OCTET_STREAM("application/octet-stream"),
    MULTIPART_FORM_DATA("multipart/form-data"),
    TEXT_HTML("text/html"),
    TEXT_PLAIN("text/plain"),
    WILDCARD("*/*");

    private final String mimeType;
    private final Charset charset;

    ContentType(String mimeType, Charset charset) {
        this.mimeType = mimeType;
        this.charset = charset;
    }

    ContentType(String mimeType) {
        this.mimeType = mimeType;
        this.charset = Charsets.UTF_8;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public Charset getCharset() {
        return this.charset;
    }

    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder(64);
        buf.append(this.mimeType);
        if (this.charset != null) {
            buf.append("; charset=");
            buf.append(this.charset.name());
        }
        return buf.toString();
    }

}
