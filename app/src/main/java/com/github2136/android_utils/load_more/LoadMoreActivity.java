package com.github2136.android_utils.load_more;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github2136.android_utils.R;
import com.github2136.base.BaseLoadMoreRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class LoadMoreActivity extends BaseListActivity<String> {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_load_more;
    }

    @Override
    protected void initListData(Bundle savedInstanceState) {
        TextView textView = new TextView(this);
        textView.setText("asdfsadf");
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
        textView.setLayoutParams(layoutParams);
        mAdapter.setHeadView(textView);
    }

    @Override
    protected BaseLoadMoreRecyclerAdapter<String> getAdapter() {
        return new LoadMoreAdapter(this, null);
    }

    @Override
    protected void getListData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        List<String> s = new ArrayList<>();
                        s.add("asdf1");
                        s.add("asdf2");
                        s.add("asdf3");
                        s.add("asdf4");
                        s.add("asdf5");
//                        s.add("asdf6");
//                        s.add("asdf7");
//                        s.add("asdf8");
//                        s.add("asdf9");
//                        s.add("asdf10");

//                        if (new Random().nextBoolean()) {
                        getDataSuccessful(s);
//                        } else {
//                            getDataSuccessful(null);
//                        }
                    }
                });
            }
        }).start();
    }

    @Override
    protected void itemClick(String s, int position) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void itemLongClick(String s, int position) {
        Toast.makeText(this, "long" + s, Toast.LENGTH_SHORT).show();
    }
}
