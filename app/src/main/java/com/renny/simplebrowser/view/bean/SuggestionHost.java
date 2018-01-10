package com.renny.simplebrowser.view.bean;

import java.util.List;

/**
 * Created by Renny on 2018/1/10.
 */

public class SuggestionHost {

    private List<String> header;
    private List<String> footer;

    public List<String> getHeader() {
        return header;
    }

    public void setHeader(List<String> header) {
        this.header = header;
    }

    public List<String> getFooter() {
        return footer;
    }

    public void setFooter(List<String> footer) {
        this.footer = footer;
    }
}
