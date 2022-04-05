package com.hjl.commonlib.customview

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.view.View
import com.hjl.commonlib.customview.RoundBackgroundSpan

/**
 * Author : long
 * Description :
 * Date : 2020/6/24
 */
object SpannableStringUtils{

    fun addStartRoundBackgroundSpanString(spannableStringBuilder : SpannableStringBuilder = SpannableStringBuilder(),
                                          text : String,
                                          textColor : Int,
                                          bgColor : Int,
                                          isSolid : Boolean = false
    ) : SpannableStringBuilder{
        spannableStringBuilder.insert(0,text)

        spannableStringBuilder.setSpan(
                RoundBackgroundSpan(textColor = textColor,bgColor = bgColor,isSolid = isSolid),
                0,
                text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableStringBuilder
    }

    fun appendClickableSpan(
        spannableStringBuilder : SpannableStringBuilder = SpannableStringBuilder(),
        text : String,
        action : (view : View)-> Unit
    ) : SpannableStringBuilder{
        val start = spannableStringBuilder.length
        spannableStringBuilder.append(text)
        spannableStringBuilder.setSpan(
            object : ClickableSpan(){
                override fun onClick(widget: View) {
                    action.invoke(widget)
                }
            },
            start,
            start + text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spannableStringBuilder
    }

}