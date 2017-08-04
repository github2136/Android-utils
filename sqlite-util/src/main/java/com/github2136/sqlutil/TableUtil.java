package com.github2136.sqlutil;

import java.lang.reflect.Field;


/**
 * Created by yubin on 2017/7/20.
 */

public class TableUtil {
    static final String DATA_TYPE_TEXT = " TEXT";
    static final String DATA_TYPE_INTEGER = " INTEGER";
    static final String DATA_TYPE_BLOB = " BLOB";
    static final String DATA_TYPE_REAL = " REAL";

    static final String PRIMARY_KEY = " PRIMARY KEY";
    static final String NOT_NULL = " NOT UNKNOW";
    static final String UNIQUE = " UNIQUE";
    static final String COMMA_SEP = ",";

    public static String getCreateSQL(Class<?> clazz) {
        StringBuilder sql = new StringBuilder();
        String tableName;
        Table table = null;
        if (clazz.isAnnotationPresent(Table.class)) {
            table = clazz.getAnnotation(Table.class);
        }
        if (table == null) {
            throw new RuntimeException("No Table annotations in class " + clazz.getName());
        }
        if (table.tableName().equals("")) {
            tableName = clazz.getSimpleName();
        } else {
            tableName = table.tableName();
        }
        sql.append("CREATE TABLE ");
        sql.append(tableName);
        sql.append("(");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                String columnName = column.columnName();
                if (columnName.equals("")) {
                    columnName = field.getName();
                }
                Column.Type columnType = column.columnType();
                boolean isPrimaryKey = column.primaryKey();
                boolean isNotNull = column.notNull();
                boolean isUnique = column.unique();
                sql.append(columnName);
                sql.append(getType(columnType, field));
                if (isPrimaryKey) {
                    sql.append(PRIMARY_KEY);
                }
                if (isNotNull) {
                    sql.append(NOT_NULL);
                }
                if (isUnique) {
                    sql.append(UNIQUE);
                }
                sql.append(COMMA_SEP);
            }
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        return sql.toString();
    }

    private static String getType(Column.Type type, Field field) {
        switch (type) {
            case STRING:
                return DATA_TYPE_TEXT;
            case BYTE:
            case BOOLEAN:
            case SHORT:
            case INTEGER:
            case LONG:
                return DATA_TYPE_INTEGER;
            case BYTES:
                return DATA_TYPE_BLOB;
            case FLOAT:
            case DOUBLE:
                return DATA_TYPE_REAL;
            default:
                switch (field.getGenericType().toString()) {
                    case "class java.lang.String":
                        return DATA_TYPE_TEXT;
                    case "class [B":
                    case "class [Ljava.lang.Byte;":
                        return DATA_TYPE_BLOB;
                    case "byte":
                    case "boolean":
                    case "short":
                    case "int":
                    case "long":
                    case "class java.lang.Byte":
                    case "class java.lang.Boolean":
                    case "class java.lang.Short":
                    case "class java.lang.Integer":
                    case "class java.lang.Long":
                        return DATA_TYPE_INTEGER;
                    case "float":
                    case "double":
                    case "class java.lang.Float":
                    case "class java.lang.Double":
                        return DATA_TYPE_REAL;
                    default:
                        return DATA_TYPE_TEXT;
                }
        }
    }
}
