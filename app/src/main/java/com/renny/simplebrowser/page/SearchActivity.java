package com.renny.simplebrowser.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.renny.simplebrowser.R;
import com.renny.simplebrowser.adapter.HostAdapter;
import com.renny.simplebrowser.base.BaseActivity;
import com.renny.simplebrowser.base.CommonAdapter;
import com.renny.simplebrowser.business.helper.HostHelper;
import com.renny.simplebrowser.business.helper.KeyboardUtils;
import com.renny.simplebrowser.business.toast.ToastHelper;
import com.renny.simplebrowser.listener.SimpleTextWatcher;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {

    RecyclerView mRecyclerViewHeader, mRecyclerViewFooter;
    EditText searchEdit;
    Button actionBtn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }


    @Override
    public void bindView(Bundle savedInstanceState) {
        searchEdit = findViewById(R.id.search_edit);
        mRecyclerViewHeader = findViewById(R.id.list_header);
        mRecyclerViewFooter = findViewById(R.id.list_footer);
        actionBtn = findViewById(R.id.do_action);
        actionBtn.setOnClickListener(this);
    }

    @Override
    public void afterViewBind(Bundle savedInstanceState) {
        searchEdit.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String text = searchEdit.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    searchEdit.setText("");
                    startBrowser(text);
                    KeyboardUtils.hideSoftInput(SearchActivity.this);
                    finish();
                    return true;
                }
                return false;
            }
        });
        searchEdit.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    actionBtn.setText("取消");
                } else {
                    actionBtn.setText("进入");
                }
            }
        });
        mRecyclerViewHeader.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewFooter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        final List<String> headerList = new ArrayList<>();
        headerList.add("www.");
        headerList.add("wap.");
        headerList.add("m.");
        final List<String> footerList = new ArrayList<>();
        footerList.add(".com");
        footerList.add(".cn");
        footerList.add(".net");
        footerList.add(".edu");
        footerList.add(".cc");
        footerList.add(".io");
        footerList.add(".im");
        mRecyclerViewHeader.setAdapter(new HostAdapter(this, R.layout.item_host, headerList).setItemClickListener(new CommonAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(int position, View view) {
                String content = String.format("%s%s", searchEdit.getText().toString(), headerList.get(position));
                searchEdit.setText(content);
                searchEdit.setSelection(content.length());//将光标移至文字末尾
            }
        }));
        mRecyclerViewFooter.setAdapter(new HostAdapter(this, R.layout.item_host, footerList).setItemClickListener(new CommonAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(int position, View view) {
                String content = String.format("%s%s", searchEdit.getText().toString(), footerList.get(position));
                searchEdit.setText(content);
                searchEdit.setSelection(content.length());//将光标移至文字末尾
            }
        }));
    }

    @Override
    public void onResume() {
        super.onResume();
        KeyboardUtils.showSoftInput(this, searchEdit);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        switch (id) {
            case R.id.do_action:
                String text = searchEdit.getText().toString();
                if (!text.isEmpty()) {
                    startBrowser(text);
                }
                finish();
                KeyboardUtils.hideSoftInput(this);
                break;
        }
    }

    private void startBrowser(String text) {
        if (TextUtils.isEmpty(text)) {
            ToastHelper.makeToast("请输入网址");
        } else {
            String temp = text;
            if (!temp.startsWith("http") && !temp.startsWith("https")) {
                temp = "https://" + temp;
            }
            Intent intent = new Intent();
            if (!HostHelper.isUrl(temp.replace(" ", ""))) {
                intent.putExtra("url", "http://www.baidu.com/s?wd=" + text);
                setResult(111, intent);
            } else {
                intent.putExtra("url", temp);
                setResult(111, intent);
            }
        }
    }


}
