package com.renny.simplebrowser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;


/**
 * Created by yh on 2016/6/27.
 */
public class HomePageFragment extends Fragment implements View.OnClickListener {
    private goPageListener mGoPageListener;
    private View rootView;
    private String homePage = "https://juejin.im/user/5795bb80d342d30059f14b1c";
    private String baidu = "https://www.baidu.com/";
    private String github = "https://github.com/renjianan/SimpleBrowser";

    EditText mEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.homepage, container, false);
            afterViewBind(savedInstanceState);
        }
        return rootView;
    }


    public void afterViewBind(Bundle savedInstanceState) {
        mEditText = rootView.findViewById(R.id.url_edit);
        rootView.findViewById(R.id.profile).setOnClickListener(this);
        rootView.findViewById(R.id.baidu).setOnClickListener(this);
        rootView.findViewById(R.id.github).setOnClickListener(this);
        mEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    String text = mEditText.getText().toString();
                    if (TextUtils.isEmpty(text) || !isUrl(text)) {
                        Toast.makeText(getActivity(), "请输入网址", Toast.LENGTH_SHORT).show();
                    } else if (mGoPageListener != null) {
                        mGoPageListener.onGopage(text);
                    }
                    return true;
                }
                return false;
            }
        });

    }

    public void setGoPageListener(goPageListener goPageListener) {
        mGoPageListener = goPageListener;
    }

    @Override
    public void onClick(View v) {
        if (mGoPageListener != null) {
            int id = v.getId();
            switch (id) {
                case R.id.profile:
                    mGoPageListener.onGopage(homePage);
                    break;
                case R.id.baidu:
                    mGoPageListener.onGopage(baidu);
                    break;
                case R.id.github:
                    mGoPageListener.onGopage(github);
                    break;
            }
        }
    }

    public interface goPageListener {
        void onGopage(String url);
    }

    private boolean isUrl(String url) {
        Pattern pattern = Pattern
                .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
        return pattern.matcher(url).matches();
    }
}
