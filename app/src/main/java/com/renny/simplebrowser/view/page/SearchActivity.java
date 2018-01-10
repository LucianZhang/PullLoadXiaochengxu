package com.renny.simplebrowser.view.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.renny.simplebrowser.R;
import com.renny.simplebrowser.business.base.BaseActivity;
import com.renny.simplebrowser.business.base.CommonAdapter;
import com.renny.simplebrowser.business.helper.KeyboardUtils;
import com.renny.simplebrowser.business.helper.Validator;
import com.renny.simplebrowser.business.http.constants.Apis;
import com.renny.simplebrowser.business.toast.ToastHelper;
import com.renny.simplebrowser.globe.http.callback.ApiCallback;
import com.renny.simplebrowser.globe.http.reponse.IResult;
import com.renny.simplebrowser.globe.task.TaskHelper;
import com.renny.simplebrowser.view.adapter.HostAdapter;
import com.renny.simplebrowser.view.bean.SuggestionHost;
import com.renny.simplebrowser.view.listener.SimpleTextWatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends BaseActivity {

    RecyclerView mRecyclerViewHeader, mRecyclerViewFooter;
    EditText searchEdit;
    Button actionBtn;
    ListView keyListView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void bindView(Bundle savedInstanceState) {
        searchEdit = findViewById(R.id.search_edit);
        mRecyclerViewHeader = findViewById(R.id.list_header);
        mRecyclerViewFooter = findViewById(R.id.list_footer);
        keyListView = findViewById(R.id.search_key);
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
                String textContain = s.toString();
                if (TextUtils.isEmpty(textContain)) {
                    actionBtn.setText("取消");
                    keyListView.setAdapter(new ArrayAdapter<>(SearchActivity.this, R.layout.item_host, new ArrayList<>()));
                } else {
                    if (!Validator.checkUrl(textContain)) {
                        actionBtn.setText("搜索");
                    } else {
                        actionBtn.setText("进入");
                    }
                    HashMap<String, String> params = new HashMap<>();
                    params.put("wd", s.toString());
                    TaskHelper.apiCall(Apis.searchSuggestion, params, new ApiCallback<List<String>>() {
                        @Override
                        public void onSuccess(IResult<List<String>> result) {
                            final List<String> keyList = result.data();
                            if (keyList != null && !result.data().isEmpty()) {
                                keyListView.setAdapter(new ArrayAdapter<>(SearchActivity.this, R.layout.item_suggestion_word, keyList));
                                keyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        searchEdit.setText(keyList.get(position));
                                        searchEdit.setSelection(keyList.get(position).length());
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
        mRecyclerViewHeader.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewFooter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        TaskHelper.apiCall(Apis.host, null, new ApiCallback<SuggestionHost>() {
            @Override
            public void onSuccess(IResult<SuggestionHost> result) {
                SuggestionHost suggestionHost = result.data();
                final List<String> headerList = suggestionHost.getHeader();
                final List<String> footerList = suggestionHost.getFooter();
                if (headerList != null) {
                    mRecyclerViewHeader.setAdapter(new HostAdapter(headerList).setItemClickListener(new CommonAdapter.ItemClickListener() {
                        @Override
                        public void onItemClicked(int position, View view) {
                            String content = String.format("%s%s", searchEdit.getText().toString(), headerList.get(position));
                            searchEdit.setText(content);
                            searchEdit.setSelection(content.length());//将光标移至文字末尾
                        }
                    }));
                }
                if (footerList != null) {
                    mRecyclerViewFooter.setAdapter(new HostAdapter(footerList).setItemClickListener(new CommonAdapter.ItemClickListener() {
                        @Override
                        public void onItemClicked(int position, View view) {
                            String content = String.format("%s%s", searchEdit.getText().toString(), footerList.get(position));
                            searchEdit.setText(content);
                            searchEdit.setSelection(content.length());//将光标移至文字末尾
                        }
                    }));
                }
            }
        });

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
            Intent intent = new Intent();
            if (!Validator.checkUrl(text)) {
                intent.putExtra("url", "http://wap.baidu.com/s?wd=" + text);
                setResult(111, intent);
            } else {
                intent.putExtra("url", text);
                setResult(111, intent);
            }
        }
    }

}
