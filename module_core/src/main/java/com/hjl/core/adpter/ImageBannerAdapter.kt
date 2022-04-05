package com.hjl.core.adpter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hjl.core.R
import com.hjl.core.net.bean.HomeBannerBean
import com.youth.banner.adapter.BannerAdapter

/**
 * Description Simple ImageView Adapter for BannerView
 * Author long
 * Date 2020/6/8 21:49
 */
class ImageBannerAdapter(data : MutableList<HomeBannerBean>) : BannerAdapter<HomeBannerBean,ImageBannerAdapter.BannerViewHolder>(data){



    class BannerViewHolder(val view : ImageView) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {

        val imageView = ImageView(parent?.context);
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        return BannerViewHolder(imageView)
    }

    override fun onBindView(holder: BannerViewHolder?, data: HomeBannerBean?, position: Int, size: Int) {
        if (data?.imagePath.isNullOrEmpty()){
            Glide.with(holder?.view!!).load(R.drawable.core_custom_pic).centerCrop().into(holder.view)
        }else{
            Glide.with(holder?.view!!).load(data?.imagePath).into(holder.view)
        }
    }
}