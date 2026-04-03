package com.hjl.core.adpter

import android.app.Activity
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.hjl.commonlib.utils.ToastUtil
import com.hjl.core.R
import com.hjl.core.customview.HomeBannerView
import com.hjl.core.net.bean.HomeBannerBean
import com.hjl.core.ui.SimpleWebActivity
import com.youth.banner.indicator.CircleIndicator

class HomeBannerHeaderAdapter(
    private val activity: Activity
) : RecyclerView.Adapter<HomeBannerHeaderAdapter.HomeBannerHeaderViewHolder>() {

    private val bannerData = mutableListOf<HomeBannerBean>()
    private var bannerView: HomeBannerView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeBannerHeaderViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.core_header_banner, parent, false)
        return HomeBannerHeaderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeBannerHeaderViewHolder, position: Int) {
        if (bannerView !== holder.bannerView) {
            bannerView = holder.bannerView
            bannerView?.isFocusable = false
            bannerView?.isFocusableInTouchMode = false
            bannerView?.addBannerLifecycleObserver(activity as LifecycleOwner)
                ?.setAdapter(ImageBannerAdapter(bannerData), false)
                ?.setIndicator(CircleIndicator(activity))
                ?.isAutoLoop(false)
                ?.setIntercept(true)
                ?.setOnBannerListener { data, _ ->
                    val bean = data as HomeBannerBean
                    if (bean.url.isEmpty()) {
                        ToastUtil.show("Hello World!")
                    } else {
                        SimpleWebActivity.loadUrl(activity, bean.url)
                    }
                }
            bannerView?.viewPager2?.apply {
                descendantFocusability = ViewPager2.FOCUS_BLOCK_DESCENDANTS
                isFocusable = false
                isFocusableInTouchMode = false
            }
        }
        bannerView?.setDatas(bannerData)
        bannerView?.setCurrentItem(0, false)
    }

    override fun getItemCount(): Int = 1

    fun submitBannerData(data: List<HomeBannerBean>) {
        bannerData.clear()
        bannerData.addAll(data)
        notifyItemChanged(0)
    }

    class HomeBannerHeaderViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val bannerView: HomeBannerView = itemView.findViewById(R.id.home_banner)
    }
}
