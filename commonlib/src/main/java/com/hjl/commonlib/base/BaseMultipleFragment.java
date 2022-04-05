package com.hjl.commonlib.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hjl.commonlib.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseMultipleFragment extends Fragment {

    protected View mContentView;
    protected MultipleStatusView mMultipleStatusView;

    public CompositeDisposable mCompositeDisposable;

    protected LinearLayout mLlRoot;

    protected Activity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.common_fragment_base_multiple,container,false);
        mContentView = inflater.inflate(getLayoutId(),container,false);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(lp);
        mMultipleStatusView = mView.findViewById(R.id.multiple_state_view);
        if (mMultipleStatusView != null){
            mMultipleStatusView.addView(mContentView);
            mMultipleStatusView.setContentView(mContentView);
            mMultipleStatusView.showContent();
        }else {
            super.onCreateView(inflater, container, savedInstanceState);
        }

        initTitleView(mView);
        return mView;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivity = getActivity();

        getKeyData(savedInstanceState);
        initView(mContentView);
        initData();
    }

    protected void getKeyData(Bundle savedInstanceState){

    }

    public void notifyCustomDataChange(){

    }

    protected abstract int getLayoutId();
    protected abstract void initView(View view);
    protected abstract void initData();

    private void initTitleView(View view){

        mLlRoot = view.findViewById(R.id.ll_root);
    }

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

    @Override
    public void onDestroyView() {
        clearDisposable();
        super.onDestroyView();


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = BaseApplication.getRefWatcher(getActivity());
//        refWatcher.watch(this);
    }


    public void onLoading() {
        mMultipleStatusView.showLoading();
    }


    public void onComplete() {
        mMultipleStatusView.showContent();
    }


    public void onEmpty() {
        mMultipleStatusView.showEmpty();
    }


    public void onError() {
        mMultipleStatusView.showError();
    }
}
