package com.github2136.android_utils;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.github2136.sqlutil.BaseDao;

/**
 * Created by yubin on 2017/7/24.
 */

public class MyTableDao extends BaseDao<Class<MyTable>, MyTable> {


    public MyTableDao(Context context) {
        super(context);
    }

    @Override
    public SQLiteOpenHelper getSQLHelper(Context context) {
        return new DBHelper(context);
    }

    @Override
    public Class<MyTable> getDataClass() {
        return MyTable.class;
    }
}
