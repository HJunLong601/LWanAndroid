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
| 阶段 3 | 下沉公共换肤基类并接入多语言钩子 | 已完成 | 在 `commonlib` 新增公共 `SkinBaseActivity` 与 `ActivityDelegateRegistry`，`BaseActivity`/`BaseMultipleActivity` 改为显式继承，`WanApplication` 负责注册多语言钩子 | `commonlib/src/main/java/com/hjl/commonlib/base/ActivityDelegateRegistry.kt`、`commonlib/src/main/java/com/hjl/commonlib/base/SkinBaseActivity.java`、`commonlib/src/main/java/com/hjl/commonlib/base/BaseMultipleActivity.java`、`jetpacklib/src/main/java/com/hjl/jetpacklib/mvvm/view/BaseActivity.kt`、`app/src/main/java/com/hjl/lwanandroid/WanApplication.kt`、`commonlib/build.gradle.kts` | 待提交 |
| 阶段 3 | 停用换肤字节码替换逻辑 | 已完成 | `SkinApplicationTransformer` 已改为 no-op，构建不再执行 Activity 父类替换；`assembleDebug` 通过且 D8 构造函数告警消失 | `buildSrc/build.gradle.kts`、`gradle-plugin/skin-plugin/src/main/kotlin/SkinApplicationTransformer.kt` | 待提交 |
| 阶段 4 | 升级 Gradle/AGP/Kotlin/SDK 基线 | 未开始 | 待处理 | `gradle/wrapper/gradle-wrapper.properties`、`build.gradle.kts`、`buildSrc/src/main/kotlin/VersionConfig.kt` |  |
| 阶段 5 | 升级核心依赖与应用兼容层 | 未开始 | 待处理 | `buildSrc/src/main/kotlin/VersionConfig.kt`、各模块 `build.gradle.kts`、网络/数据库/分页相关代码 |  |
| 阶段 6 | 补最小测试面与回归验证 | 未开始 | 待处理 | `module_core/src/test/**`、`module_base/src/test/**` 等 |  |

## 最近一次执行

- 时间：2026-04-03
- 内容：完成显式继承迁移的第一轮实现，公共换肤基类已下沉到 `commonlib`，多语言钩子已通过注册器接入。
- 验证：执行 `.\gradlew.bat assembleDebug` 成功；`SkinApplicationTransformer` 不再打印和改写类，之前的 D8 构造函数告警已消失。
- 提交：待提交

## 当前判断

- 当前可工作的稳定组合是：Gradle 7.5 + AGP 7.4.2 + Kotlin 1.6.21 + JDK 11。
- `app` 实际启用了 `Booster`、`TheRouter` 和 `Hilt`。
- `SkinApplicationTransformer` 基于 Booster 的 Transform/ASM 链路运行，是升级 AGP 8 的首要阻塞点。
- `TheRouter` 当前可正常工作，但也依赖旧构建链路，需要在升级前单独验证。
- `com.hjl.plugin` 自定义插件当前在 `app/build.gradle.kts` 中是注释状态，不是当前主阻塞项。
- 显式继承方案本身方向正确，但需要先解决模块分层问题，不能让基础模块依赖 `app`。
- 目前换肤能力已经可由公共基类显式承接，后续可以继续收缩 `WanBaseActivity` 和历史 skin-plugin 产物。

## 下一步

- 清理迁移后的历史残留，重点处理 `WanBaseActivity`、`app` 模块中的旧 `SkinBaseActivity`、以及 `skin-plugin`/`buildSrc` 中不再需要的接入点。
- 然后再进入 Gradle/AGP/Kotlin/SDK 基线升级准备，这时阻塞会比之前小很多。
