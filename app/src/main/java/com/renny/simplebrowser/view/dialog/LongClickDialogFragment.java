package com.renny.simplebrowser.view.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.renny.simplebrowser.R;
import com.renny.simplebrowser.business.base.CommonAdapter;
import com.renny.simplebrowser.business.base.ViewHolder;
import com.renny.simplebrowser.business.helper.UIHelper;
import com.renny.simplebrowser.view.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Renny on 2018/1/10.
 */

public class LongClickDialogFragment extends DialogFragment {
    private RecyclerView mRecyclerView;
    private int LocationX = 0;
    private int LocationY = 0;
    OnItemClickListener mOnItemClickListener;

    public static LongClickDialogFragment getInstance(int locationX, int locationY) {
        LongClickDialogFragment longClickDialogFragment = new LongClickDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("intX", locationX);
        bundle.putInt("intY", locationY);
        longClickDialogFragment.setArguments(bundle);
        return longClickDialogFragment;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            initParams(getArguments());
        }

        View rootView = inflater.inflate(R.layout.popup_list, container, false);
        bindView(rootView, savedInstanceState);
        afterViewBind(rootView, savedInstanceState);
        return rootView;
    }


    public void bindView(View rootView, Bundle savedInstanceState) {
        mRecyclerView = rootView.findViewById(R.id.popup_list);

    }

    public void afterViewBind(View rootView, Bundle savedInstanceState) {
        final List<String> list = new ArrayList<>();
        list.add("保存图片");
        list.add("取消");
        mRecyclerView.setAdapter(new CommonAdapter<String>(R.layout.item_popup_list, list) {
            @Override
            protected void convert(ViewHolder holder, final int position) {
                TextView tv = holder.getView(R.id.item_title);
                tv.setText(list.get(position));
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mItemClickListener.onItemClicked(position, view);
                    }
                });
            }
        }.setItemClickListener(new CommonAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(int position, View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClicked(position);
                }
                dismiss();
            }
        }));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    protected void initParams(Bundle bundle) {
        LocationX = bundle.getInt("intX");
        LocationY = bundle.getInt("intY");
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams lp = window.getAttributes();
                window.setGravity(Gravity.LEFT | Gravity.TOP);
                lp.x = LocationX;
                lp.y = LocationY;
                lp.width = UIHelper.dip2px(100);
                lp.dimAmount = 0.0f;
                lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
    }


}

