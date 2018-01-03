package com.renny.simplebrowser.widget.PullExtend;

/**
 * Created by Renny on 2018/1/2.
 */

public interface OnRefreshListener {

    /**
     * 下拉松手后会被调用
     *
     * @param refreshView 刷新的View
     */
    void onPullDownToRefresh(final PullExtendLayout refreshView);

    /**
     * 加载更多时会被调用或上拉时调用
     *
     * @param refreshView 刷新的View
     */
    void onPullUpToRefresh(final PullExtendLayout refreshView);
}

