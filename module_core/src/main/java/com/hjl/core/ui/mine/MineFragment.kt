package com.hjl.core.ui.mine

import android.content.Intent
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
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

/**
 * Author : long
 * Description : Fragment for Main - Mine ;
 * Date : 2020/6/25
 */
class MineFragment : BaseFragment2<CoreFragmentMineBinding>(), View.OnClickListener {

    val tipDialog by lazy {
        BaseTipDialog(mContext,BaseTipDialog.TipDialogEnum.DIALOG_TIP)
                .setTitle("确定退出登录？").also { dialog ->
                dialog.setOnConfirmClickListener {
                    lifecycleScope.launchNetRequest({
                        coreApiServer.logout().await()
                        com.hjl.commonlib.utils.SpUtils.clearCookie()
                        SpUtils.saveUserInfo("")
                        BaseApplication.runOnUIThread {
                            binding.mineUserNameTv.text = "去登陆"
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
            binding.mineUserInfo.text = "ID:${userInfo.id}    积分: ${userInfo.coinCount}"
        } else {
            binding.mineUserNameTv.text = "去登陆"
            binding.mineUserInfo.visibility = View.GONE
        }
    }

    private fun buildItemList(itemList: ArrayList<MineItemBean>) {
        itemList.add(MineItemBean("我的收藏", Constant.ACTION_COLLECT, R.drawable.core_icon_like))
        itemList.add(MineItemBean("主题皮肤", Constant.ACTION_SKIN, R.drawable.core_icon_skin))
        itemList.add(MineItemBean("积分榜单", Constant.ACTION_RANK, R.drawable.core_icon_integral))
        itemList.add(MineItemBean("Maven查询", Constant.ACTION_MAVEN, R.drawable.core_icon_maven))
        itemList.add(MineItemBean("退出登录", Constant.ACTION_EXIT, R.drawable.core_icon_exit))
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
                    ToastUtil.show("您尚未登录")
                }
            }
            Constant.ACTION_MAVEN -> {
                quickStartActivity(MavenActivity::class.java)
            }

            Constant.ACTION_SKIN -> {
                ARouter.getInstance().build(RouterName.SKIN_SKIN_ACTIVITY).navigation()
            }
            else -> {
                ToastUtil.show("暂未开放")
            }
        }
    }
}