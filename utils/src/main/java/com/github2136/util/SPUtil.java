package com.github2136.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.SharedPreferencesCompat;

import java.util.Set;

/**
 * SharedPreferences<br>
 * 默认SP的文件名为android-util<br>
 * 如果需要更换可先在application中添加名为util_sp_name的&lt;meta-data/&#62;
 */
public class SPUtil {

    private volatile static SPUtil ourInstance;

    public static SPUtil getInstance(Context context) {
        if (ourInstance == null) {
            synchronized (SPUtil.class) {
                if (ourInstance == null) {
                    ourInstance = new SPUtil(context);
                }
            }
        }
        return ourInstance;
    }

    public static SPUtil getInstance(Context context, String name) {
        if (ourInstance == null) {
            synchronized (SPUtil.class) {
                if (ourInstance == null) {
                    ourInstance = new SPUtil(context, name);
                }
            }
        }
        return ourInstance;
    }

    private SharedPreferences sp;
    private Edit edit;

    protected SPUtil(Context context, String name) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    protected SPUtil(Context context) {
        String name = "android-util";
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle metaData = appInfo.metaData;
            if (metaData != null) {
                name = metaData.getString("util_sp_name", name);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            name = "android-util";
        }
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public SharedPreferences get() {
        return sp;
    }

    public class Edit {
        private SharedPreferences.Editor editor;

        public Edit() {
            editor = sp.edit();
        }

        public Edit putValue(String key, String value) {

            editor.putString(key, value);
            return this;
        }

        public Edit putValue(String key, boolean value) {
            editor.putBoolean(key, value);
            return this;
        }

        public Edit putValue(String key, float value) {
            editor.putFloat(key, value);
            return this;
        }

        public Edit putValue(String key, int value) {
            editor.putInt(key, value);
            return this;
        }

        public Edit putValue(String key, long value) {
            editor.putLong(key, value);
            return this;
        }

        public Edit putValue(String key, Set<String> value) {
            editor.putStringSet(key, value);
            return this;
        }

        //保存数据，建议使用apply
        public void apply() {
            SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
        }
    }

    public Edit edit() {
        if (edit == null) {
            edit = new Edit();
        }
        return edit;
    }


    //查询某值是否存在
    public boolean contains(String key) {
        return sp.contains(key);
    }

    //删除某值
    public void remove(String key) {
        SharedPreferencesCompat.EditorCompat.getInstance().apply(sp.edit().remove(key));
    }

    //清理所有
    public void clear() {
        SharedPreferencesCompat.EditorCompat.getInstance().apply(sp.edit().clear());
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defVal) {
        return sp.getString(key, defVal);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defVal) {
        return sp.getBoolean(key, defVal);
    }

    public float getFloat(String key) {
        return getFloat(key, 0);
    }

    public float getFloat(String key, float defVal) {
        return sp.getFloat(key, defVal);
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defVal) {
        return sp.getInt(key, defVal);
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public long getLong(String key, long defVal) {
        return sp.getLong(key, defVal);
    }

    public Set<String> getStringSet(String key) {
        return getStringSet(key, null);
    }

    public Set<String> getStringSet(String key, Set<String> defVal) {
        return sp.getStringSet(key, defVal);
    }
}