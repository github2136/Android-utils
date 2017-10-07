package com.github2136.base.mvp;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.github2136.util.JsonUtil;
import com.github2136.util.SPUtil;

import java.util.Set;


/**
 * presenter基础类
 */
public abstract class BaseMVPPresenter<V extends IBaseMVPView> {
    protected V mView;
    protected AppCompatActivity mActivity;
    protected Fragment mFragment;
    protected final String failedStr = "无法连接服务器";
    protected JsonUtil mJsonUtil;
    protected SPUtil mSpUtil;
    protected Handler mHandler;

    public BaseMVPPresenter(AppCompatActivity activity, V view) {
        mActivity = activity;
        initPresenter(view);
    }

    public BaseMVPPresenter(Fragment fragment, V view) {
        mFragment = fragment;
        initPresenter(view);
    }

    private void initPresenter(V view) {
        this.mView = view;
        mJsonUtil = JsonUtil.getInstance();
        mHandler = new Handler(Looper.getMainLooper());
        if (mActivity == null) {
            mSpUtil = SPUtil.getInstance(mFragment.getContext());
        } else {
            mSpUtil = SPUtil.getInstance(mActivity);
        }
    }

    public String getSPString(String key) {
        return mSpUtil.getString(key);
    }

    public boolean getSPBoolean(String key) {
        return mSpUtil.getBoolean(key);
    }

    public float getSPFloat(String key) {
        return mSpUtil.getFloat(key);
    }

    public int getSPInt(String key) {
        return mSpUtil.getInt(key);
    }

    public long getSPLong(String key) {
        return mSpUtil.getLong(key);
    }

    public Set<String> getSPStringSet(String key) {
        return mSpUtil.getStringSet(key);
    }

    public void postMain(Runnable runnable) {
        mHandler.post(runnable);
    }

    //取消请求
    public abstract void cancelRequest();
}
