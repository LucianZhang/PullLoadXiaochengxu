package com.renny.simplebrowser.view.page;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.renny.simplebrowser.R;
import com.renny.simplebrowser.business.base.CommonAdapter;
import com.renny.simplebrowser.business.base.ViewHolder;
import com.renny.simplebrowser.business.toast.ToastHelper;
import com.renny.simplebrowser.view.adapter.ExtendHeadAdapter;
import com.renny.simplebrowser.view.widget.pullextend.ExtendListHeader;
import com.renny.simplebrowser.view.widget.pullextend.PullExtendLayoutForRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    RecyclerView listHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        PullExtendLayoutForRecyclerView pullExtendLayoutForRecyclerView=findViewById(R.id.parent);
        pullExtendLayoutForRecyclerView.setPullLoadEnabled(false);
        ExtendListHeader mPullNewHeader = findViewById(R.id.extend_header);
        listHeader = mPullNewHeader.getRecyclerView();
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        final List<String> headerList = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            headerList.add("item" + i);
        }
        mRecyclerView.setAdapter(new CommonAdapter(android.R.layout.simple_expandable_list_item_1, headerList) {
            @Override
            protected void convert(ViewHolder holder, int position) {
                String data = headerList.get(position);
                TextView textView = holder.getView(android.R.id.text1);
                textView.setText(data);
            }
        });

        final List<String> mDatas = new ArrayList<>();
        mDatas.add("历史记录");
        mDatas.add("无痕浏览");
        mDatas.add("新建窗口");
        mDatas.add("无图模式");
        mDatas.add("夜间模式");

        listHeader.setLayoutManager(new LinearLayoutManager(this,OrientationHelper.HORIZONTAL,false));
        listHeader.setAdapter(new ExtendHeadAdapter(mDatas).setItemClickListener(new CommonAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(int position, View view) {
                ToastHelper.makeToast(mDatas.get(position) + " 功能待实现");
            }
        }));
    }

}
