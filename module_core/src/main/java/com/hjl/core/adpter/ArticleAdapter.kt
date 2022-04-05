package com.hjl.core.adpter

import android.app.Activity
import android.content.Context
import android.text.Html
import android.text.SpannableStringBuilder
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import com.hjl.commonlib.base.ResourceManager
import com.hjl.commonlib.customview.SpannableStringUtils
import com.hjl.commonlib.utils.LogUtils
import com.hjl.commonlib.utils.ToastUtil
import com.hjl.core.R
import com.hjl.core.base.PagingDataAdapter
import com.hjl.core.base.PagingDataViewHolder
import com.hjl.core.databinding.CoreItemHomeArticleBinding
import com.hjl.core.net.bean.HomeArticleBean
import com.hjl.core.net.bean.HomeBannerBean
import com.hjl.core.ui.SimpleWebActivity
import com.youth.banner.Banner
import com.youth.banner.indicator.CircleIndicator

/**
 * author: long
 * description HomeArticleAdapter // https://www.jianshu.com/p/0b7c82a5c27f
 * Date: 2020/6/11
 */
class ArticleAdapter(private val context : Context)
    : PagingDataAdapter<HomeArticleBean.Article,ArticleAdapter.HomeArticleViewHolder, CoreItemHomeArticleBinding>(diff) {

    private var mBanner : Banner<HomeBannerBean, ImageBannerAdapter>? = null
    var cacheBannerData : MutableList<HomeBannerBean>? = null

    var isRequestFocus = false

    override fun getLayoutRes(): Int {
        return R.layout.core_item_home_article
    }

    override fun bindData(holder: HomeArticleViewHolder, position: Int) {
        this.getItem(position)?.let {
            holder.binding.bean = getItem(position)
            holder.binding.executePendingBindings()
            holder.binding.homeArticleTitleTv.text = Html.fromHtml(getItem(position)?.title)
            setAuthorText(it,holder.binding)
            setTagText(it,holder.binding)
        }

        holder.addChildClick(R.id.home_article_like_iv)
    }

    override fun bindHeaderView(view: View) {
        LogUtils.i("bindHeaderView")
        mBanner = view.findViewById(R.id.home_banner)
        mBanner?.addBannerLifecycleObserver(context as LifecycleOwner)
            ?.setAdapter(ImageBannerAdapter(arrayListOf()))
            ?.setIndicator(CircleIndicator(context))
            ?.setLoopTime(2000)
            ?.setIntercept(true)
            ?.setOnBannerListener { data, position ->
                val bean = data as HomeBannerBean
                if (bean.url.isEmpty()){
                    ToastUtil.show("Hello World!")
                }else{
                    SimpleWebActivity.loadUrl(context as Activity, bean.url)
                }
            }
            ?.start()

        // 重复创建VH导致数据丢失 先这样赋值一个缓存的数据
        cacheBannerData?.let {
            mBanner?.setDatas(it)
        }

    }

    fun setBannerData(data : MutableList<HomeBannerBean>){
        mBanner?.setDatas(data)
        cacheBannerData = data
    }

    private fun setAuthorText(item : HomeArticleBean.Article,binding: CoreItemHomeArticleBinding){
        val sb = SpannableStringBuilder("${item.author}${item.shareUser}")

        if (item.isTop){
            SpannableStringUtils.addStartRoundBackgroundSpanString(
                sb,
                "顶",
                ResourceManager.getInstance().getColor(context,R.color.common_red),
                ResourceManager.getInstance().getColor(context,R.color.common_red)
            )
        }
        if (item.isFresh){
            SpannableStringUtils.addStartRoundBackgroundSpanString(
                sb,
                "新",
                ResourceManager.getInstance().getColor(context,R.color.common_red),
                ResourceManager.getInstance().getColor(context,R.color.common_red)
            )
        }

        // setText 必须是 SpannableString 否则会失去效果
        binding.homeArticleAuthorTv.text = sb
//        SpannableStringUtils.appendClickableSpan(sb,"点击"){
//            ToastUtil.show("点击")
//        }
//        binding.homeArticleAuthorTv.movementMethod = LinkMovementMethod.getInstance()

    }

    private fun setTagText(item : HomeArticleBean.Article,binding: CoreItemHomeArticleBinding){

        val sb = SpannableStringBuilder("${item.superChapterName}·${item.chapterName}")

        for (tag in item.tags){
            SpannableStringUtils.addStartRoundBackgroundSpanString(
                sb,
                tag.name,
                ResourceManager.getInstance().getColor(context,R.color.core_tag_green),
                ResourceManager.getInstance().getColor(context,R.color.core_tag_green)
            )
        }

        binding.homeArticleTagTv.text = sb
    }

    class HomeArticleViewHolder(binding: CoreItemHomeArticleBinding) : PagingDataViewHolder<CoreItemHomeArticleBinding>(binding){


    }

    companion object{
        val diff = object : DiffUtil.ItemCallback<HomeArticleBean.Article>(){
            override fun areItemsTheSame(oldItem: HomeArticleBean.Article, newItem: HomeArticleBean.Article): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HomeArticleBean.Article, newItem: HomeArticleBean.Article): Boolean {
                return oldItem.title == newItem.title
            }
        }
    }

}