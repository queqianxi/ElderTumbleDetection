package com.ww.ll;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ww.ll.base.BaseFragment;

/**
 *
 * @author Ww
 * @date 2018/5/16
 */
public class AboutFragment extends BaseFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about,container,false);
        return view;
    }

    @Override
    protected int setView() {
        return 0;
    }

    @Override
    protected void init(View view) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }
}
