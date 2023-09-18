package com.github2136.android_utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.github2136.util.BitmapUtil
import com.github2136.util.FileUtil
import kotlinx.android.synthetic.main.activity_bitmap.*

/**
 * Created by yb on 2018/10/31.
 */
class BitmapActivity : BaseActivity(), View.OnClickListener {
    private val REQUEST_PIC = 610

    override fun initData(savedInstanceState: Bundle?) {
        btn_choose.setOnClickListener(this)
    }


    override fun getViewResId(): Int {
        return R.layout.activity_bitmap
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_choose -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                startActivityForResult(intent, REQUEST_PIC)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_PIC -> {
                    val path = data?.let {
                        FileUtil.getFileAbsolutePath(mContext, it.data)
                    }
                    path?.let {
                        BitmapUtil.getInstance(it).run {
                            limitPixel(1080)
                            limitSize(1024)
                            addWaterMark(arrayOf("水印1", "000001113"))
                            rotation()
                            getBitmap { bitmap -> iv_image.setImageBitmap(bitmap) }
                        }
                    }
                }
            }
        }
    }
}