package com.github2136.android_utils

import android.os.Bundle
import com.github2136.util.DateUtil
import kotlinx.android.synthetic.main.activity_date.*

/**
 * Created by yb on 2018/10/31.
 */
class DateActivity : BaseActivity() {
    override fun initData(savedInstanceState: Bundle?) {
        tv_content.append("当前时间：" + DateUtil.getDateNow())
        tv_content.append("\n")
        tv_content.append("当前时间（指定格式yyyy MM dd HH mm ss）：" + DateUtil.getDateNow("yyyy MM dd HH mm ss"))
        tv_content.append("\n")
        val d = DateUtil.str2date("2018-05-17 15:32:45")//String转Date
        tv_content.append("Date转String（指定格式HH mm ss yyyy MM dd）：" + DateUtil.date2str(d!!, "HH mm ss yyyy MM dd"))
        tv_content.append("\n")
        tv_content.append("指定时间与当前时间的差距：" + DateUtil.getRelativeTimeString(d))
    }

    override fun getViewResId(): Int {
        return R.layout.activity_date;
    }
}