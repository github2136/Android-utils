package com.github2136.android_utils;

import com.github2136.sqlutil.Column;
import com.github2136.sqlutil.Table;
import com.github2136.sqlutil.TestAnn;

/**
 * Created by yubin on 2017/7/25.
 */
@Table
public class Test {
    @Column()
    private String teststr1;
    @Column()
    private String teststr2;
    @Column()
    private String teststr3;
    private String teststr4;
}
