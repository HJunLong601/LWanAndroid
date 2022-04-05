package com.hjl.commonlib.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 功能:recycleView分割线
 */

public class RecycleViewVerticalDivider extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private Drawable mDivider;
    private int mDividerHeight = 2;//分割线高度
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};


    private int mLeftSpace = 0;
    private int mRightSpace = 0;


    private boolean mIsShowEnd = true;
    /**
     * 默认分割线：高度为2px，颜色为灰色
     *
     * @param context
     */
    public RecycleViewVerticalDivider(Context context) {

        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param drawableId  分割线图片
     */
    public RecycleViewVerticalDivider(Context context, int drawableId) {
        this(context);
        mDivider = ContextCompat.getDrawable(context, drawableId);
        mDividerHeight = mDivider.getIntrinsicHeight();
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    public RecycleViewVerticalDivider(Context context, int dividerHeight, int dividerColor) {
        this(context);
        mDividerHeight = dividerHeight;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    public RecycleViewVerticalDivider(Context context, int dividerHeight, int dividerColor, int leftSpace, int rightSpace) {
        this(context,dividerHeight,dividerColor);
        mLeftSpace = leftSpace;
        mRightSpace = rightSpace;
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    public RecycleViewVerticalDivider(Context context, int dividerHeight, int dividerColor, int leftSpace, int rightSpace, boolean isShowEnd) {
        this(context,dividerHeight,dividerColor);
        mLeftSpace = leftSpace;
        mRightSpace = rightSpace;
        mIsShowEnd = isShowEnd;
    }

    //获取分割线尺寸
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if(isLastRaw( ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition(),parent.getAdapter().getItemCount()))
        {
            outRect.set(0, 0, 0, 0);
        }else {
            outRect.set(0, 0, 0, mDividerHeight);
        }
    }

    //绘制分割线
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        drawHorizontal(c, parent);

    }

    //绘制横向 item 分割线
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft() + mLeftSpace;
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight() - mRightSpace;
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {

            if(mIsShowEnd && (i + 1 == childSize) )
            {
                return;
            }

            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    private boolean isLastRaw(int pos,int childCount)
    {
        // 如果是最后一行，则不需要绘制底部
        return (pos+1) >= childCount;
    }

}