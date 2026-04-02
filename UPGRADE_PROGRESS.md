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
| 阶段 4 | 升级 Gradle/AGP/Kotlin/SDK 基线 | 未开始 | 待处理 | `gradle/wrapper/gradle-wrapper.properties`、`build.gradle.kts`、`buildSrc/src/main/kotlin/VersionConfig.kt` |  |
| 阶段 5 | 升级核心依赖与应用兼容层 | 未开始 | 待处理 | `buildSrc/src/main/kotlin/VersionConfig.kt`、各模块 `build.gradle.kts`、网络/数据库/分页相关代码 |  |
| 阶段 6 | 补最小测试面与回归验证 | 未开始 | 待处理 | `module_core/src/test/**`、`module_base/src/test/**` 等 |  |

## 最近一次执行

- 时间：2026-04-03
- 内容：从默认构建链路里移除 Booster，继续收口旧 Transform 入口，同时保留历史插件源码和本地 Maven 产物。
- 验证：使用 `GRADLE_USER_HOME=E:\GradleHome2`、`TEMP=E:\Temp`、`TMP=E:\Temp` 执行 `.\gradlew.bat --no-daemon assembleDebug` 通过；Booster 已退出默认构建链路，但 `TheRouter` 仍会触发 `registerTransform` 警告。
- 提交：待提交

## 当前判断

- 当前可工作的稳定组合是：Gradle 7.5 + AGP 7.4.2 + Kotlin 1.6.21 + JDK 11。
- `app` 实际启用了 `Booster`、`TheRouter` 和 `Hilt`。
- `SkinApplicationTransformer` 基于 Booster 的 Transform/ASM 链路运行，是升级 AGP 8 的首要阻塞点。
- `TheRouter` 当前可正常工作，但也依赖旧构建链路，需要在升级前单独验证。
- `com.hjl.plugin` 自定义插件当前在 `app/build.gradle.kts` 中是注释状态，不是当前主阻塞项。
- 显式继承方案本身方向正确，但需要先解决模块分层问题，不能让基础模块依赖 `app`。
- 目前换肤能力已经可由公共基类显式承接，后续可以继续收缩 `WanBaseActivity` 和历史 skin-plugin 产物。
- 旧实现文件已保留，但当前运行链路已优先走显式继承方案。
- 旧 `skin-plugin` 之所以仍出现在构建 classpath，不是因为 `app` 应用了插件，而是因为根脚本依赖的 `custom-plugin:1.0.1` 在本地 Maven 中声明了对 `skin-plugin:1.0.0` 的运行时依赖。
- 当前默认 App 构建图已经不再加载历史插件工程，也不再依赖 `custom-plugin` buildscript classpath；旧源码和本地 Maven 产物仍保留，便于后续单独整理。
- 默认主构建链路中的 Booster 已移除，当前剩余的历史 Transform 相关代码只作为保留实现存在，不再默认参与 App 构建。
- 当前仍然存在的 `android.registerTransform` 警告来自 `TheRouter` 官方插件，而不是项目自定义插件链。
- `TheRouter` 在当前项目里仍承担页面路由能力，短期内不能像 Booster 一样直接移除，后续升级 AGP 8 前需要优先确认其可升级版本或替代方案。
- 当前机器上建议临时使用 ASCII 路径的 `GRADLE_USER_HOME` 进行构建验证，例如 `E:\GradleHome2`；否则默认 `C:\Users\龙\.gradle` 在 daemon 启动阶段存在路径编码问题。
- `module_base` 的 Room/KAPT 还依赖临时目录中的 `sqlitejdbc.dll`，当前机器上需要把 `TEMP/TMP` 指到 ASCII 路径，例如 `E:\Temp`，否则会在 `:module_base:kaptDebugKotlin` 阶段失败。

## 下一步

- 在保留旧实现文件的前提下，开始进入 Gradle/AGP/Kotlin/SDK 基线升级准备，优先评估 `TheRouter` 从 1.2.1 升级到兼容 AGP 8 的版本路径。
- 然后再进入 Gradle/AGP/Kotlin/SDK 基线升级准备，这时阻塞会比之前小很多。
