package com.hjl.commonlib.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjl.commonlib.R;

public class MultipleStatusView extends RelativeLayout{

    private static final String TAG = "MultipleStatusView";

    private static final RelativeLayout.LayoutParams DEFAULT_LAYOUT_PARAMS =
            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);

    public static final int STATUS_CONTENT = 0x00;
    public static final int STATUS_LOADING = 0x01;
    public static final int STATUS_EMPTY = 0x02;
    public static final int STATUS_ERROR = 0x03;
    public static final int STATUS_NO_NETWORK = 0x04;

    private static final int NULL_RESOURCE_ID = -1;

    private View mEmptyView;
    private View mErrorView;
    private View mLoadingView;
    private View mNoNetworkView;

    public View getmContentView() {
        return mContentView;
    }

    private View mContentView;
    private ImageView mEmptyRetryView;
    private View mErrorRetryView;
    private View mNoNetworkRetryView;

    private OnClickListener mOnRetryClickListener;

    private int mEmptyViewResId;
    private int mErrorViewResId;
    private int mLoadingViewResId;
    private int mNoNetworkViewResId;
    private int mContentViewResId;

    private int contentId = NULL_RESOURCE_ID;

    private int mViewStatus = -1;
    private LayoutInflater mInflater;
    private final ViewGroup.LayoutParams mLayoutParams = new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


    public MultipleStatusView(Context context) {
        super(context);
    }

    public MultipleStatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultipleStateViewAttrs, defStyleAttr, 0);
        mEmptyViewResId = a.getResourceId(R.styleable.MultipleStateViewAttrs_emptyView,R.layout.common_custom_empty_view);
        mErrorViewResId = a.getResourceId(R.styleable.MultipleStateViewAttrs_errorView, R.layout.common_custom_error_view);
        mLoadingViewResId = a.getResourceId(R.styleable.MultipleStateViewAttrs_loadingView,R.layout.common_custom_loading_view);
        mNoNetworkViewResId = a.getResourceId(R.styleable.MultipleStateViewAttrs_noNetworkView, R.layout.common_custom_no_network_view);
        mContentViewResId = a.getResourceId(R.styleable.MultipleStateViewAttrs_contentView, NULL_RESOURCE_ID);
        a.recycle();
        mInflater = LayoutInflater.from(getContext());
    }



    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clear(mEmptyView, mLoadingView, mErrorView, mNoNetworkView);
    }


    /**
     * 显示内容视图
     */
    public final void showContent() {
        mViewStatus = STATUS_CONTENT;
        if (null == mContentView) {
            if (mContentViewResId != NULL_RESOURCE_ID) {
                mContentView = mInflater.inflate(mContentViewResId, null);
                addView(mContentView, 0, mLayoutParams);
            } else if (contentId != NULL_RESOURCE_ID) {
                mContentView = findViewById(contentId);
            }
        }
        showViewByStatus(mViewStatus);
    }

    /**
     * 显示空视图
     */
    public final void showEmpty() {
        mViewStatus = STATUS_EMPTY;
        if (null == mEmptyView) {
            mEmptyView = mInflater.inflate(mEmptyViewResId, null);
            mEmptyRetryView = mEmptyView.findViewById(R.id.empty_retry_view);
            if (null != mOnRetryClickListener && null != mEmptyRetryView) {
                mEmptyRetryView.setOnClickListener(mOnRetryClickListener);
            }
            addView(mEmptyView, 0, mLayoutParams);
        }
        showViewByStatus(mViewStatus);
    }

    /**
     * @param emptyText 为空时显示的字体;
     * @param showBtn 是否显示刷新按钮;
     * @param showBtnText 按钮显示的文字;
     */
    public final void showEmpty(String emptyText, boolean showBtn,String showBtnText) {
        mViewStatus = STATUS_EMPTY;
        if (null == mEmptyView) {
            mEmptyView = mInflater.inflate(mEmptyViewResId, null);
            mEmptyRetryView = mEmptyView.findViewById(R.id.empty_retry_view);
            TextView emptyTextView = (TextView) mEmptyView.findViewById(R.id.empty_view_tv);
            Button emptyButton = (Button) mEmptyView.findViewById(R.id.empty_view_btn);
            if (null != mOnRetryClickListener && null != mEmptyRetryView) {
                mEmptyRetryView.setOnClickListener(mOnRetryClickListener);
            }
            if (emptyText != null) {
                emptyTextView.setText(emptyText);
            }
            if (showBtn) {
                emptyButton.setVisibility(VISIBLE);
                emptyButton.setText(showBtnText);
                emptyButton.setOnClickListener(mOnRetryClickListener);
            }
            addView(mEmptyView, 0, mLayoutParams);
        }
        showViewByStatus(mViewStatus);
    }

    /**
     * 显示错误视图
     */
    public final void showError() {
        mViewStatus = STATUS_ERROR;
        if (null == mErrorView) {
            mErrorView = mInflater.inflate(mErrorViewResId, null);
            mErrorRetryView = mErrorView.findViewById(R.id.error_retry_view);
            if (null != mOnRetryClickListener && null != mErrorRetryView) {
                mErrorRetryView.setOnClickListener(mOnRetryClickListener);
            }
            addView(mErrorView, 0, mLayoutParams);
        }
        showViewByStatus(mViewStatus);
    }

    /**
     * 显示加载中视图
     */
    public final void showLoading() {
        mViewStatus = STATUS_LOADING;
        if (null == mLoadingView) {
            mLoadingView = mInflater.inflate(mLoadingViewResId, null);
            addView(mLoadingView, 0, mLayoutParams);
        }
        showViewByStatus(mViewStatus);
    }

    private void showViewByStatus(int viewStatus) {
        if (null != mLoadingView) {
            mLoadingView.setVisibility(viewStatus == STATUS_LOADING ? View.VISIBLE : View.GONE);
        }
        if (null != mEmptyView) {
            mEmptyView.setVisibility(viewStatus == STATUS_EMPTY ? View.VISIBLE : View.GONE);
        }
        if (null != mErrorView) {
            mErrorView.setVisibility(viewStatus == STATUS_ERROR ? View.VISIBLE : View.GONE);
        }
        if (null != mNoNetworkView) {
            mNoNetworkView.setVisibility(viewStatus == STATUS_NO_NETWORK ? View.VISIBLE : View.GONE);
        }
        if (null != mContentView) {
            mContentView.setVisibility(viewStatus == STATUS_CONTENT ? View.VISIBLE : View.GONE);
        }
    }

    private void clear(View... views) {
        if (null == views) {
            return;
        }
        try {
            for (View view : views) {
                if (null != view) {
                    removeView(view);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置重试点击事件
     *
     * @param onRetryClickListener 重试点击事件
     */
    public void setOnRetryClickListener(OnClickListener onRetryClickListener) {
        this.mOnRetryClickListener = onRetryClickListener;
    }

    public View setEmpty(int emptyViewResId) {
        mEmptyViewResId = emptyViewResId;
        return this;
    }

    public View setErrorView(int errorViewResId) {
        mErrorViewResId = errorViewResId;
        return this;
    }

    public View setLoadingView(int loadingViewResId) {
        mLoadingViewResId = loadingViewResId;
        return this;
    }

    public View setNoNetworkView(int noNetworkViewResId) {
        mNoNetworkViewResId = noNetworkViewResId;
        return this;
    }

    public View setContentViewResId(int contentViewResId) {
        mContentViewResId = contentViewResId;
        return this;
    }

    /**
     * 设置内容视图,视图要是此控件的在一个页面
     */
    public void setContentView(View contentView) {
        mContentView = contentView;
    }

    public void clearView(){

        removeAllViews();

        if (null != mLoadingView) {
            mLoadingView = null;
        }
        if (null != mEmptyView) {
            mEmptyView = null;
        }
        if (null != mErrorView) {
            mErrorView = null;
        }
        if (null != mNoNetworkView) {
            mNoNetworkView = null;
        }
        if (null != mContentView) {
            mContentView = null;
        }
    }
}
