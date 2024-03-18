package com.hjl.language.impl

import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjl.commonlib.customview.BaseTipDialog
import com.hjl.commonlib.extend.addDivider
import com.hjl.commonlib.utils.AndroidUtils
import com.hjl.jetpacklib.mvvm.view.BaseActivity
import com.hjl.language.MultiLanguage
import com.hjl.language.R
import com.hjl.language.databinding.ActivityLanguageSettingBinding
import com.hjl.module_base.router.RouterName
import com.therouter.router.Route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Route(path = RouterName.LANGUAGE_SETTING)
class LanguageSettingActivity : BaseActivity<ActivityLanguageSettingBinding>() {

    private lateinit var languageList: List<LanguageBean>
    private lateinit var mAdapter: LanguageItemAdapter
    private var currentIndex = -1
    private var currentLanguage = MultiLanguage.languageSetting.getLanguageSetting()

    override fun initData() {
        Log.i("ward", "javaClass.superclass:${javaClass.superclass.simpleName}")
        Log.i("ward", "javaClass.superclass:${javaClass.superclass.superclass.simpleName}")

        languageList = arrayListOf(
            LanguageBean(
                SPLanguageSetting.LANGUAGE_CHINESE,
                "中文",
                currentLanguage == SPLanguageSetting.LANGUAGE_CHINESE
            ),
            LanguageBean(
                SPLanguageSetting.LANGUAGE_ENGLISH,
                "English",
                currentLanguage == SPLanguageSetting.LANGUAGE_ENGLISH
            )
        )

        mAdapter = LanguageItemAdapter().apply {
            setNewData(languageList)
            setOnItemClickListener { position, view, bean ->
                currentIndex = position
                languageList.forEachIndexed { index, languageBean ->
                    languageBean.isSelect = index == currentIndex
                }
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun initView() {
        binding.baseTitleLayout.titleCenterTv.text = "语言设置"

        binding.rvLanguage.apply {
            adapter = mAdapter
            addDivider()
            layoutManager = LinearLayoutManager(this@LanguageSettingActivity)
        }
        mAdapter.notifyDataSetChanged()
        binding.btnLanguageConfirm.setOnClickListener {
            when (currentIndex) {
                0 -> currentLanguage = SPLanguageSetting.LANGUAGE_CHINESE
                1 -> currentLanguage = SPLanguageSetting.LANGUAGE_ENGLISH
            }
            if (currentLanguage != MultiLanguage.languageSetting.getLanguageSetting()) {

                BaseTipDialog(this, BaseTipDialog.TipDialogEnum.DIALOG_WITH_CONTENT)
                    .setTitle("确认更改语言设置?")
                    .setContent("变更语言将重启应用")
                    .also { dialog ->
                        dialog.setOnConfirmClickListener {
                            MultiLanguage.languageSetting.saveLanguageSetting(currentLanguage)
                            dialog.dismiss()
                            lifecycleScope.launch(Dispatchers.IO) {
                                delay(500)
                                AndroidUtils.restartApp(this@LanguageSettingActivity)
                            }
                        }
                    }.show()


            }

        }
    }

    override fun initLoad() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_language_setting
    }

}