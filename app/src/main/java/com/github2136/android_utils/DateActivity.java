package com.github2136.android_utils;

import android.os.Bundle;
import android.widget.TextView;

import com.github2136.util.DateUtil;

import java.util.Date;

public class DateActivity extends BaseActivity {
    TextView tvContent;

    @Override
    protected int getViewResId() {
        return R.layout.activity_date;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvContent.append("当前时间：" + DateUtil.getDateNow());
        tvContent.append("\n");
        tvContent.append("当前时间（指定格式yyyy MM dd HH mm ss）：" + DateUtil.getDateNow("yyyy MM dd HH mm ss"));
        tvContent.append("\n");
        Date d = DateUtil.str2date("2018-05-17 15:32:45");//String转Date
        tvContent.append("Date转String（指定格式HH mm ss yyyy MM dd）：" + DateUtil.date2str(d, "HH mm ss yyyy MM dd"));
        tvContent.append("\n");
        tvContent.append("指定时间与当前时间的差距：" + DateUtil.getRelativeTimeString(d));
    }
}
