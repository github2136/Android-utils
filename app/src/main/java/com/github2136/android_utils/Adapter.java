package com.github2136.android_utils;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.github2136.base.BaseListAdapter;
import com.github2136.base.ViewHolderListView;

import java.util.List;

/**
 * Created by yubin on 2017/5/25.
 */

public class Adapter extends BaseListAdapter<String> {
    public Adapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void getItemView(String s, ViewHolderListView holder, int position, View convertView) {

    }
//    public Adapter(Context context, List<String> list) {
//        super(context, list);
//    }
//
//    @Override
//    public int getLayoutId() {
//        return 0;
//    }
//
//    @Override
//    public void getItemView(String s, ViewHolder holder, int position, View convertView) {
//    holder.setGone(R.id.activity_main);
//    }
}
