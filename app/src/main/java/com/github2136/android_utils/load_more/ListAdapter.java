package com.github2136.android_utils.load_more;

import android.content.Context;

import com.github2136.android_utils.R;
import com.github2136.base.BaseRecyclerAdapter;
import com.github2136.base.ViewHolderRecyclerView;

import java.util.List;

/**
 * Created by yubin on 2017/9/21.
 */

public class ListAdapter extends BaseRecyclerAdapter<String> {
    public ListAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_item;
    }

    @Override
    protected void onBindView(String s, ViewHolderRecyclerView holder, int position) {
        holder.setText(R.id.tv_text, s);
    }
}

