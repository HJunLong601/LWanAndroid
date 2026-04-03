# 工程升级进度表

## 执行规则

- 每完成一次实际执行，都更新本表。
- 状态使用：`未开始`、`进行中`、`已完成`、`阻塞`。
- “关联提交”仅记录已经提交到 Git 的变更。

## 当前进度

| 阶段 | 任务 | 状态 | 结果 | 关联文件 | 关联提交 |
| --- | --- | --- | --- | --- | --- |
| 阶段 1 | 清理 `app` Manifest 重复/过时权限 | 已完成 | 去除了重复网络权限和旧存储/电话/A-GPS 权限声明 | `app/src/main/AndroidManifest.xml` | `c0a33f3` |
| 阶段 1 | 收敛启动页运行时权限申请 | 已完成 | `MainActivity` 仅保留定位权限申请，移除旧存储和非运行时权限申请 | `module_core/src/main/java/com/hjl/core/MainActivity.kt` | `c0a33f3` |
| 阶段 1 | 修复关键乱码文件 | 已完成 | 修复了关键构建脚本、README 和 Application 注释/文案中的乱码与不可读文本 | `app/build.gradle.kts`、`settings.gradle.kts`、`README.md`、`app/src/main/java/com/hjl/lwanandroid/WanApplication.kt` | `fd07db4` |
| 阶段 1 | 统一版本配置来源 | 已完成 | 根构建不再加载历史 `version.gradle`，版本来源收敛到 `buildSrc` | `build.gradle.kts`、`version.gradle` | `11cf099` |
| 阶段 2 | 盘点构建基线与插件兼容关系 | 已完成 | 确认当前组合为 Gradle 7.5 + AGP 7.4.2 + Kotlin 1.6.21 + JDK 11；`Booster + skin-plugin` 是主要升级风险，`TheRouter` 正在使用，`com.hjl.plugin` 在 `app` 中未启用 | `gradle/wrapper/gradle-wrapper.properties`、`build.gradle.kts`、`buildSrc/build.gradle.kts`、`buildSrc/src/main/kotlin/VersionConfig.kt`、`gradle-plugin/skin-plugin/**`、`gradle-plugin/plugin/**`、`app/build.gradle.kts` | `f6acfed` |
| 阶段 3 | 评估移除字节码换肤替换方案 | 已完成 | 明确不能直接让 `jetpacklib/commonlib` 继承 `app` 模块里的 `SkinBaseActivity`；可行路径是把换肤基类下沉到公共模块或功能模块，再让页面基类显式继承 | `app/src/main/java/com/hjl/lwanandroid/skin/SkinBaseActivity.java`、`app/src/main/java/com/hjl/lwanandroid/WanBaseActivity.kt`、`jetpacklib/src/main/java/com/hjl/jetpacklib/mvvm/view/BaseActivity.kt`、`commonlib/src/main/java/com/hjl/commonlib/base/BaseMultipleActivity.java` | `021f7f1` |
| 阶段 3 | 下沉公共换肤基类并接入多语言钩子 | 已完成 | 在 `commonlib` 新增公共 `SkinBaseActivity` 与 `ActivityDelegateRegistry`，`BaseActivity`/`BaseMultipleActivity` 改为显式继承，`WanApplication` 负责注册多语言钩子 | `commonlib/src/main/java/com/hjl/commonlib/base/ActivityDelegateRegistry.kt`、`commonlib/src/main/java/com/hjl/commonlib/base/SkinBaseActivity.java`、`commonlib/src/main/java/com/hjl/commonlib/base/BaseMultipleActivity.java`、`jetpacklib/src/main/java/com/hjl/jetpacklib/mvvm/view/BaseActivity.kt`、`app/src/main/java/com/hjl/lwanandroid/WanApplication.kt`、`commonlib/build.gradle.kts` | `7c94bd2` |
| 阶段 3 | 停用换肤字节码替换逻辑 | 已完成 | `SkinApplicationTransformer` 已改为 no-op，构建不再执行 Activity 父类替换；`assembleDebug` 通过且 D8 构造函数告警消失 | `buildSrc/build.gradle.kts`、`gradle-plugin/skin-plugin/src/main/kotlin/SkinApplicationTransformer.kt` | `7c94bd2` |
| 阶段 4 | 固化旧 Transform 链路兼容开关 | 已完成 | 在当前 Gradle/AGP 基线上显式启用 `legacyTransform.forceNonIncremental`，降低保留旧实现时的构建不确定性 | `gradle.properties` | 待提交 |
| 阶段 4 | 定位仍生效的旧 Transform 接入点 | 已完成 | 确认触发源不是 `app` 显式应用 `com.hjl.plugin`，而是根 `build.gradle.kts` 的 `custom-plugin` classpath 仍通过本地 Maven 运行时依赖带入 `skin-plugin:1.0.0`；该 classpath 目前不能直接移除，否则会触发 Gradle 类加载约束错误并导致构建失败 | `build.gradle.kts`、`gradle-plugin/plugin/build.gradle.kts`、`localMaven/com/hjl/plugin/custom-plugin/1.0.1/custom-plugin-1.0.1.pom`、`localMaven/com/hjl/plugin/skin-plugin/1.0.0/skin-plugin-1.0.0.pom` | 待提交 |
| 阶段 4 | 修复显式继承迁移后的 `VerifyError` 风险 | 已完成 | 将 `skin-plugin` 改为绝对 no-op 并重新发布到本地 Maven；默认主工程构建图中移除历史插件工程与 `custom-plugin` buildscript classpath，同时把 `app` 构建脚本中的 AGP 布尔解析改为本地函数，避免旧 Transform 再次改写 `BaseActivity`/`BaseMultipleActivity` | `gradle-plugin/skin-plugin/src/main/kotlin/SkinApplicationTransformer.kt`、`localMaven/com/hjl/plugin/skin-plugin/**`、`build.gradle.kts`、`settings.gradle.kts`、`app/build.gradle.kts` | 待提交 |
| 阶段 4 | 移除默认构建链路中的 Booster | 已完成 | `app` 不再应用 `com.didiglobal.booster`，根构建也不再加载 Booster Gradle 插件；历史 Booster 相关源码继续保留但不再参与主构建 | `app/build.gradle.kts`、`build.gradle.kts`、`README.md` | 待提交 |
| 阶段 4 | 确认剩余 Transform 警告来源 | 已完成 | 反编译确认 `TheRouter` 插件会在 `apply(Project)` 中调用 `registerTransform`，当前项目仍在使用 `@Route` 与 `TheRouter.build(...).navigation()`，因此它是现阶段升级到 AGP 8 前的剩余主要构建阻塞之一 | `app/build.gradle.kts`、`module_core/src/main/java/com/hjl/core/ui/mine/MineFragment.kt`、`module_func/func_language/src/main/java/com/hjl/language/impl/LanguageSettingActivity.kt`、`module_func/func_skin/src/main/java/com/hjl/skin/SkinActivity.kt` | 待提交 |
| 阶段 4 | 收口业务层的路由调用入口 | 已完成 | 在 `module_base` 新增统一导航入口 `RouterNavigator`，`MineFragment` 不再直接调用 `TheRouter.build(...).navigation()`；后续如果升级或替换路由框架，业务层改动面会更小 | `module_base/src/main/java/com/hjl/module_base/router/RouterNavigator.kt`、`module_core/src/main/java/com/hjl/core/ui/mine/MineFragment.kt` | 待提交 |
| 阶段 4 | 升级 Gradle/AGP/Kotlin/SDK 基线 | 已完成 | 根工程切换到现代插件声明；Gradle 升级到 `8.7`，AGP 升级到 `8.5.2`，Kotlin 升级到 `1.9.22`，`compileSdk/targetSdk` 升级到 `34`，并完成 JDK 17 构建验证 | `gradle/wrapper/gradle-wrapper.properties`、`build.gradle.kts`、`settings.gradle.kts`、`buildSrc/build.gradle.kts`、`buildSrc/src/main/kotlin/VersionConfig.kt`、各模块 `build.gradle.kts` | `0a31906` |
| 阶段 5 | 升级核心依赖与应用兼容层 | 已完成 | 升级 `TheRouter 1.3.2`、`Hilt 2.51.1`、`Room 2.6.1`、`Paging 3.3.6`、`Lifecycle 2.8.7`、`AppCompat 1.7.0`、`Material 1.12.0`、`OkHttp 4.12.0`、`Retrofit 2.11.0`，并修复 `OkHttp 4` API 变更、Android 12+ `android:exported`、协程 `BroadcastChannel` 迁移、非传递 `R` 兼容、历史权限/格式化/lint 阻塞 | `buildSrc/src/main/kotlin/VersionConfig.kt`、`gradle.properties`、`commonlib/src/main/java/com/hjl/commonlib/network/interceptor/LogInterceptor.kt`、`commonlib/src/main/java/com/hjl/commonlib/utils/AndroidUtils.java`、`commonlib/src/main/AndroidManifest.xml`、`commonlib/src/main/res/layout/common_dialog_mark_content.xml`、`commonlib/src/main/res/layout/common_item_base_tv.xml`、`module_core/src/main/AndroidManifest.xml`、`module_core/src/moduledebug/AndroidManifest.xml`、`module_core/src/main/java/com/hjl/core/viewmodel/MavenViewModel.kt`、`module_core/src/main/java/com/hjl/core/viewmodel/HomeSearchViewModel.kt`、`module_core/src/main/java/com/hjl/core/ui/**`、`module_core/src/main/res/**`、`app/src/main/AndroidManifest.xml`、`app/src/main/java/com/hjl/lwanandroid/WanBaseActivity.kt` | `0a31906` |
| 阶段 6 | 补最小测试面与回归验证 | 已完成 | `assembleDebug`、`test`、`lintDebug` 全部通过；升级闭环完成，当前剩余为非阻塞警告与体验级优化项 | `app/build.gradle.kts`、`app/build/reports/**`、各模块 `build/reports/**` | `0a31906` |
| 阶段 7 | 实现“积分榜单”Compose 页面首版 | 已完成 | 在“我的”页接入积分榜单入口，基于 WanAndroid `coin/rank/{page}/json` 完成 Compose 榜单页面、首屏效果和分页加载 | `buildSrc/src/main/kotlin/VersionConfig.kt`、`module_core/build.gradle.kts`、`module_core/src/main/AndroidManifest.xml`、`module_core/src/main/java/com/hjl/core/net/CoreApiServer.kt`、`module_core/src/main/java/com/hjl/core/net/bean/CoinRankPageBean.kt`、`module_core/src/main/java/com/hjl/core/repository/CoinRankRepository.kt`、`module_core/src/main/java/com/hjl/core/viewmodel/CoinRankViewModel.kt`、`module_core/src/main/java/com/hjl/core/ui/mine/CoinRankActivity.kt`、`module_core/src/main/java/com/hjl/core/ui/mine/MineFragment.kt`、`module_core/src/main/res/values/strings.xml`、`module_core/src/main/res/values-en/strings.xml` | `9a0077c` |
| 阶段 7 | 优化积分榜单中文文案与头像对比度 | 已完成 | 页面文案统一为中文显示，前三名标题改为中文，后续排名头像改为高对比实色底，提升可见性 | `module_core/src/main/java/com/hjl/core/ui/mine/CoinRankActivity.kt`、`module_core/src/main/res/values/strings.xml`、`module_core/src/main/res/values-en/strings.xml` | 待提交 |

## 最近一次执行

- 时间：2026-04-03
- 内容：优化“积分榜单”页面的中文文案与头像对比度，解决英文显示和后排头像过淡的问题。
- 验证：在工作区内 `.jdk17`、`.gradle-local`、`.temp-local`、`.android-local`、`.localappdata`、`.appdata` 目录下执行 `assembleDebug` 通过。
- 提交：待提交

## 当前判断

- 当前可工作的稳定组合是：Gradle 8.7 + AGP 8.5.2 + Kotlin 1.9.22 + JDK 17。
- `app` 实际启用了 `Booster`、`TheRouter` 和 `Hilt`。
- `SkinApplicationTransformer` 基于 Booster 的 Transform/ASM 链路运行，是升级 AGP 8 的首要阻塞点。
- `TheRouter` 已升级到 `1.3.2`，当前可在 AGP 8 链路下正常工作。
- `com.hjl.plugin` 自定义插件当前在 `app/build.gradle.kts` 中是注释状态，不是当前主阻塞项。
- 显式继承方案本身方向正确，但需要先解决模块分层问题，不能让基础模块依赖 `app`。
- 目前换肤能力已经可由公共基类显式承接，后续可以继续收缩 `WanBaseActivity` 和历史 skin-plugin 产物。
- 旧实现文件已保留，但当前运行链路已优先走显式继承方案。
- 旧 `skin-plugin` 之所以仍出现在构建 classpath，不是因为 `app` 应用了插件，而是因为根脚本依赖的 `custom-plugin:1.0.1` 在本地 Maven 中声明了对 `skin-plugin:1.0.0` 的运行时依赖。
- 当前默认 App 构建图已经不再加载历史插件工程，也不再依赖 `custom-plugin` buildscript classpath；旧源码和本地 Maven 产物仍保留，便于后续单独整理。
- 默认主构建链路中的 Booster 已移除，当前剩余的历史 Transform 相关代码只作为保留实现存在，不再默认参与 App 构建。
- 当前仍然存在的 `android.registerTransform` 警告来自 `TheRouter` 官方插件，而不是项目自定义插件链；但它已不再阻塞升级后的构建与验证。
- 业务层直接依赖 `TheRouter` 的地方已经进一步收口到统一导航入口，目前明确的直接运行时调用主要集中在 `module_base` 的 `RouterNavigator`。
- 当前机器上建议临时使用 ASCII 路径的 `GRADLE_USER_HOME` 进行构建验证，例如 `E:\GradleHome2`；否则默认 `C:\Users\龙\.gradle` 在 daemon 启动阶段存在路径编码问题。
- `module_base` 的 Room/KAPT 还依赖临时目录中的 `sqlitejdbc.dll`，当前机器上需要把 `TEMP/TMP` 指到 ASCII 路径，例如 `E:\Temp`，否则会在 `:module_base:kaptDebugKotlin` 阶段失败。
- 在当前环境下，可改用工作区内 `.gradle-local`、`.temp-local`、`.android-local` 目录并配合 JDK 17 完成稳定构建验证。
- 当前仓库内已经具备 `module_core` 的 Compose 页面能力，可继续用同样方式接入新的独立页面。
- “我的”页里的“积分榜单”已经接到 Compose Activity，当前版本展示榜单首屏、前三高亮和滚动分页加载。
- 当前积分榜页面的可见文案已统一为中文，后续排名头像已切换为更深的高对比配色。
- 当前剩余问题主要是废弃 API、无障碍与文案等告警，不影响编译、测试与 lint 通过。

## 下一步

- 如果继续完善积分榜单，优先补当前登录用户排名、下拉刷新、骨架屏和榜单详情交互。
- 升级闭环本身没有新的阻塞项；工程侧后续仍优先处理废弃 API、`launchWhenStarted/launchWhenResumed` 迁移、`resources.getDrawable` 替换与可访问性告警。
