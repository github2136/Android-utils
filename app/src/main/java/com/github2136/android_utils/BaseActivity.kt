package com.github2136.android_utils

import android.content.Context
import android.os.Bundle
import android.os.Message
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.lang.ref.WeakReference

/**
 * Created by yb on 2018/10/31.
 */
abstract class BaseActivity : AppCompatActivity() {
    protected val TAG = this.javaClass.name
    protected lateinit var mContext: Context
    protected lateinit var mHandler: Handler
    protected lateinit var mToast: Toast
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setContentView(getViewResId())

        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT)
        mHandler = Handler(this)
        initData(savedInstanceState)
    }

    fun showToast(msg: String) {
        mToast.setText(msg)
        mToast.duration = Toast.LENGTH_SHORT
        mToast.show()
    }

    fun showToastLong(msg: String) {
        mToast.setText(msg)
        mToast.duration = Toast.LENGTH_LONG
        mToast.show()
    }

    class Handler(activity: BaseActivity) : android.os.Handler() {
        private var weakReference: WeakReference<BaseActivity> = WeakReference(activity)

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            val activity: BaseActivity? = weakReference.get()
            activity?.also { it.handleMessage(msg) }
        }
    }

    protected fun handleMessage(msg: Message?) {}
    //布局ID
    protected abstract fun getViewResId(): Int

    //初始化
    protected abstract fun initData(savedInstanceState: Bundle?)

    //取消请求
    fun cancelRequest() {}
}