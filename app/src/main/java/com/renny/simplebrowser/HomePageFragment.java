package com.renny.simplebrowser;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.renny.simplebrowser.adapter.ExtendHeadAdapter;
import com.renny.simplebrowser.base.BaseFragment;
import com.renny.simplebrowser.widget.PullExtend.ExtendListHeader;
import com.renny.zxing.Activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;


/**
 * Created by yh on 2016/6/27.
 */
public class HomePageFragment extends BaseFragment implements View.OnClickListener {
    private goPageListener mGoPageListener;
    private String homePage = "https://juejin.im/user/5795bb80d342d30059f14b1c";
    private String baidu = "https://www.baidu.com/";
    private String github = "https://github.com/renjianan/SimpleBrowser";
    private static final int REQUEST_SCAN = 0;
    EditText mEditText;
    ExtendListHeader mPullNewHeader;
    RecyclerView mRecyclerView;
    List<String> mDatas = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.homepage;
    }


    public void afterViewBind(View rootView, Bundle savedInstanceState) {
        mEditText = rootView.findViewById(R.id.url_edit);
        mPullNewHeader = rootView.findViewById(R.id.extend_header);
        mRecyclerView = mPullNewHeader.getRecyclerView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        for (int i = 1; i < 10; i++) {
            mDatas.add("应用  " + i);
        }
        mRecyclerView.setAdapter(new ExtendHeadAdapter(getActivity(), R.layout.item_list, mDatas) );

        rootView.findViewById(R.id.profile).setOnClickListener(this);
        rootView.findViewById(R.id.baidu).setOnClickListener(this);
        rootView.findViewById(R.id.github).setOnClickListener(this);
        rootView.findViewById(R.id.scan).setOnClickListener(this);
        mEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String text = mEditText.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_GO && mGoPageListener != null) {
                    startBrowser(text);
                    mEditText.setText("");
                    return true;
                }
                return false;
            }
        });

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
                if (!isUrl(temp.replace(" ", ""))) {
                    mGoPageListener.onGopage("http://www.baidu.com/s?wd=" + text);
                } else {
                    mGoPageListener.onGopage(temp);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    jumpScanPage();
                } else {
                    Toast.makeText(getActivity(), "拒绝", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SCAN && resultCode == RESULT_OK) {
            startBrowser(data.getStringExtra("barCode"));
        }
    }

    /**
     * 跳转到扫码页
     */
    private void jumpScanPage() {
        startActivityForResult(new Intent(getActivity(), CaptureActivity.class), REQUEST_SCAN);
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
                case R.id.scan:
                    getRuntimeRight();
                    break;
            }
        }
    }

    /**
     * 获得运行时权限
     */
    private void getRuntimeRight() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            jumpScanPage();
        }
    }

    public interface goPageListener {
        void onGopage(String url);
    }

    private boolean isUrl(String url) {
        Pattern pattern = Pattern
                .compile("(http|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?");
        return pattern.matcher(url).matches();
    }
}
