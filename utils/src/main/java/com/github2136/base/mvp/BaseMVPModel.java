package com.github2136.base.mvp;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.github2136.util.JsonUtil;
import com.github2136.util.SPUtil;


/**
 * model基础类
 */
public abstract class BaseMVPModel {
    protected AppCompatActivity mActivity;
    protected Fragment mFragment;

    protected String mTag;
    protected SPUtil mSpUtil;
    protected JsonUtil mJsonUtil;

    public BaseMVPModel(AppCompatActivity activity) {
        mActivity = activity;
        mTag = activity.getClass().getSimpleName();
        initMode();
    }

    public BaseMVPModel(Fragment fragment) {
        mFragment = fragment;
        mTag = fragment.getClass().getSimpleName();
    }

    private void initMode() {
        mJsonUtil = JsonUtil.getInstance();
        if (mActivity == null) {
            mSpUtil = SPUtil.getInstance(mFragment.getContext());
        } else {
            mSpUtil = SPUtil.getInstance(mActivity);
        }
    }

    public abstract void cancelRequest();
}