package com.github2136.sqlutil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yubin on 2017/7/20.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String columnName() default "";

    enum Type {STRING, BYTE, SHORT, INTEGER, LONG, FLOAT, DOUBLE, BOOLEAN, BYTES, UNKNOW}

    Type columnType() default Type.UNKNOW;

    //主键
    boolean primaryKey() default false;

    //非空
    boolean notNull() default false;

    //唯一
    boolean unique() default false;
}
