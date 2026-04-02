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
| 阶段 1 | 修复关键乱码文件 | 已完成 | 修复了关键构建脚本、README 和 Application 注释/文案中的乱码与不可读文本 | `app/build.gradle.kts`、`settings.gradle.kts`、`README.md`、`app/src/main/java/com/hjl/lwanandroid/WanApplication.kt` | 待提交 |
| 阶段 2 | 升级 Gradle/AGP/Kotlin/SDK 基线 | 未开始 | 待处理 | `gradle/wrapper/gradle-wrapper.properties`、`build.gradle.kts`、`buildSrc/src/main/kotlin/VersionConfig.kt` |  |
| 阶段 3 | 验证 Booster/ASM/自定义插件兼容性 | 未开始 | 待处理 | `gradle-plugin/skin-plugin/**`、`plugin/**`、`build.gradle.kts` |  |
| 阶段 4 | 升级核心依赖与应用兼容层 | 未开始 | 待处理 | `buildSrc/src/main/kotlin/VersionConfig.kt`、各模块 `build.gradle.kts`、网络/数据库/分页相关代码 |  |
| 阶段 5 | 补最小测试面与回归验证 | 未开始 | 待处理 | `module_core/src/test/**`、`module_base/src/test/**` 等 |  |

## 最近一次执行

- 时间：2026-04-03
- 内容：完成阶段 1 的第二批清理，修复关键构建脚本、README 和 `WanApplication` 中的乱码与不可读文本。
- 验证：执行 `.\gradlew.bat assembleDebug` 成功。
- 提交：待提交

## 下一步

- 开始阶段 2，升级 Gradle/AGP/Kotlin/SDK 基线，并优先评估 Booster/Transform API 的兼容性风险。
