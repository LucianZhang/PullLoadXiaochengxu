package com.renny.simplebrowser.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.renny.simplebrowser.R;
import com.renny.simplebrowser.base.CommonAdapter;
import com.renny.simplebrowser.base.ViewHolder;

import java.util.List;

/**
 * Created by Renny on 2018/1/3.
 */

public class ExtendHeadAdapter extends CommonAdapter<String> {

    public ExtendHeadAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final int position) {
        String data=getData(position);
        TextView tv = holder.getView(R.id.item_title);
        tv.setText(data);
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
