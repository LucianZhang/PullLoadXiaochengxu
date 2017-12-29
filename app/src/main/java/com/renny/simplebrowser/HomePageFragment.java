package com.renny.simplebrowser;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.renny.zxing.Activity.CaptureActivity;

import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;


/**
 * Created by yh on 2016/6/27.
 */
public class HomePageFragment extends Fragment implements View.OnClickListener {
    private goPageListener mGoPageListener;
    private View rootView;
    private String homePage = "https://juejin.im/user/5795bb80d342d30059f14b1c";
    private String baidu = "https://www.baidu.com/";
    private String github = "https://github.com/renjianan/SimpleBrowser";
    private static final int REQUEST_SCAN = 0;
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
        rootView.findViewById(R.id.scan).setOnClickListener(this);
        mEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String text = mEditText.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_GO && mGoPageListener != null) {
                    startBrowser(text);
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
            } else if (!isUrl(text)) {
                mGoPageListener.onGopage("http://www.baidu.com/s?wd=" + text);
            } else {
                mGoPageListener.onGopage(text);
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
                .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
        return pattern.matcher(url).matches();
    }
}
