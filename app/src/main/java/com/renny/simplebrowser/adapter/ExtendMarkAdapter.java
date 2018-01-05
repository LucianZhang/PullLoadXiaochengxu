package com.renny.simplebrowser.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.renny.simplebrowser.R;
import com.renny.simplebrowser.base.CommonAdapter;
import com.renny.simplebrowser.base.ViewHolder;
import com.renny.simplebrowser.business.db.entity.BookMark;

import java.util.List;

/**
 * Created by Renny on 2018/1/3.
 */

public class ExtendMarkAdapter extends CommonAdapter<BookMark> {
    private ItemLongClickListener mLongClickListener;
    public ExtendMarkAdapter(Context context, int layoutId, List<BookMark> datas) {
        super(context, layoutId, datas);
    }

    public void setLongClickListener(ItemLongClickListener longClickListener) {
        mLongClickListener = longClickListener;
    }

    @Override
    protected void convert(ViewHolder holder, final int position) {
        BookMark data=getData(position);
        TextView tv = holder.getView(R.id.item_title);
        tv.setText(data.getTitle());
        if (mItemClickListener != null) {
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClicked(position, v);
                }
            });
        }
        if (mLongClickListener != null) {
            tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongClickListener.onItemLongClicked(position, v);
                    return true;
                }
            });
        }
    }
    public interface ItemLongClickListener {
        void onItemLongClicked(int position, View view);
    }

}
