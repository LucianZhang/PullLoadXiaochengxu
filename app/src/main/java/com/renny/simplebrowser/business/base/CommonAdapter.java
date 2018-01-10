package com.renny.simplebrowser.business.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;
import java.util.List;

/**
 * Created by Renny on 2018/1/3.
 */

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    private int mLayoutId;
    private List<T> dataList;
    protected ItemClickListener mItemClickListener;

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public CommonAdapter(int layoutId, List<T> datas) {
        mLayoutId = layoutId;
        dataList = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        return ViewHolder.get(parent, mLayoutId);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        convert(holder, position);
    }


    protected T getData(int position) {
        return dataList.get(position);
    }

    protected abstract void convert(ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public CommonAdapter setItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
        return this;
    }

    public void removeData(int position) {
        if (dataList != null && dataList.size() > position) {
            dataList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount()); //刷新被删除数据，以及其后面的数据
        }
    }

    public void clear() {
        if (dataList != null) {
            dataList.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * 填充数据,此操作会清除原来的数据
     *
     * @param list 要填充的数据
     * @return true:填充成功并调用刷新数据
     */
    public boolean setDataWithNotify(Collection<T> list) {
        boolean result = false;
        dataList.clear();
        if (list != null) {
            result = dataList.addAll(list);
        }
        notifyDataSetChanged();
        return result;
    }

    public interface ItemClickListener {
        void onItemClicked(int position, View view);
    }
}