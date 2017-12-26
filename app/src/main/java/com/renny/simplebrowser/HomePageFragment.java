package com.renny.simplebrowser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by yh on 2016/6/27.
 */
public class HomePageFragment extends Fragment implements View.OnClickListener{

    View rootView;
    // Fragment管理对象
    private FragmentManager manager;
    private FragmentTransaction ft;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.webview_item, container, false);
            afterViewBind(savedInstanceState);
        }
        return rootView;
    }




    public void afterViewBind(Bundle savedInstanceState) {

    }


    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.profile:

        }
    }
}
