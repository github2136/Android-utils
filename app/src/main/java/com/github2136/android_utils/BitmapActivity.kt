package com.github2136.android_utils

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import com.github2136.util.BitmapUtil
import com.github2136.util.FileUtil
import kotlinx.android.synthetic.main.activity_bitmap.*
import kotlin.math.max

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
                            // addWaterMark(arrayOf("水印1", "000001113"))
                            addWaterMark { bitmap ->
                                val txt1 = "水印"
                                val txt2 = "水印2222"
                                val canvas = Canvas(bitmap)
                                val paintTxt = Paint(Paint.ANTI_ALIAS_FLAG)
                                paintTxt.style = Paint.Style.FILL
                                paintTxt.color = Color.WHITE
                                paintTxt.setShadowLayer(3f, 0f, 0f, Color.BLACK)
                                paintTxt.textSize = 40f
                                paintTxt.strokeWidth = 5f

                                val txtRect1 = Rect()
                                paintTxt.getTextBounds(txt1, 0, txt1.lastIndex, txtRect1)
                                val txtRect2 = Rect()
                                paintTxt.getTextBounds(txt2, 0, txt2.lastIndex, txtRect2)

                                val paddingBg = 100
                                val paddingTxt = 100
                                val paintBg = Paint(Paint.ANTI_ALIAS_FLAG)
                                paintBg.style = Paint.Style.FILL
                                paintBg.color = Color.parseColor("#33FFFFFF")

                                val rect = Rect(
                                    paddingBg,
                                    canvas.height - paddingBg - txtRect1.height() * 2 - paddingTxt * 2,
                                    paddingBg * 2 + max(txtRect1.width(), txtRect2.width()) + paddingTxt * 2,
                                    canvas.height - paddingBg
                                )
                                canvas.drawRect(rect, paintBg)

                                canvas.drawText(txt1, (paddingBg + paddingTxt).toFloat(), (canvas.height - paddingBg - rect.height() + paddingTxt).toFloat(), paintTxt)
                                canvas.drawText(txt2, (paddingBg + paddingTxt).toFloat(), (canvas.height - paddingBg - rect.height() + paddingTxt + txtRect1.height()).toFloat(), paintTxt)
                            }
                            rotation()
                            getBitmap { bitmap -> iv_image.setImageBitmap(bitmap) }
                        }
                    }
                }
            }
        }
    }
}