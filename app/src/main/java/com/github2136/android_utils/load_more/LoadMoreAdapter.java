package com.github2136.android_utils.load_more;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github2136.android_utils.R;
import com.github2136.base.BaseLoadMoreRecyclerAdapter;
import com.github2136.base.ViewHolderRecyclerView;

import java.util.List;

/**
 * Created by yubin on 2017/8/22.
 */

public class LoadMoreAdapter extends BaseLoadMoreRecyclerAdapter<String> {
    public LoadMoreAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    protected int getPageSize() {
        return 5;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_item;
    }

    @Override
    protected void onBindView(String s, ViewHolderRecyclerView holder, int position) {
        holder.setText(R.id.tv_text, s);
       TextView tv= holder.getView(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "ssss", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
