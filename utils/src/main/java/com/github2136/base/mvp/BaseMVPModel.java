package com.github2136.base.mvp;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.github2136.util.SPUtil;


/**
 * model基础类
 */
public abstract class BaseMVPModel {
    protected AppCompatActivity mActivity;
    protected Fragment mFragment;

    protected String mTag;
    protected SPUtil mSpUtil;

    public BaseMVPModel(AppCompatActivity activity) {
        mActivity = activity;
        mTag = activity.getClass().getSimpleName();
        mSpUtil = SPUtil.getInstance(mActivity);
    }

    public BaseMVPModel(Fragment fragment) {
        mFragment = fragment;
        mTag = fragment.getClass().getSimpleName();
        mSpUtil = SPUtil.getInstance(mFragment.getContext());
    }

    public abstract void cancelRequest();
}