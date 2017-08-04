package com.github2136.sqlutil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yubin on 2017/7/24.
 */

public abstract class BaseDao<T extends Class<?>, D> {
    protected SQLiteOpenHelper mSQLHelper;
    private T t;

    public BaseDao(Context context) {
        mSQLHelper = getSQLHelper(context);
        t = getDataClass();
    }

    public abstract SQLiteOpenHelper getSQLHelper(Context context);

    public abstract T getDataClass();

    /**
     * 插入数据
     *
     * @param d
     * @return
     */
    public boolean insert(D d) {
        SQLiteDatabase dbWrite = mSQLHelper.getWritableDatabase();
        String tableName;
        Table table = null;
        if (t.isAnnotationPresent(Table.class)) {
            table = t.getAnnotation(Table.class);
        }
        if (table == null) {
            throw new RuntimeException("No Table annotations in class " + t.getName());
        }
        if (table.tableName().equals("")) {
            tableName = t.getSimpleName();
        } else {
            tableName = table.tableName();
        }
        Field[] fields = t.getDeclaredFields();
        ContentValues cv = getContentValues(d, fields);
        long result = dbWrite.insert(tableName, null, cv);
        dbWrite.close();
        if (cv != null) {
            return result > 0;
        } else {
            return false;
        }
    }

    /**
     * 插入数据
     *
     * @param d
     * @return
     */
    public boolean insert(List<D> d) {
        SQLiteDatabase dbWrite = mSQLHelper.getWritableDatabase();
        dbWrite.beginTransaction();
        String tableName;
        Table table = null;
        if (t.isAnnotationPresent(Table.class)) {
            table = t.getAnnotation(Table.class);
        }
        if (table == null) {
            throw new RuntimeException("No Table annotations in class " + t.getName());
        }
        if (table.tableName().equals("")) {
            tableName = t.getSimpleName();
        } else {
            tableName = table.tableName();
        }
        Field[] fields = t.getDeclaredFields();
        int result = 0;
        for (D d1 : d) {
            ContentValues cv = getContentValues(d1, fields);
            if (cv != null && dbWrite.insert(tableName, null, cv) > 0) {
                result++;
            }
        }
        if (result == d.size()) {
            dbWrite.setTransactionSuccessful();
        } else {
            result = 0;
        }
        dbWrite.endTransaction();
        dbWrite.close();
        return result > 0;
    }

    /**
     * 查询所有数据
     *
     * @return
     */
    public List<D> query() {
        SQLiteDatabase dbRead = mSQLHelper.getReadableDatabase();

        String tableName;
        Table table = null;
        if (t.isAnnotationPresent(Table.class)) {
            table = t.getAnnotation(Table.class);
        }
        if (table == null) {
            throw new RuntimeException("No Table annotations in class " + t.getName());
        }
        if (table.tableName().equals("")) {
            tableName = t.getSimpleName();
        } else {
            tableName = table.tableName();
        }
        Field[] fields = t.getDeclaredFields();
        Cursor cursor = dbRead.query(tableName, getColumns(fields), null, null, null, null, null);
        List<D> dArrayList = new ArrayList<>();
        try {
            if (cursor != null && cursor.moveToFirst()) {
                Map<String, Integer> columnIndex = getColumnIndex(fields, cursor);
                do {
                    dArrayList.add(getData(columnIndex, cursor));
                } while (cursor.moveToNext());
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
            return dArrayList;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return dArrayList;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return dArrayList;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dbRead.close();
        }
        return dArrayList;
    }

    private ContentValues getContentValues(D d, Field[] fields) {
        ContentValues cv = new ContentValues();
        try {
            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    String columnName = column.columnName();
                    if (columnName.equals("")) {
                        columnName = field.getName();
                    }
                    Column.Type columnType = column.columnType();
                    field.setAccessible(true);
                    Object value = field.get(d);
                    if (value != null) {
                        switch (columnType) {
                            case STRING: {
                                String str = String.valueOf(value);
                                cv.put(columnName, str);
                            }
                            break;
                            case INTEGER: {
                                int intVal = Integer.parseInt(String.valueOf(value));
                                cv.put(columnName, intVal);
                            }
                            break;
                            case BOOLEAN: {
                                boolean bolVal = Boolean.parseBoolean(String.valueOf(value));
                                cv.put(columnName, bolVal);
                            }
                            break;
                            case SHORT: {
                                short shortVal = Short.parseShort(String.valueOf(value));
                                cv.put(columnName, shortVal);
                            }
                            break;
                            case LONG: {
                                long longVal = Long.parseLong(String.valueOf(value));
                                cv.put(columnName, longVal);
                            }
                            break;
                            case BYTE: {
                                byte byteVal = Byte.parseByte(String.valueOf(value));
                                cv.put(columnName, byteVal);
                            }
                            break;
                            case BYTES: {
                                byte[] bytesVal = (byte[]) value;
                                cv.put(columnName, bytesVal);
                            }
                            break;
                            case FLOAT: {
                                float floatVal = Float.parseFloat(String.valueOf(value));
                                cv.put(columnName, floatVal);
                            }
                            break;
                            case DOUBLE: {
                                double doubleVal = Double.parseDouble(String.valueOf(value));
                                cv.put(columnName, doubleVal);
                            }
                            break;
                            default:
                                switch (field.getGenericType().toString()) {
                                    case "class java.lang.String":
                                    default:
                                        String str = String.valueOf(value);
                                        cv.put(columnName, str);
                                        break;
                                    case "byte":
                                    case "class java.lang.Byte":
                                        byte byteVal = Byte.parseByte(String.valueOf(value));
                                        cv.put(columnName, byteVal);
                                        break;
                                    case "class [B": {
                                        byte[] bytesVal = (byte[]) value;
                                        cv.put(columnName, bytesVal);
                                    }
                                    break;
                                    case "class [Ljava.lang.Byte;": {
                                        byte[] bytesVal = toPrimitive((Byte[]) value);
                                        cv.put(columnName, bytesVal);
                                    }
                                    break;
                                    case "short":
                                    case "class java.lang.Short":
                                        short shortVal = Short.parseShort(String.valueOf(value));
                                        cv.put(columnName, shortVal);
                                        break;
                                    case "int":
                                    case "class java.lang.Integer":
                                        int intVal = Integer.parseInt(String.valueOf(value));
                                        cv.put(columnName, intVal);
                                        break;
                                    case "long":
                                    case "class java.lang.Long":
                                        long longVal = Long.parseLong(String.valueOf(value));
                                        cv.put(columnName, longVal);
                                        break;
                                    case "boolean":
                                    case "class java.lang.Boolean":
                                        boolean bolVal = Boolean.parseBoolean(String.valueOf(value));
                                        cv.put(columnName, bolVal);
                                        break;
                                    case "float":
                                    case "class java.lang.Float":
                                        float floatVal = Float.parseFloat(String.valueOf(value));
                                        cv.put(columnName, floatVal);
                                        break;
                                    case "double":
                                    case "class java.lang.Double":
                                        double doubleVal = Double.parseDouble(String.valueOf(value));
                                        cv.put(columnName, doubleVal);
                                        break;
                                }
                        }
                    }
                }
            }
            return cv;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String[] getColumns(Field[] fields) {
        List<String> columns = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                String columnName = column.columnName();
                if (columnName.equals("")) {
                    columnName = field.getName();
                }
                columns.add(columnName);
            }
        }
        String[] temp = new String[columns.size()];
        columns.toArray(temp);
        return temp;
    }

    /**
     * 获得数据表列index
     *
     * @param fields
     * @param cursor
     * @return
     */
    private Map<String, Integer> getColumnIndex(Field[] fields, Cursor cursor) {
        Map<String, Integer> columnIndex = new HashMap<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                String columnName = column.columnName();
                if (columnName.equals("")) {
                    columnName = field.getName();
                }
                columnIndex.put(columnName, cursor.getColumnIndex(columnName));
            }
        }
        return columnIndex;
    }

    private D getData(Map<String, Integer> columnIndex, Cursor cursor)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        D d = (D) Class.forName(t.getName()).newInstance();
        Class dClass = d.getClass();
        Field[] fields = dClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                String columnName = column.columnName();
                if (columnName.equals("")) {
                    columnName = field.getName();
                }
                Column.Type columnType = column.columnType();
                field.setAccessible(true);

                switch (columnType) {
                    case STRING: {
                        field.set(d, cursor.getString(columnIndex.get(columnName)));
                    }
                    break;
                    case BYTE: {
                        byte val = (byte) cursor.getInt(columnIndex.get(columnName));
                        field.set(d, val);
                    }
                    break;
                    case BOOLEAN: {
                        boolean val = cursor.getInt(columnIndex.get(columnName)) == 1;
                        field.set(d, val);
                    }
                    break;
                    case INTEGER: {
                        field.set(d, cursor.getInt(columnIndex.get(columnName)));
                    }
                    break;
                    case SHORT: {
                        field.set(d, cursor.getShort(columnIndex.get(columnName)));
                    }
                    break;
                    case LONG: {
                        field.set(d, cursor.getLong(columnIndex.get(columnName)));
                    }
                    break;
                    case BYTES: {
                        field.set(d, cursor.getBlob(columnIndex.get(columnName)));
                    }
                    break;
                    case FLOAT: {
                        field.set(d, cursor.getFloat(columnIndex.get(columnName)));
                    }
                    break;
                    case DOUBLE: {
                        field.set(d, cursor.getDouble(columnIndex.get(columnName)));
                    }
                    break;
                    default:
                        switch (field.getGenericType().toString()) {
                            case "class java.lang.String":
                            default:
                                field.set(d, cursor.getString(columnIndex.get(columnName)));
                                break;
                            case "byte":
                            case "class java.lang.Byte": {
                                byte val = (byte) cursor.getInt(columnIndex.get(columnName));
                                field.set(d, val);
                            }
                            break;
                            case "int":
                            case "class java.lang.Integer":
                                field.set(d, cursor.getInt(columnIndex.get(columnName)));
                                break;
                            case "boolean":
                            case "class java.lang.Boolean": {
                                boolean val = cursor.getInt(columnIndex.get(columnName)) == 1;
                                field.set(d, val);
                            }
                            break;
                            case "class [B":
                                field.set(d, cursor.getBlob(columnIndex.get(columnName)));
                                break;
                            case "class [Ljava.lang.Byte;":
                                byte[] value = cursor.getBlob(columnIndex.get(columnName));
                                Byte[] obj = toObject(value);
                                field.set(d, obj);
                                break;
                            case "short":
                            case "class java.lang.Short":
                                field.set(d, cursor.getShort(columnIndex.get(columnName)));
                                break;
                            case "long":
                            case "class java.lang.Long":
                                field.set(d, cursor.getLong(columnIndex.get(columnName)));
                                break;
                            case "float":
                            case "class java.lang.Float":
                                field.set(d, cursor.getFloat(columnIndex.get(columnName)));
                                break;
                            case "double":
                            case "class java.lang.Double":
                                field.set(d, cursor.getDouble(columnIndex.get(columnName)));
                                break;
                        }
                }
            }
        }
        return d;
    }

    private Byte[] toObject(byte[] array) {
        if (array == null)
            return null;
        if (array.length == 0) {
            return new Byte[0];
        }
        Byte[] result = new Byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = Byte.valueOf(array[i]);
        }
        return result;
    }

    private byte[] toPrimitive(Byte[] array) {
        if (array == null)
            return null;
        if (array.length == 0) {
            return new byte[0];
        }
        byte[] result = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].byteValue();
        }
        return result;
    }
}