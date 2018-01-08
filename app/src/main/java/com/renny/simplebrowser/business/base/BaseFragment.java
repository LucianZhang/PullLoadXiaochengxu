package com.renny.simplebrowser.business.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Renny on 2018/1/3.
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            initParams(getArguments());
        }
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
            bindView(rootView, savedInstanceState);
            afterViewBind(rootView, savedInstanceState);
        }
        return rootView;
    }

    protected void initParams(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }

    protected abstract int getLayoutId();

    public void bindView(View rootView, Bundle savedInstanceState) {

    }

    public void afterViewBind(View rootView, Bundle savedInstanceState) {
    }

}
