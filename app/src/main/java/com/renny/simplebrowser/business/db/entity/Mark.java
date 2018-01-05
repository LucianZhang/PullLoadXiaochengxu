package com.renny.simplebrowser.business.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Renny on 2018/1/5.
 */
@DatabaseTable(tableName = "tb_mark")
public class Mark {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "title")
    String title;
    @DatabaseField(columnName = "url")
    String url;

    public Mark() {
    }

    public Mark(String title, String url) {
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
