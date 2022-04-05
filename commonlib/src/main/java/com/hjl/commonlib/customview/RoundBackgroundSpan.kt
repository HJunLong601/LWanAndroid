package com.hjl.commonlib.customview

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.style.ReplacementSpan

/**
 * Author : long
 * Description : 给文字添加一个有弧度的背景
 * Date : 2020/6/24
 */
class RoundBackgroundSpan(private val margin : Int = 5,private val radius : Float = 5.0F,private val textColor : Int,private val bgColor : Int,private val isSolid : Boolean = false) : ReplacementSpan() {

    override fun getSize(paint: Paint, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        return (paint.measureText(text,start,end) + margin * 3).toInt()
    }

    override fun draw(canvas: Canvas, text: CharSequence?, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {

        val mPaint = Paint(paint)
        val textWidth = paint.measureText(text,start,end)
        val textHeight =(paint.fontMetrics.descent -  paint.fontMetrics.ascent)
        val rect = RectF()

        rect.top = top.toFloat()
        rect.bottom = bottom.toFloat()
        rect.left = x
        rect.right = rect.left + textWidth + margin*2

        if (isSolid){
            // 实心矩形
            mPaint.color = bgColor
            mPaint.style = Paint.Style.FILL
            canvas.drawRoundRect(rect,radius,radius,mPaint)

            mPaint.color = textColor
            mPaint.style = Paint.Style.FILL

            canvas.drawText(
                text.toString(),
                start,
                end,
                x + ((rect.right - rect.left - textWidth) / 2),
                rect.top + (rect.height() - textHeight)/2 - mPaint.fontMetrics.top,
                mPaint
            )
        }else{
            mPaint.color = bgColor
            mPaint.style = Paint.Style.STROKE
            canvas.drawRoundRect(rect,radius,radius,mPaint)

            mPaint.color = textColor
            mPaint.style = Paint.Style.FILL

            canvas.drawText(
                text.toString(),
                start,
                end,
                x + ((rect.right - rect.left - textWidth) / 2),
                y.toFloat(),
                mPaint
            )
        }

    }
}