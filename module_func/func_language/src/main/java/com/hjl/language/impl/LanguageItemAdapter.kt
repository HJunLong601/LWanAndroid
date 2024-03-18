package com.hjl.language.impl

import com.hjl.jetpacklib.mvvm.recycleview.BaseRecyclerViewAdapter
import com.hjl.language.R
import com.hjl.language.databinding.ItemLanguageBinding

class LanguageItemAdapter : BaseRecyclerViewAdapter<LanguageBean, ItemLanguageBinding>() {
    override fun bindData(binding: ItemLanguageBinding?, data: LanguageBean) {
        binding?.tvLanguageName?.text = data.name
        binding?.cbLanguage?.isChecked = data.isSelect
    }

    override fun getLayoutId(): Int {
        return R.layout.item_language
    }
}