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
| 阶段 4 | 升级 Gradle/AGP/Kotlin/SDK 基线 | 未开始 | 待处理 | `gradle/wrapper/gradle-wrapper.properties`、`build.gradle.kts`、`buildSrc/src/main/kotlin/VersionConfig.kt` |  |
| 阶段 5 | 升级核心依赖与应用兼容层 | 未开始 | 待处理 | `buildSrc/src/main/kotlin/VersionConfig.kt`、各模块 `build.gradle.kts`、网络/数据库/分页相关代码 |  |
| 阶段 6 | 补最小测试面与回归验证 | 未开始 | 待处理 | `module_core/src/test/**`、`module_base/src/test/**` 等 |  |

## 最近一次执行

- 时间：2026-04-03
- 内容：保留旧实现文件的前提下，固化当前基线对旧 Transform 链路的兼容开关，并定位仍生效的旧插件接入点。
- 验证：使用 `GRADLE_USER_HOME=E:\GradleHome2` 执行 `.\gradlew.bat assembleDebug` 通过；默认用户目录包含中文路径时，Gradle 7.5 daemon 在当前 JDK 11/GBK 环境下存在启动不稳定问题。
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
- 现阶段不能直接删除根脚本里的 `custom-plugin` classpath；这样会触发 AGP/Gradle 类加载约束错误，需要等后续拆除其历史发布链路时一起处理。
- 当前机器上建议临时使用 ASCII 路径的 `GRADLE_USER_HOME` 进行构建验证，例如 `E:\GradleHome2`；否则默认 `C:\Users\龙\.gradle` 在 daemon 启动阶段存在路径编码问题。

## 下一步

- 在保留旧实现文件的前提下，继续清理“仍然生效的旧接入点”，优先评估如何让 `custom-plugin` 保留源码和产物但不再进入主工程 buildscript classpath。
- 然后再进入 Gradle/AGP/Kotlin/SDK 基线升级准备，这时阻塞会比之前小很多。
