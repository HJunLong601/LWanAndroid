package com.hjl.core.ui.mine

import android.content.Intent
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjl.commonlib.base.BaseApplication
import com.hjl.commonlib.base.ResourceManager
import com.hjl.commonlib.customview.BaseTipDialog
import com.hjl.commonlib.extend.addDivider
import com.hjl.commonlib.extend.quickStartActivity
import com.hjl.commonlib.utils.DensityUtil
import com.hjl.commonlib.utils.JsonUtils
import com.hjl.commonlib.utils.RecycleViewVerticalDivider
import com.hjl.commonlib.utils.ToastUtil
import com.hjl.core.R
import com.hjl.core.adpter.MineItemAdapter
import com.hjl.core.databinding.CoreFragmentMineBinding
import com.hjl.core.net.await
import com.hjl.core.net.bean.MineItemBean
import com.hjl.core.net.bean.UserBean
import com.hjl.core.net.coreApiServer
import com.hjl.core.net.launchNetRequest
import com.hjl.core.ui.login.LoginActivity
import com.hjl.core.utils.Constant
import com.hjl.core.utils.SpUtils
import com.hjl.core.utils.Utils
import com.hjl.jetpacklib.mvvm.view.BaseFragment2
import com.hjl.module_base.constants.EventKey
import com.hjl.module_base.router.RouterName
import com.jeremyliao.liveeventbus.LiveEventBus
import com.therouter.TheRouter

/**
 * Author : long
 * Description : Fragment for Main - Mine ;
 * Date : 2020/6/25
 */
class MineFragment : BaseFragment2<CoreFragmentMineBinding>(), View.OnClickListener {

    val tipDialog by lazy {
        BaseTipDialog(mContext,BaseTipDialog.TipDialogEnum.DIALOG_TIP)
            .setTitle(BaseApplication.getApplication().getString(R.string.r_y_sr_t_lg_t))
            .also { dialog ->
                dialog.setOnConfirmClickListener {
                    lifecycleScope.launchNetRequest({
                        coreApiServer.logout().await()
                        com.hjl.commonlib.utils.SpUtils.clearCookie()
                        SpUtils.saveUserInfo("")
                        BaseApplication.runOnUIThread {
                            binding.mineUserNameTv.text =
                                BaseApplication.getApplication().getString(R.string.go_log_in)
                            binding.mineUserInfo.visibility = View.GONE
                            dialog.dismiss()
                        }
                    })
                }
            }
    }

    override fun initLayoutResID(): Int {
        return R.layout.core_fragment_mine
    }

    override fun initData() {

    }

    override fun initView() {

        binding.mineUserIconIv.setOnClickListener(this)
        binding.mineUserNameTv.setOnClickListener(this)

        val adapter = MineItemAdapter()
        val itemList = ArrayList<MineItemBean>()
        buildItemList(itemList)
        adapter.setNewData(itemList)

        adapter.setOnItemClickListener { position, view, bean -> dispatchAction(bean.action) }

        binding.mineUserRv.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(context)
            addDivider()
            this.addItemDecoration(RecycleViewVerticalDivider(context,2,
                ResourceManager.getInstance().getColor(context,R.color.common_divider_line_color),
                DensityUtil.dp2px(15F),0))
        }

        LiveEventBus.get<String>(EventKey.LOGIN_STATE_CHANGE).observe(this,{
            updateUserState()
        })
        updateUserState()


    }

    private fun updateUserState() {
        if (SpUtils.getUserInfo().isNotEmpty()) {
            val userInfo = JsonUtils.parseObject(SpUtils.getUserInfo(), UserBean::class.java)
            binding.mineUserNameTv.text = userInfo.nickname
            binding.mineUserInfo.visibility = View.VISIBLE
            binding.mineUserInfo.text = String.format(
                BaseApplication.getApplication().getString(R.string.id_s_points_s),
                userInfo.id,
                userInfo.coinCount
            )
        } else {
            binding.mineUserNameTv.text =
                BaseApplication.getApplication().getString(R.string.go_log_in)
            binding.mineUserInfo.visibility = View.GONE
        }
    }

    private fun buildItemList(itemList: ArrayList<MineItemBean>) {
        itemList.add(
            MineItemBean(
                BaseApplication.getApplication().getString(R.string.my_collection),
                Constant.ACTION_COLLECT,
                R.drawable.core_icon_like
            )
        )
        itemList.add(
            MineItemBean(
                BaseApplication.getApplication().getString(R.string.theme_skin),
                Constant.ACTION_SKIN,
                R.drawable.core_icon_skin
            )
        )
        itemList.add(
            MineItemBean(
                BaseApplication.getApplication().getString(R.string.points_ranking),
                Constant.ACTION_RANK,
                R.drawable.core_icon_integral
            )
        )
        itemList.add(
            MineItemBean(
                BaseApplication.getApplication().getString(R.string.maven_query),
                Constant.ACTION_MAVEN,
                R.drawable.core_icon_maven
            )
        )
        itemList.add(
            MineItemBean(
                BaseApplication.getApplication().getString(R.string.language_settings),
                Constant.ACTION_LANGUAGE,
                R.drawable.core_icon_language
            )
        )
        itemList.add(
            MineItemBean(
                BaseApplication.getApplication().getString(R.string.log_out_and_log_in),
                Constant.ACTION_EXIT,
                R.drawable.core_icon_exit
            )
        )
    }

    override fun loadData() {

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.mine_user_icon_iv,R.id.mine_user_icon_iv ->{
                if (!Utils.hasCookie()){
                    LoginActivity.startLoginActivity(activity!!)
                }
            }
        }
    }

    private fun dispatchAction(action : String){
        when (action){
            Constant.ACTION_COLLECT -> {
                if (Utils.hasCookie()){
                    mContext.startActivity(Intent(mContext, CollectListActivity::class.java))
                }else{
                    LoginActivity.startLoginActivity(mContext)
                }
            }
            Constant.ACTION_EXIT -> {
                if (Utils.hasCookie()){
                    tipDialog.show()
                }else{
                    ToastUtil.show(
                        BaseApplication.getApplication().getString(R.string.y_hv_nt_lggd_n_yt)
                    )
                }
            }

            Constant.ACTION_MAVEN -> {
                quickStartActivity(MavenActivity::class.java)
            }

            Constant.ACTION_SKIN -> {
                TheRouter.build(RouterName.SKIN_SKIN_ACTIVITY).navigation()
            }

            Constant.ACTION_LANGUAGE -> {
                TheRouter.build(RouterName.LANGUAGE_SETTING).navigation()
            }

            else -> {
                ToastUtil.show(BaseApplication.getApplication().getString(R.string.not_yet_open))
            }
        }
    }
}