package com.github2136.android_utils.load_more;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.github2136.android_utils.R;
import com.github2136.base.BaseLoadMoreRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoadMoreActivity extends BaseListActivity<String> {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_load_more;
    }

    @Override
    protected void initListData(Bundle savedInstanceState) {

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
                    Thread.sleep(1000);
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
                        s.add("asdf6");
                        s.add("asdf7");
                        s.add("asdf8");
                        s.add("asdf9");
                        s.add("asdf10");

                        if (new Random().nextBoolean()) {
                            getDataSuccessful(s);
                        } else {
                            getDataSuccessful(null);
                        }
                    }
                });
            }
        }).start();
    }
}
