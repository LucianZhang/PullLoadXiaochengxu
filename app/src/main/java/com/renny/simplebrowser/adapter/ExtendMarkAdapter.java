package com.renny.simplebrowser.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.renny.simplebrowser.R;
import com.renny.simplebrowser.base.CommonAdapter;
import com.renny.simplebrowser.base.ViewHolder;
import com.renny.simplebrowser.business.db.entity.Mark;

import java.util.List;

/**
 * Created by Renny on 2018/1/3.
 */

public class ExtendMarkAdapter extends CommonAdapter<Mark> {

    public ExtendMarkAdapter(Context context, int layoutId, List<Mark> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final int position) {
        Mark data=getData(position);
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
    }


}
