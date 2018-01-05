package com.renny.simplebrowser.business.db.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.renny.simplebrowser.App;
import com.renny.simplebrowser.business.db.DatabaseHelper;
import com.renny.simplebrowser.business.db.entity.Mark;
import com.renny.simplebrowser.business.log.Logs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Renny on 2018/1/5.
 */

public class MarkDao {

    private Dao<Mark, Integer> mMarkDao;

    public MarkDao() {
        try {
            mMarkDao = DatabaseHelper.getHelper(App.getContext()).getmarkDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMark(Mark mark) {
        try {
            mMarkDao.create(mark);
        } catch (SQLException e) {
            Logs.base.e(e);
        }
    }

    public void addMarkList(List<Mark> markList) {
        try {
            mMarkDao.create(markList);
        } catch (SQLException e) {
            Logs.base.e(e);
        }
    }

    /**
     * 删除一条记录
     */
    public void delete(String url) {
        try {
            DeleteBuilder<Mark, Integer> deleteBuilder = mMarkDao.deleteBuilder();
            deleteBuilder.where().eq("url",url);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询一条记录
     */
    public boolean query(String url) {
        List<Mark> markList = null;
        try {
            markList = mMarkDao.queryBuilder().where().eq("url", url).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return markList != null && markList.size() > 0;
    }

    /**
     * 查询所有记录
     *
     */
    public List<Mark> queryForAll() {
        List<Mark> markList = new ArrayList<>();
        try {
            markList = mMarkDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return markList;
    }


}
