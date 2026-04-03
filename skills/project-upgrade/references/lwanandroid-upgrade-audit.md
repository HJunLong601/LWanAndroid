# LWanAndroid 升级审计

## 当前架构摘要

- 工程是 Android 多模块项目，入口模块为 `app`，核心业务在 `module_core`
  ，基础能力分布在 `module_base`、`commonlib`、`jetpacklib`。
- 功能模块单独拆分为 `module_func:func_skin` 和 `module_func:func_language`。
- 构建链路除常规 Android/Kotlin/Hilt 外，还接入了 Booster、TheRouter 和自定义 Gradle/ASM 插件。
- `app` 主要负责壳工程装配，`WanApplication` 中初始化了 Hilt、多语言、换肤、LiveEventBus 和 Room。
- 业务主链路基本遵循 `ViewModel -> Repository -> Retrofit/PagingSource`。

## 值得保留的点

- 模块划分清楚，`app` 没有堆积全部业务代码。
- MVVM、Repository、Paging 的主链路基本统一，利于后续渐进式重构。
- 换肤和语言能力被拆为独立模块，具备组件化意识。
- 自定义插件和构建逻辑已经独立出来，后续可单独演进或下线。

## 主要风险

### P0

- 仓库中存在签名密码和签名文件路径配置，位于 `gradle.properties`。
- 该问题属于明确的凭据泄露风险，应立即迁出仓库并更换密钥。

### P1

- Android 基线偏老：`compileSdk=31`、`targetSdk=30`、`buildToolsVersion=30.0.3`，Kotlin 为 `1.6.21`。
- 核心依赖版本较老，包括 Retrofit、OkHttp、Room、Paging、Lifecycle、Material 等。
- 升级到更高 `targetSdk` 后，权限、存储、导出组件、后台行为等兼容问题会集中暴露。

### P1

- Manifest 和运行时权限模型包含旧权限和重复声明，尤其是外部存储和网络相关权限。
- 当目标版本提升后，`WRITE_EXTERNAL_STORAGE` 相关逻辑会失效或需要替换。

### P1

- 存在自定义 Booster/ASM 改写逻辑，用于将基础 Activity 替换到换肤基类。
- 这类构建期改写对 AGP、字节码结构和 classpath 兼容性敏感，是升级过程中的高风险点。

### P2

- 多个 Gradle/Kotlin/README 文件存在乱码或编码不一致现象。
- 文案、注释和占位字符串的可维护性已经受影响，后续升级时容易误判配置含义。

### P2

- Room 单例、基础异常封装和若干基础库写法偏旧，可工作但扩展性与稳健性一般。

### P2

- 测试覆盖接近空白，业务模块缺少有效的单元测试和回归保护。
- 没有测试保护时，依赖升级和权限改造的回归成本会明显上升。

## 已确认的项目事实

- 模块声明位于 `settings.gradle.kts`，包含 `app`、`commonlib`、`module_base`、`module_core`、`jetpacklib`
  、功能模块、插件模块和皮肤模块。
- 版本集中管理位于 `buildSrc/src/main/kotlin/VersionConfig.kt`。
- `app/build.gradle.kts` 中启用了 Booster、TheRouter、Hilt，且通过 `isEnableSkin` 决定是否装配换肤功能。
- `gradle.properties`
  中存在 `RELEASE_KEY_PASSWORD`、`RELEASE_KEY_ALIAS`、`RELEASE_STORE_PASSWORD`、`RELEASE_STORE_FILE`。
- `gradle-plugin/skin-plugin` 中存在 `SkinApplicationTransformer`，使用 ASM
  改写父类和部分 `INVOKESPECIAL` 调用。

## 升级计划

### 第一阶段：止血和清理

- 从仓库移除签名密码、签名文件路径和其他敏感配置，改为本地私有配置或 CI Secret。
- 统一文件编码为 UTF-8，清理关键 Gradle、Manifest、README 和 Kotlin 文件中的乱码。
- 梳理并去重 Manifest 权限，删除明显过时或重复的声明。
- 盘点 `localMaven`、自定义插件和 Booster 的实际使用范围，确认是否必须保留。

### 第二阶段：建立升级基线

- 升级 Gradle Wrapper、AGP、Kotlin、JDK 目标版本，先让构建链路进入可维护区间。
- 同步调整 `compileSdk`、`targetSdk`、`buildToolsVersion` 和相关 AndroidX 依赖。
- 对自定义插件做兼容性验证，优先确认 ASM 逻辑是否仍可用。

### 第三阶段：应用层兼容改造

- 重构权限申请逻辑，按新 Android 存储和运行时权限模型处理。
- 检查 Activity、Service、Receiver、Provider 的导出属性和行为变化。
- 检查 WebView、文件读写、通知、定位等与高版本系统耦合的能力。

### 第四阶段：库和架构升级

- 分批升级网络、数据库、分页、Material、Lifecycle、Hilt 等核心依赖。
- 逐步替换陈旧封装，减少对历史兼容写法和全局工具类的依赖。
- 评估自定义插件是否继续维护，或改为更低风险的实现方式。

### 第五阶段：回归保护

- 为关键 Repository、PagingSource、数据库访问和权限分支补最小测试面。
- 对启动链路、首页、登录、收藏、换肤、多语言等核心流程执行人工回归。
- 在每个阶段结束时保留可构建、可运行、可回退的中间状态。

## 执行顺序建议

1. 先处理密钥与编码问题。
2. 再升级构建链路和 SDK 基线。
3. 然后解决权限和高版本 Android 兼容。
4. 再推进库升级和历史封装收敛。
5. 最后补测试并稳定回归流程。

## 本次审计边界

- 本文档基于静态代码和构建脚本检查形成。
- 本次没有实际执行 `gradle build`、单元测试或真机验证。
