package com.renny.simplebrowser.widget.pullextend;


/**
 * 定义了拉动刷新的接口
 * 
 */
public interface IPullToExtend {
    
    /**
     * 设置当前下拉刷新是否可用
     * 
     * @param pullRefreshEnabled true表示可用，false表示不可用
     */
     void setPullRefreshEnabled(boolean pullRefreshEnabled);
    
    /**
     * 设置当前上拉加载更多是否可用
     * 
     * @param pullLoadEnabled true表示可用，false表示不可用
     */
     void setPullLoadEnabled(boolean pullLoadEnabled);

    /**
     * 判断当前下拉刷新是否可用
     * 
     * @return true如果可用，false不可用
     */
     boolean isPullRefreshEnabled();
    
    /**
     * 判断上拉加载是否可用
     * 
     * @return true可用，false不可用
     */
     boolean isPullLoadEnabled();

    /**
     * 得到Header布局对象
     * 
     * @return Header布局对象
     */
     ExtendLayout getHeaderExtendLayout();
    
    /**
     * 得到Footer布局对象
     * 
     * @return Footer布局对象
     */
     ExtendLayout getFooterExtendLayout();
    

}
