package com.github2136.base.mvp;

import android.app.Service;
import android.support.v4.app.Fragment;
import android.support.v4.app.ServiceCompat;
import android.support.v7.app.AppCompatActivity;

import com.github2136.util.JsonUtil;
import com.github2136.util.SPUtil;


/**
 * model基础类
 */
public abstract class BaseMVPModel {
    protected AppCompatActivity mActivity;
    protected Fragment mFragment;
    protected Service mService;

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
        initMode();
    }

    public BaseMVPModel(Service service) {
        mService = service;
        mTag = service.getClass().getSimpleName();
        initMode();
    }

    private void initMode() {
        mJsonUtil = JsonUtil.getInstance();
        if (mActivity != null) {
            mSpUtil = SPUtil.getInstance(mActivity);
        } else if (mFragment != null) {

            mSpUtil = SPUtil.getInstance(mFragment.getContext());
        } else if (mService != null) {
            mSpUtil = SPUtil.getInstance(mService);
        }
    }

    public abstract void cancelRequest();
}