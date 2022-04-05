package com.hjl.commonlib.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.hjl.commonlib.R;
import com.hjl.commonlib.utils.StatusBarUtil;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseMultipleActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    protected MultipleStatusView mMultipleStateView;
    protected LinearLayout mLlRoot;

    public CompositeDisposable mCompositeDisposable;
    private boolean mIsRelease = false;

    protected String TAG = getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AppCompat_NoActionBar);

        setContentView(getLayoutId());

        initBaseView();
        setStatusBar();
//        getWindow().setBackgroundDrawable(null);

        initKeyData();
        initTitle();
        initView();
        initData();
    }

    public void initTitle(){

    }

    @Override
    public void setContentView(int layoutResID) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View rootView = inflater.inflate(R.layout.common_activity_base_multiple,null);
        setContentView(rootView);
        mMultipleStateView = rootView.findViewById(R.id.multiple_state_view);

        View contentView = inflater.inflate(layoutResID, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.setLayoutParams(lp);
//        contentView.setBackground(null);

        if (null != mMultipleStateView) {
            mMultipleStateView.addView(contentView);
            mMultipleStateView.setContentView(contentView);
            mMultipleStateView.showContent();
        } else {
            super.setContentView(layoutResID);
        }
    }


    protected void initBaseView(){

        mLlRoot = findViewById(R.id.ll_root);
    }

    protected void initKeyData(){

    }

    protected void initData(){

    }

    protected void initView(){

    }

    protected abstract int getLayoutId();


    /**
     * 添加订阅
     */
    public void addDisposable(Disposable mDisposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(mDisposable);
    }

    /**
     * 取消所有订阅
     */
    public void clearDisposable() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    protected void realReleaseResource(){

    }

    protected void realRestoreResource(){

    }

    private void restoreResource(){
        mIsRelease = true;
        realRestoreResource();
    }

    private void releaseResource(){

        if (mIsRelease) return;

        if (isFinishing()){
            realReleaseResource();
        }

        mIsRelease = true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        restoreResource();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseResource();
    }

    @Override
    protected void onDestroy() {
        clearDisposable();
        super.onDestroy();

        releaseResource();
    }



    public void showProgress(String msg, boolean cancelable){
        progressDialog = ProgressDialog.show(this,null,msg,false,cancelable);
    }

    public void dismissProgress(){
        if (progressDialog == null){
            return;
        }

        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    public void showLoading() {
        mMultipleStateView.showLoading();
    }


    public void showComplete() {
        mMultipleStateView.showContent();
    }


    public void showEmpty() {
        mMultipleStateView.showEmpty();
    }


    public void showError() {
        mMultipleStateView.showError();
    }


    protected void setStatusBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isUseFullScreenMode()) {
                StatusBarUtil.transparencyBar(this);
            } else {
                StatusBarUtil.setStatusBarColor(this, getStatusBarColor());
            }

            if (isUseBlackFontWithStatusBar()) {
                StatusBarUtil.setLightStatusBar(this, true, isUseFullScreenMode());
            }
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            StatusBarUtil.setStatusBar(getWindow());
        }
    }

    /**
     * 是否改变状态栏文字颜色为黑色，默认为黑色
     */
    protected boolean isUseBlackFontWithStatusBar() {
        return true;
    }

    /**
     * 是否设置成透明状态栏，即就是全屏模式
     */
    protected boolean isUseFullScreenMode() {
        return false;
    }

    /**
     * 更改状态栏颜色，只有非全屏模式下有效
     */
    protected int getStatusBarColor() {
        return R.color.common_white;
    }

}
