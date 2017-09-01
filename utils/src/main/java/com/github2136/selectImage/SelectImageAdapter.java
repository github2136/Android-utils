package com.github2136.selectImage;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github2136.base.BaseRecyclerAdapter;
import com.github2136.base.ViewHolderRecyclerView;
import com.github2136.util.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yubin on 2017/8/26.
 */

public class SelectImageAdapter extends BaseRecyclerAdapter<SelectImage> {
    List<String> selectPosition;
    private int limit;
    private RelativeLayout.LayoutParams layoutParams;

    public SelectImageAdapter(Context context, List<SelectImage> list) {
        super(context, list);
        limit = context.getResources().getDisplayMetrics().widthPixels / 3;
        layoutParams = new RelativeLayout.LayoutParams(limit, limit);
        selectPosition = new ArrayList<>();
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_util_select_image;
    }

    @Override
    protected void onBindView(final SelectImage selectImage, ViewHolderRecyclerView holder, final int position) {
        ImageView ivImage = holder.getView(R.id.iv_image);
        ImageButton ibCheck = holder.getView(R.id.ib_check);
        ivImage.setLayoutParams(layoutParams);
        Glide.with(mContext)
                .load(selectImage.getData())
                .override(limit, limit)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.drawable.img_util_select_wiat)
                .error(R.drawable.img_util_select_fail)
                .into(ivImage);
        if (selectPosition.contains(position + "")) {
            ibCheck.setImageResource(R.drawable.ic_util_select_checkbox_check);
        } else {
            ibCheck.setImageResource(R.drawable.ic_util_select_checkbox_uncheck);
        }
        ibCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectPosition.contains(position + "")) {
                    selectPosition.remove(position + "");
                } else {
                    selectPosition.add(position + "");
                }
                notifyItemChanged(position);
            }
        });
    }
}
