package com.renny.simplebrowser.page;

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
import android.widget.Toast;

import com.renny.simplebrowser.R;
import com.renny.simplebrowser.base.BaseFragment;
import com.renny.simplebrowser.base.CommonAdapter;
import com.renny.simplebrowser.base.ViewHolder;
import com.renny.simplebrowser.business.helper.HostHelper;
import com.renny.simplebrowser.business.helper.KeyboardUtils;
import com.renny.simplebrowser.listener.SimpleTextWatcher;
import com.renny.simplebrowser.listener.goPageListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Renny on 2018/1/5.
 */

public class SearchFragment extends BaseFragment {
    private goPageListener mGoPageListener;
    RecyclerView mRecyclerViewHeader, mRecyclerViewFooter;
    EditText searchEdit;
    Button actionBtn;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initParams(Bundle bundle) {
        super.initParams(bundle);
    }

    @Override
    public void bindView(View rootView, Bundle savedInstanceState) {
        searchEdit = rootView.findViewById(R.id.search_edit);
        mRecyclerViewHeader = rootView.findViewById(R.id.list_header);
        mRecyclerViewFooter = rootView.findViewById(R.id.list_footer);
        actionBtn = rootView.findViewById(R.id.do_action);
        actionBtn.setOnClickListener(this);
    }

    @Override
    public void afterViewBind(View rootView, Bundle savedInstanceState) {
        searchEdit.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String text = searchEdit.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_GO && mGoPageListener != null) {
                    startBrowser(text);
                    searchEdit.setText("");
                    return true;
                }
                return false;
            }
        });
        searchEdit.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())){
                    actionBtn.setText("取消");
                }else {
                    actionBtn.setText("进入");
                }
            }
        });
        mRecyclerViewHeader.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewFooter.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
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
        mRecyclerViewHeader.setAdapter(new CommonAdapter<String>(getContext(), R.layout.item_host, headerList) {
            @Override
            protected void convert(ViewHolder holder, int position) {
                TextView textView = holder.getView(R.id.item_title);
                textView.setText(headerList.get(position));
            }
        });
        mRecyclerViewFooter.setAdapter(new CommonAdapter<String>(getContext(), R.layout.item_host, footerList) {
            @Override
            protected void convert(ViewHolder holder, int position) {
                TextView textView = holder.getView(R.id.item_title);
                textView.setText(footerList.get(position));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        KeyboardUtils.showSoftInput(getActivity(), searchEdit);
    }

    public void setGoPageListener(goPageListener goPageListener) {
        mGoPageListener = goPageListener;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        switch (id) {
            case R.id.do_action:
                String text = searchEdit.getText().toString();
                if (text.isEmpty()) {
                    mGoPageListener.onGopage("");
                } else {
                    startBrowser(text);
                }
                break;
        }
    }

    private void startBrowser(String text) {
        if (mGoPageListener != null) {
            if (TextUtils.isEmpty(text)) {
                Toast.makeText(getActivity(), "请输入网址", Toast.LENGTH_SHORT).show();
            } else {
                String temp = text;
                if (!temp.startsWith("http") && !temp.startsWith("https")) {
                    temp = "https://" + temp;
                }
                if (!HostHelper.isUrl(temp.replace(" ", ""))) {
                    mGoPageListener.onGopage("http://www.baidu.com/s?wd=" + text);
                } else {
                    mGoPageListener.onGopage(temp);
                }
            }
        }
    }
}
