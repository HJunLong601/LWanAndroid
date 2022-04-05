package com.hjl.commonlib.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;


import com.hjl.commonlib.R;
import com.hjl.commonlib.utils.DensityUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.Nullable;

/**
 * author: long
 * description please add a description here
 * Date: 2021/7/22
 */
public class ThumbnailView extends View {

    private final String TAG = "ThumbnailView";

    /**
     * 默认宽高 wrap_content 时有效
     */
    private int DEFAULT_WIDTH = 1000;
    private int DEFAULT_HEIGHT = DensityUtil.dp2px(60);

    private int DRAG_BORDER_WIDTH = DensityUtil.dp2px(2); // 拖动的边框宽度
    private int THUMBNAIL_HEIGHT = DensityUtil.dp2px(60);

    private int DEFAULT_BACKGROUND_COLOR;

    /**
     * 宽、高
     */
    private int mWidth;
    private int mHeight;
    private int marginTB;
    private int mThumbnailWidth;


    /**
     * 滑动相关
     */

    private Scroller mScroller;
    private boolean mIsShowIndicator;//

    //提供手指速度计算;
    private VelocityTracker velocityTracker;
    private int minimumVelocity;
    private int maximumVelocity;

    /**
     * 文字
     */

    /**
     * 画笔
     */

    private Paint mBitmapPaint;
    private Paint mIndicatorPaint;
    private Paint mPendingPaint;
    private Paint mDragBorderPaint;

    private Rect mCurrentBitmapRect;
    private Rect mStartHandleRect;
    private Rect mEndHandleRect;

    /**
     * 数据
     */

    private List<Object> thumbnailPathList = new ArrayList<>(1);


    private OnProcessChangeListener onProcessChangeListener;
    private OnCutoutChangeListener onCutoutChangeListener;

    private int mDrawStartX; // 当前绘制 缩略图的X

    private int mStartHandleOffsetX; // 起始把手滑动的距离X 始终为正
    private int mEndHandleOffsetX; // 结束把手滑动的距离X 始终为负数

    private Bitmap mDragHandleBitmap;

    /**
     * 时间
     */
    private long currentTime = 0L;
    private float progress;
    public boolean isDragging;

    // 获取Bitmap 根据需要自己实现 提供默认
    public IBitmapRender iBitmapRender;

    public ThumbnailView(Context context) {
        super(context);
        init();
    }

    public ThumbnailView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ThumbnailView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        DEFAULT_BACKGROUND_COLOR = getContext().getResources().getColor(R.color.common_gray);

        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);

        mIndicatorPaint = new Paint();
        mIndicatorPaint.setAntiAlias(true);
        mIndicatorPaint.setStrokeWidth(DensityUtil.dp2px(1));
        mIndicatorPaint.setStyle(Paint.Style.FILL);
        mIndicatorPaint.setColor(Color.WHITE);

        mDragBorderPaint = new Paint();
        mDragBorderPaint.setAntiAlias(true);
        mDragBorderPaint.setStrokeWidth(DRAG_BORDER_WIDTH);
        mDragBorderPaint.setStyle(Paint.Style.FILL);
        mDragBorderPaint.setColor(Color.WHITE);


        mPendingPaint = new Paint();
        mPendingPaint.setAntiAlias(true);
        mPendingPaint.setColor(DEFAULT_BACKGROUND_COLOR);

        mCurrentBitmapRect = new Rect();
        mStartHandleRect = new Rect();
        mEndHandleRect = new Rect();
        mDragHandleBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.common_video_border_tag);

        measureThumbnailWidth();
//        computeIndicatorData();


        // 滑动相关

        mScroller = new Scroller(getContext(),new LinearInterpolator());

        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        minimumVelocity = configuration.getScaledMinimumFlingVelocity();
        maximumVelocity = configuration.getScaledMaximumFlingVelocity();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(DEFAULT_WIDTH,DEFAULT_HEIGHT);
        }else if (widthMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(DEFAULT_WIDTH,heightSize);
        }else if (heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSize,DEFAULT_HEIGHT);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        marginTB = (mHeight - THUMBNAIL_HEIGHT) / 2;

        Log.d(TAG, "onSizeChanged: " + Arrays.asList(mWidth,mHeight, marginTB));
    }

    /**
     * 根据当前的高度 等比算出缩略图的宽度
     */
    public void measureThumbnailWidth(){
        if (thumbnailPathList.size() == 0 || iBitmapRender == null) return;

        Bitmap bitmap = iBitmapRender.renderBitmap(0,thumbnailPathList.get(0));
        if (bitmap == null) return;
        mThumbnailWidth = (int) (Float.parseFloat(String.valueOf(bitmap.getWidth()))/ bitmap.getHeight() * THUMBNAIL_HEIGHT);
        Log.d(TAG, "measureThumbnailWidth: " + mThumbnailWidth);

    }


    /**
     * 绘制相关
     */

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mThumbnailWidth == 0) measureThumbnailWidth();

        canvas.drawColor(DEFAULT_BACKGROUND_COLOR);

        drawAllThumbnail(canvas);

        drawCutoutBorder(canvas);


        canvas.drawLine(getCenterLineX(),0,getCenterLineX(),mHeight,mIndicatorPaint);
    }

    private int getCenterLineX() {
        return mWidth / 2 +  getScrollX();
    }


    /**
     * 绘制在屏幕范围内的缩略图
     */
    private void drawAllThumbnail(Canvas canvas) {
        mDrawStartX = mWidth/2;

        if (iBitmapRender == null) return;

        for (int i = 0;i < thumbnailPathList.size();i ++){

            mCurrentBitmapRect.set(mDrawStartX, marginTB,mDrawStartX + mThumbnailWidth,mHeight - marginTB);

            if (mDrawStartX - getScrollX() + mThumbnailWidth >= 0 &&
                    mDrawStartX - getScrollX() <= mWidth){
                Bitmap bitmap = iBitmapRender.renderBitmap(i,thumbnailPathList.get(i));

//                Log.d(TAG,"mCurrentBitmapRect:" + mCurrentBitmapRect.toShortString());
                if (bitmap == null || bitmap.isRecycled()){
                    Log.e(TAG,"error!!! bitmap is null or is isRecycled");
                    continue;
                }
                canvas.drawBitmap(bitmap,null,mCurrentBitmapRect, mBitmapPaint);
            }else {
//                Log.d(TAG,"Not in the screen ,draw color");
                canvas.drawRect(mCurrentBitmapRect,mPendingPaint);
            }

            mDrawStartX = mDrawStartX + mThumbnailWidth;
        }
    }

    /**
     * 绘制两边的把手和边框
     */
    private void drawCutoutBorder(Canvas canvas){

        int drawStartX = mWidth / 2;

        mDragBorderPaint.setStrokeWidth(DRAG_BORDER_WIDTH);

        // 开始把手的位置
        mStartHandleRect.set(drawStartX - mDragHandleBitmap.getWidth() + mStartHandleOffsetX,
                marginTB - DRAG_BORDER_WIDTH,
                drawStartX + mStartHandleOffsetX,
                mHeight-marginTB + DRAG_BORDER_WIDTH
        );

        // 结束把手的位置
        mEndHandleRect.set(getMaxScrollX() + drawStartX + mEndHandleOffsetX,
                marginTB - DRAG_BORDER_WIDTH,
                getMaxScrollX() + drawStartX + mDragHandleBitmap.getWidth() + mEndHandleOffsetX,
                mHeight-marginTB + DRAG_BORDER_WIDTH
        );

//        Log.d(TAG, "drawCutoutBorder: " + mDrawHandleStartRect.toShortString());
        canvas.drawBitmap(mDragHandleBitmap,null, mStartHandleRect,mDragBorderPaint);

        canvas.drawLine(
                mStartHandleRect.left + mDragHandleBitmap.getWidth(),
                marginTB - DRAG_BORDER_WIDTH /2,
                mEndHandleRect.left,
                marginTB - DRAG_BORDER_WIDTH /2 ,
                mDragBorderPaint);

        canvas.drawLine(
                mStartHandleRect.left + mDragHandleBitmap.getWidth(),
                mHeight - marginTB + DRAG_BORDER_WIDTH /2,
                mEndHandleRect.left,
                mHeight - marginTB + DRAG_BORDER_WIDTH /2,
                mDragBorderPaint);



//        Log.d(TAG, "drawCutoutBorder: " + mDrawHandleEndRect.toShortString());

        canvas.drawBitmap(mDragHandleBitmap,null, mEndHandleRect,mDragBorderPaint);


    }


    /********************************************
     ******************* 滑动相关 ****************
     ********************************************/


    // 滑动速度计算
    private void initVelocityTrackerIfNotExists() {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }


    @Override
    public boolean canScrollHorizontally(int direction) {

        boolean result;
        //  加上把手的偏移
        if (direction > 0){
            result = getScrollX() < getMaxScrollX() + mEndHandleOffsetX;
        }else {
            result = getScrollX() - mStartHandleOffsetX > 0;
        }
//        Log.i(TAG,"canScrollHorizontally:" + Arrays.asList(direction,getScrollX(),result));
        return result;
    }

    /**
     * 获取当前可以滑动的最大距离
     * @param direction 正数：右滑  负数：左滑 （内容）
     * getScrollX： View左边缘和View内容左边缘在水平方方向的距离 往右为正
     * getScrollY： View上边缘和View中内容上边缘在竖直方向的距离 往下为正
     */
    public float getScrollableX(int direction){

        if (direction > 0){
            return getMaxScrollX() - getScrollX() + mEndHandleOffsetX;
        }else if (direction < 0){
            return getScrollX() - mStartHandleOffsetX;
        }

        return 0;
    }

    private int getMaxScrollX() {
        return thumbnailPathList.size() * mThumbnailWidth;
    }


    private float mLastX; // down 事件的起始坐标X
    private final int DRAGGING_ORIGIN_STATE = 0;
    private final int DRAGGING_START_HANDLE = 1;
    private final int DRAGGING_END_HANDLE = 2;

    private int isDraggingHandle = DRAGGING_ORIGIN_STATE;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

//        Log.d(TAG,"progress:" + (float)getScrollX() / getMaxScrollX());

        initVelocityTrackerIfNotExists();
        velocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                mLastX = event.getX();
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);  //解决事件冲突;
                }
                if (isRectContainWithPadding(mStartHandleRect,(int) event.getX() + getScrollX(),(int)event.getY(),DensityUtil.dp2px(1.5F))){
                    isDraggingHandle = DRAGGING_START_HANDLE;
                }else if (isRectContainWithPadding(mEndHandleRect,(int) event.getX() + getScrollX(),(int)event.getY(),DensityUtil.dp2px(1.5F))){
                    isDraggingHandle = DRAGGING_END_HANDLE;
                }

                Log.d(TAG, "isDraggingHandle: " + isDraggingHandle);

                return true;
            case MotionEvent.ACTION_MOVE:

                switch (isDraggingHandle){
                    case DRAGGING_ORIGIN_STATE:
                        performDraggingThumbnail(event);
                        break;
                    case DRAGGING_START_HANDLE:
                        performDraggingStartHandle(event);
                        break;
                    case DRAGGING_END_HANDLE:
                        performDraggingEndHandle(event);
                        break;
                }

                mLastX = event.getX();

            break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                if (isDraggingHandle == DRAGGING_ORIGIN_STATE){
                    // 根据手速滑动
                    velocityTracker.computeCurrentVelocity(1000, maximumVelocity);
                    int velocityX = (int) velocityTracker.getXVelocity();
                    fling(velocityX);
                    recycleVelocityTracker();
                }else if (onCutoutChangeListener != null){
                    float startPercent = getFormatFloat((float)Math.abs(mStartHandleOffsetX) / getMaxScrollX());
                    float endPercent = getFormatFloat(1 - (float)Math.abs(mEndHandleOffsetX) / getMaxScrollX());
                    onCutoutChangeListener.onCutoutChange(startPercent,endPercent);

                    // 超过当前播放自动滑动
                    if (getCenterLineX() < mStartHandleRect.right){
                        scrollTo(mStartHandleRect.right - mWidth/2,0);
                    }else if (getCenterLineX() > mEndHandleRect.left){
                        scrollTo(mEndHandleRect.left  - mWidth/2,0);
                    }
                    notifyProgressChange(true);
                }


                isDragging = false;
                isDraggingHandle = DRAGGING_ORIGIN_STATE;   // 重置状态
                break;
        }


        return true;
    }

    /**
     * 开始把手的拖动
     * @param event
     */
    private void performDraggingStartHandle(MotionEvent event){
        float deltaX = mLastX - event.getX(); // 正数为左滑 负数为右滑 （内容）

        if (Math.abs(deltaX) < 2) return; // 忽略小于2像素的滑动
//        Log.d(TAG, "deltaX: " + deltaX + "  mLastX：" + mLastX);

        if (deltaX > 0){
            mStartHandleOffsetX = mStartHandleOffsetX - deltaX > 0 ? (int) (mStartHandleOffsetX - deltaX) : 0;
        }else if (deltaX < 0){
            int endX = mEndHandleRect.left - mThumbnailWidth - mWidth / 2; // 预留一个缩略图的位置
            mStartHandleOffsetX = (int) Math.min(mStartHandleOffsetX - deltaX,endX);
        }

//        Log.d(TAG, "mDrawHandleStartRect: " + mDrawHandleStartRect.toShortString());
        invalidate();

    }

    /**
     * 结束把手的拖动
     * @param event
     */
    private void performDraggingEndHandle(MotionEvent event){
        float deltaX = mLastX - event.getX(); // 正数为左滑 负数为右滑 （内容）

        if (Math.abs(deltaX) < 2) return; // 忽略小于2像素的滑动
        Log.d(TAG, "deltaX: " + deltaX + "  mLastX：" + mLastX);

        // mEndHandleOffsetX 都为负数
        if (deltaX < 0){
            mEndHandleOffsetX = Math.min(0,(int) (mEndHandleOffsetX - deltaX));
        }else {
            int leftMostOffset = 0 - (getMaxScrollX() - (mStartHandleOffsetX + mThumbnailWidth)) ; // 预留一个缩略图的位置
            mEndHandleOffsetX = (int) Math.max(leftMostOffset,mEndHandleOffsetX - deltaX);
            Log.d(TAG, "leftMostOffset: " + leftMostOffset + "," + mEndHandleOffsetX);
        }

        Log.d(TAG, "mDrawHandleEndRect: " + mEndHandleRect.toShortString());
        invalidate();

    }

    /**
     * 缩略图拖动
     * @param event
     */
    private void performDraggingThumbnail(MotionEvent event) {
        float deltaX = mLastX - event.getX(); // 正数为右 负数为左滑
//        Log.d(TAG, "deltaX: " + deltaX + "  mLastX：" + mLastX);
//        Log.d(TAG, "offset: " + Arrays.asList(mStartHandleOffsetX,mEndHandleOffsetX) );

//        if (Math.abs(deltaX) < 5) return; // 忽略小于5像素的滑动

        isDragging = true;

        if (deltaX > 0 && canScrollHorizontally(1)) { // 右滑
            scrollBy((int) Math.min(deltaX, getScrollableX(1)), 0); // 取滑动的最小值，保证滑动界限
//                    Log.d(TAG, "右滑: 已经滑动了 ：" + getScrollX());
//                    Log.d(TAG, "实际滑动: " + Math.min(deltaX, getScrollableX(1)));
//                    Log.d(TAG, "剩余可滑动: " + getScrollableX(1));

        } else if (deltaX < 0 && canScrollHorizontally(-1)) {  // 左滑的时候 deltaY 为负值
            float scrollableX = getScrollableX(-1);
//                    Log.d(TAG, "左滑，已经滑动了 ：" + getScrollX());
//                    Log.d(TAG, "左滑，scrollableX ：" + scrollableX);
            if (deltaX + scrollableX < 0){
                scrollTo(mStartHandleOffsetX,0); // 边界值 直接滑回原点
//                Log.d(TAG, "scrollTo start");
            }else {
                scrollBy((int) Math.min(deltaX, scrollableX), 0); // 取滑动的最小值，保证滑动界限
//                Log.d(TAG, "min 实际滑动: " + (int) Math.min(deltaX, scrollableX));
            }

//                    Log.d(TAG, "剩余可滑动: " + getScrollableX(-1));
        }

        notifyProgressChange(true);
    }

    private void fling(int velocityX) {
        if (Math.abs(velocityX) > minimumVelocity) {

            if (Math.abs(velocityX) > maximumVelocity) { // 超过最大滑动宿舍按最大滑动速度算
                velocityX = maximumVelocity * velocityX / Math.abs(velocityX);
            }
            mScroller.fling(getScrollX(), getScrollY(), -velocityX, 0,
                    0 + mStartHandleOffsetX, //  加上把手的偏移
                    getMaxScrollX() + mEndHandleOffsetX, //  加上把手的偏移
                    0, 0);
        }
    }


    @Override
    public void computeScroll() {
        super.computeScroll();

        if (mScroller.computeScrollOffset()){ // 判断是否滑动完成
            scrollTo(mScroller.getCurrX(),0); // 滑动到当前位置
//            Log.d(TAG,"progress:" + (float)getScrollX() / getMaxScrollX());
            notifyProgressChange(true);
            invalidate();
        }

    }

    private void notifyProgressChange(boolean isFromUser){

        if (onProcessChangeListener != null){
            int cutoutLength = mEndHandleRect.left - mStartHandleRect.right;
            float durationPercent = (float)(Math.abs(mStartHandleOffsetX) + Math.abs(mEndHandleOffsetX)) / getMaxScrollX();
            onProcessChangeListener.onProcessChange(
                    getFormatFloat((float)(getCenterLineX() - mStartHandleRect.right) / cutoutLength),
                    getFormatFloat(1 - durationPercent),
                    isFromUser
            );
        }
    }

    private boolean isRectContainWithPadding(Rect rect, int x, int y, int padding){
        return rect.left < rect.right && rect.top < rect.bottom  // check for empty first
                && x >= rect.left - padding && x < rect.right + padding && y >= rect.top - padding && y < rect.bottom + padding;
    }


    public static class DefaultPathBitmapRender implements IBitmapRender{

        Map<String,Bitmap> bitmapMap = new HashMap<>();

        @Override
        public Bitmap renderBitmap(int position, Object resource) {
            Bitmap bitmap = bitmapMap.get(resource);
            if (bitmap == null || bitmap.isRecycled()){
                bitmap = BitmapFactory.decodeFile((String) resource);
                bitmapMap.put((String) resource,bitmap);
            }
            return bitmap;
        }

        @Override
        public void release() {
            for (Bitmap bitmap : bitmapMap.values()){
                bitmap.recycle();
                bitmap = null;
            }

            bitmapMap = null;
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
        iBitmapRender.release();
    }

    public interface OnProcessChangeListener {
        /**
         *
         * @param progress 剪辑范围内的progress
         * @param durationPercent 当前被剪辑的片段占总时长的百分比
         * @param isFromUser 是否来自用户拖动
         */
        void onProcessChange(float progress,float durationPercent,boolean isFromUser);
    }

    public interface OnCutoutChangeListener{
        void onCutoutChange(float startPercent,float endPercent);
    }

    public interface IBitmapRender {
        Bitmap renderBitmap(int position,Object resource);

        void release();
    }

    private Float getFormatFloat(Float data){
        return Float.parseFloat(String.format(Locale.CHINA, "%.4f", data));
    }


    /**********************************
     * 对外API
     * @param iBitmapRender
     */

    public void setiBitmapRender(IBitmapRender iBitmapRender) {
        this.iBitmapRender = iBitmapRender;
    }

    public void setOnProcessChangeListener(OnProcessChangeListener onProcessChangeListener) {
        this.onProcessChangeListener = onProcessChangeListener;
    }

    public void setOnCutoutChangeListener(OnCutoutChangeListener onCutoutChangeListener) {
        this.onCutoutChangeListener = onCutoutChangeListener;
    }

    /**
     * 滑动到 “绝对位置”
     * @param maxProgress
     */
    public void scrollOnMaxProgress(float maxProgress){
        if (progress < 0 || progress > 1) return;

        if (isDragging) return;

        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }

        scrollTo((int) (getMaxScrollX() * maxProgress),0);
    }

    /**
     * 裁剪范围内的进度
     * @param progress
     */
    public void scrollOnProgress(float progress){

        if (progress < 0 || progress > 1) return;

        if (isDragging) return;

        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }

        int cutoutDuration = mEndHandleRect.left - mStartHandleRect.right;
        int position = (int) (progress * cutoutDuration);
        scrollTo(mStartHandleRect.right + position - mWidth/2,0);
        notifyProgressChange(false);
    }

    public List<Object> getThumbnailPathList() {
        return thumbnailPathList;
    }

    public void setThumbnailPathList(List<Object> thumbnailPathList) {
        this.thumbnailPathList = thumbnailPathList;
        scrollTo(0,0);
        mEndHandleOffsetX = 0;
        mStartHandleOffsetX = 0;
    }

}
