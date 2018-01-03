package com.renny.simplebrowser.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Renny on 2018/1/3.
 */

public abstract class BaseFragment extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            initParams(getArguments());
        }
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
            afterViewBind(rootView, savedInstanceState);
        }
        return rootView;
    }

    protected void initParams(Bundle bundle) {

    }

    protected abstract int getLayoutId();

    public void afterViewBind(View rootView, Bundle savedInstanceState) {
    }

}
