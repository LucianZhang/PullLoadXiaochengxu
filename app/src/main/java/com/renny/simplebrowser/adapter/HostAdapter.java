package com.renny.simplebrowser.adapter;

import android.view.View;
import android.widget.TextView;

import com.renny.simplebrowser.R;
import com.renny.simplebrowser.base.CommonAdapter;
import com.renny.simplebrowser.base.ViewHolder;

import java.util.List;

/**
 * Created by Renny on 2018/1/3.
 *
 */

public class HostAdapter extends CommonAdapter<String> {

    public HostAdapter(List<String> datas) {
        super(R.layout.item_host, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final int position) {
        String data = getData(position);
        TextView textView = holder.getView(R.id.item_title);
        textView.setText(data);
        if (mItemClickListener != null) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClicked(position, v);
                }
            });
        }
    }


}
