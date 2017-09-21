package com.github2136.android_utils.load_more;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.github2136.android_utils.R;
import com.github2136.base.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        RecyclerView rvContent = (RecyclerView) findViewById(R.id.rv_content);
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
        ListAdapter adapter = new ListAdapter(this, s);
        adapter.setOnItemClickListener(rvContent, new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerAdapter adapter, int position) {
                Toast.makeText(ListActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnItemLongClickListener(rvContent, new BaseRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemClick(BaseRecyclerAdapter adapter, int position) {
                Toast.makeText(ListActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        rvContent.setAdapter(adapter);

    }
}
