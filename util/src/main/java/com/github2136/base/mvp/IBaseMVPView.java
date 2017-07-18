package com.github2136.base.mvp;

import android.support.annotation.StringRes;

/**
 * view基础接口
 */
public interface IBaseMVPView {
    void showProgressDialog(@StringRes int resId);

    // 显示进度框
    void showProgressDialog(String... msg);

    // 关闭进度框
    void dismissDialogDialog();
}
