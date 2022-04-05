package com.hjl.skin

import android.graphics.drawable.ColorDrawable
import com.hjl.jetpacklib.mvvm.recycleview.BaseRecyclerViewAdapter
import com.hjl.skin.databinding.SkinItemSkinSelectBinding

/**
 * Author : long
 * Description :
 * Date : 2021/12/21
 */
class SkinAdapter : BaseRecyclerViewAdapter<SkinItemBean, SkinItemSkinSelectBinding>() {
    override fun bindData(binding: SkinItemSkinSelectBinding?, data: SkinItemBean?) {
        data?.let {
            if (it.backgroundRes != -1){
                binding?.itemSkinIv!!.setBackgroundResource(it.backgroundRes)
            }else{
                val colorDrawable = ColorDrawable(data.backgroundColor)
                binding?.itemSkinIv!!.setImageDrawable(colorDrawable)
            }

            binding.itemSkinTv.text = data.desc

        }
    }

    override fun getLayoutId(): Int {
        return R.layout.skin_item_skin_select
    }
}