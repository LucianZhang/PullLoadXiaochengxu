package com.renny.simplebrowser.business.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Renny on 2018/1/5.
 */
@DatabaseTable(tableName = "tb_mark")
public class BookMark {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "title")
    private String title;
    @DatabaseField(columnName = "url")
    private String url;

    public BookMark() {
    }

    public BookMark(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
