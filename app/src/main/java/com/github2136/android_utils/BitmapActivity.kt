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
import com.github2136.util.str
import kotlinx.android.synthetic.main.activity_bitmap.btn_choose
import kotlinx.android.synthetic.main.activity_bitmap.iv_image
import java.util.Date
import kotlin.math.abs
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
                    BitmapUtil.getInstance(this, data!!.data!!).run {
                        limitPixel(1080)
                        limitSize(1024)
                        // addWaterMark(arrayOf("水印1", "000001113"))
                        addWaterMark { bitmap ->
                            val txt = mutableListOf("水印1111", "水印2", "时间：${Date().str()}")
                            val canvas = Canvas(bitmap)
                            val paintTxt = Paint(Paint.ANTI_ALIAS_FLAG)
                            paintTxt.style = Paint.Style.FILL
                            paintTxt.color = Color.WHITE
                            paintTxt.setShadowLayer(3f, 0f, 0f, Color.BLACK)
                            paintTxt.textSize = 40f

                            val txtRects = mutableListOf<Rect>()
                            var maxWidth = 0
                            txt.forEach {
                                val txtRect = Rect()
                                paintTxt.getTextBounds(it, 0, it.length, txtRect)
                                maxWidth = max(maxWidth, txtRect.width())
                                txtRects.add(txtRect)
                            }

                            val paddingBg = 100
                            val paddingTxt = 20
                            val txtSpace = 20
                            val paintBg = Paint(Paint.ANTI_ALIAS_FLAG)
                            paintBg.style = Paint.Style.FILL
                            paintBg.color = Color.parseColor("#99FFFFFF")

                            val rectTxt = Rect(
                                paddingBg,
                                canvas.height - paddingBg - txtRects[0].height() * txtRects.size - paddingTxt * 2 - txtSpace * txtRects.size,
                                paddingBg + maxWidth + paddingTxt * 2,
                                canvas.height - paddingBg
                            )
                            canvas.drawRect(rectTxt, paintBg)
                            txt.forEachIndexed { index, s ->
                                canvas.drawText(
                                    s,
                                    (paddingBg + paddingTxt).toFloat(),
                                    (rectTxt.top + paddingTxt + txtRects[0].height() + index * (txtRects[0].height() + txtSpace)).toFloat(),
                                    paintTxt
                                )
                            }
                        }
                        rotation()
                        getBitmap { bitmap -> iv_image.setImageBitmap(bitmap) }
                    }
                }
            }
        }
    }
}