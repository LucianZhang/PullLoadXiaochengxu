package com.renny.simplebrowser.view.page;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.renny.simplebrowser.R;
import com.renny.simplebrowser.business.base.CommonAdapter;
import com.renny.simplebrowser.business.base.ViewHolder;
import com.renny.simplebrowser.view.widget.pullextend.ExtendListHeader;
import com.renny.simplebrowser.view.widget.pullextend.PullExtendLayoutForRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        PullExtendLayoutForRecyclerView pullExtendLayoutForRecyclerView=findViewById(R.id.parent);
        pullExtendLayoutForRecyclerView.setPullLoadEnabled(false);
        ExtendListHeader mPullNewHeader = findViewById(R.id.extend_header);
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

    }

}
