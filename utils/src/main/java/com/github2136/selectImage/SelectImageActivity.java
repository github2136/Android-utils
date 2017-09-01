package com.github2136.selectImage;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.*;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.github2136.util.R;

import java.util.ArrayList;
import java.util.List;

public class SelectImageActivity extends AppCompatActivity {
    private RecyclerView rvImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_util_select_image);
        rvImages = (RecyclerView) findViewById(R.id.rv_images);
        rvImages.setAdapter(new SelectImageAdapter(this, getImages()));
    }

    private List<SelectImage> getImages() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor;
        try {
            cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "图片数据获取失败", Toast.LENGTH_SHORT).show();
            return null;
        }
        List<SelectImage> images = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex1 = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DESCRIPTION);
                int columnIndex2 = cursor.getColumnIndex(MediaStore.Images.ImageColumns.PICASA_ID);
                int columnIndex3 = cursor.getColumnIndex(MediaStore.Images.ImageColumns.IS_PRIVATE);
                int columnIndex4 = cursor.getColumnIndex(MediaStore.Images.ImageColumns.LATITUDE);
                int columnIndex5 = cursor.getColumnIndex(MediaStore.Images.ImageColumns.LONGITUDE);
                int columnIndex6 = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN);
                int columnIndex7 = cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION);
                int columnIndex8 = cursor.getColumnIndex(MediaStore.Images.ImageColumns.MINI_THUMB_MAGIC);
                int columnIndex9 = cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_ID);
                int columnIndex10 = cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME);

                int columnIndex11 = cursor.getColumnIndex(MediaStore.MediaColumns._ID);

                int columnIndex12 = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
                int columnIndex13 = cursor.getColumnIndex(MediaStore.MediaColumns.SIZE);
                int columnIndex14 = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
                int columnIndex15 = cursor.getColumnIndex(MediaStore.MediaColumns.TITLE);
                int columnIndex16 = cursor.getColumnIndex(MediaStore.MediaColumns.DATE_ADDED);
                int columnIndex17 = cursor.getColumnIndex(MediaStore.MediaColumns.DATE_MODIFIED);
                int columnIndex18 = cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE);
                int columnIndex19 = cursor.getColumnIndex(MediaStore.MediaColumns.WIDTH);
                int columnIndex20 = cursor.getColumnIndex(MediaStore.MediaColumns.HEIGHT);


                do {
                    SelectImage img = new SelectImage();
                    img.setDescription(cursor.getString(columnIndex1));
                    img.setPicasa_id(cursor.getString(columnIndex2));
                    img.setIs_private(cursor.getInt(columnIndex3));
                    img.setLatitude(cursor.getDouble(columnIndex4));
                    img.setLongitude(cursor.getDouble(columnIndex5));
                    img.setDate_taken(cursor.getInt(columnIndex6));
                    img.setOrientation(cursor.getInt(columnIndex7));
                    img.setMini_thumb_magic(cursor.getInt(columnIndex8));
                    img.setBucket_id(cursor.getString(columnIndex9));
                    img.setBucket_display_name(cursor.getString(columnIndex10));

                    img.set_id(cursor.getLong(columnIndex11));

                    img.setData(cursor.getString(columnIndex12));
                    img.setSize(cursor.getLong(columnIndex13));
                    img.setDisplay_name(cursor.getString(columnIndex14));
                    img.setTitle(cursor.getString(columnIndex15));
                    img.setDate_added(cursor.getLong(columnIndex16));
                    img.setDate_modified(cursor.getLong(columnIndex17));
                    img.setMime_type(cursor.getString(columnIndex18));
                    img.setWidth(cursor.getInt(columnIndex19));
                    img.setHeight(cursor.getInt(columnIndex20));

                    images.add(img);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return images;
    }
}
