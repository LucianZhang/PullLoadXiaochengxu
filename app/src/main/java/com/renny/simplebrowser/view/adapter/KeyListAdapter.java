package com.renny.simplebrowser.view.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.renny.simplebrowser.R;
import com.renny.simplebrowser.business.base.CommonAdapter;
import com.renny.simplebrowser.business.base.ViewHolder;

import java.util.List;

/**
 * Created by Renny on 2018/1/3.
 */

public class KeyListAdapter extends CommonAdapter<String> {
    private OnClickListener mOnClickListener;

    public KeyListAdapter(List<String> datas) {
        super(R.layout.item_suggestion_word, datas);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    protected void convert(ViewHolder holder, final int position) {
        String data = getData(position);
        TextView textView = holder.getView(R.id.word);
        textView.setText(data);
        ImageView imageView = holder.getView(R.id.go);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListener != null) {
                    mOnClickListener.onWordClick(position, view);
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListener != null) {
                    mOnClickListener.onGoClick(position, view);
                }
            }
        });
    }

    public interface OnClickListener {
        void onWordClick(int position, View view);

        void onGoClick(int position, View view);
    }

}
