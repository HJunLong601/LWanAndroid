# AGENTS.md - Agent 编码指南 for LWanAndroid

## 项目概述

LWanAndroid 是一款基于 WanAndroid 开放 API 的 Android 应用，采用 MVVM 架构和组件化模块化设计，使用
Jetpack 库、Kotlin 以及各种流行框架构建。

## 构建命令

### Gradle Wrapper

```bash
./gradlew          # Unix/Linux/Mac
gradlew.bat        # Windows
```

### 构建任务

```bash
./gradlew assembleDebug          # 构建调试版 APK
./gradlew assembleRelease        # 构建正式版 APK
./gradlew build                  # 完整构建（调试版 + 正式版）
./gradlew clean                  # 清理构建产物
```

### 运行测试

```bash
./gradlew test                   # 运行所有单元测试
./gradlew test --tests "ClassName"           # 运行指定的测试类
./gradlew test --tests "ClassName.methodName"  # 运行指定的测试方法
./gradlew :module_name:test   # 运行指定模块的测试（如 :app:test, :jetpacklib:test）
```

### 代码检查

```bash
./gradlew lint                   # 运行 Android lint 分析
./gradlew lintRelease            # 对正式版进行 lint 检查
./gradlew lintDebug              # 对调试版进行 lint 检查
```

### 其他常用命令

```bash
./gradlew dependencies           # 显示项目依赖
./gradlew tasks                  # 列出所有可用任务
./gradlew signingReport          # 显示签名信息
```

---

## 代码风格指南

### 语言与框架

- **语言**：Kotlin（主要），Java（少量使用）
- **最低 SDK**：查看 version.gradle（通常为 21+）
- **目标 SDK**：查看 version.gradle
- **架构**：MVVM + 清洁架构原则

### 命名规范

| 元素 | 规范               | 示例                                             |
|----|------------------|------------------------------------------------|
| 类名 | PascalCase       | `BaseViewModel`，`WanApplication`               |
| 函数 | camelCase        | `initSkinSupport()`，`launch()`                 |
| 变量 | camelCase        | `val TAG`，`var attachBaseContext`              |
| 常量 | UPPER_SNAKE_CASE | `companion object { const val KEY = "value" }` |
| 包名 | 小写               | `com.hjl.lwanandroid`                          |

### 导入组织

```kotlin
// 标准库导入
import android.content.Context

// AndroidX 导入
import androidx.lifecycle.ViewModel

// 第三方导入
import com.hjl.commonlib.utils.LogUtils
import com.jeremyliao.liveeventbus.core.LiveEventBusCore

// 项目导入（按模块分组）
import com.hjl.lwanandroid.skin.SkinResourceAcquirer
```

### 格式化规则

1. **缩进**：4 个空格（不使用 Tab）
2. **行长度**：建议最多 120 个字符
3. **大括号**：函数/类在同一行开始
4. **空行**：逻辑部分之间使用单个空行
5. **导入**：按字母顺序排列，组间用空行分隔

### Kotlin 特定规范

#### 协程与异步

```kotlin
// 在 ViewModel 中使用 viewModelScope
viewModelScope.launch {
    withContext(Dispatchers.IO) {
        // 网络/数据库操作
    }
}

// 正确处理异常
try {
    // 操作
} catch (e: Throwable) {
    // 处理错误
}
```

#### 数据绑定

- 在 `build.gradle.kts` 中启用：`dataBinding { enable = true }`
- 在 Activities/Fragments 中使用 `binding` 属性
- 布局文件使用 `<layout>` 根元素

#### 依赖注入

- 使用 **Hilt**（`@HiltAndroidApp`，`@AndroidEntryPoint`，`@HiltViewModel`）
- ViewModel：`@HiltViewModel` + `dagger.hilt.android.lifecycle.HiltViewModel`

### 类结构（推荐顺序）

1. 包声明
2. 导入
3. 类声明及注解
4. 伴生对象（常量等）
5. 属性（val/var）
6. Init 块
7. 次构造函数
8. 函数（生命周期方法优先，然后公开方法，最后私有方法）

### XML 资源

- 布局文件：`activity_*.xml`、`fragment_*.xml`、`item_*.xml`
- ID 命名：使用有意义的 camelCase（如 `tvTitle`、`rvList`）
- 字符串资源：所有用户可见文本使用 `strings.xml`

### 错误处理

```kotlin
// 使用自定义异常处理
try {
    withContext(Dispatchers.IO) {
        request()
    }
} catch (e: Throwable) {
    withContext(Dispatchers.Main) {
        val exception = ExceptionHandler.handle(e)
        fail(exception)
    }
}
```

### 注释

- 类和公开函数使用 KDoc 风格
- 类需包含作者、描述和日期
- 待办事项使用 TODO 注释：`// TODO: 描述`

---

## 项目结构

```
app/
├── src/main/
│   ├── java/com/hjl/lwanandroid/
│   │   ├── WanApplication.kt
│   │   ├── WanBaseActivity.kt
│   │   └── skin/
│   └── res/
├── build.gradle.kts

module_core/      # 核心功能模块
module_base/      # 基础/数据库层
commonlib/        # 通用工具类
jetpacklib/       # Jetpack 组件
module_func/      # 功能模块（换肤、语言）
gradle-plugin/    # 自定义 Gradle 插件
```

---

## 测试指南

### 单元测试位置

- 单元测试：`src/test/java/`
- Android 测试：`src/androidTest/java/`

### 测试框架

- JUnit 4（标准）
- Kotlin Test

### 编写测试

```kotlin
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}
```

---

## 常见问题与解决方案

1. **构建失败**：首先运行 `./gradlew clean`
2. **依赖冲突**：查看 `version.gradle` 进行集中版本管理
3. **Hilt 错误**：确保 `kapt` 配置了 `correctErrorTypes = true`
4. **DataBinding 问题**：验证 XML 中的 `<layout>` 根元素和正确的绑定导入

---

## 关键依赖（来自 build.gradle.kts）

- Kotlin Gradle Plugin
- Android Gradle Plugin (7.4.2)
- Hilt (2.44)
- Booster (4.16.3)
- TheRouter (1.2.1)
- 自定义插件

---

## 补充说明

- 此项目是 **Android Kotlin 项目**，非 JavaScript 或 Web 开发
- 使用 **阿里云镜像** 下载依赖（访问外部仓库可能需要 VPN）
- 多模块项目，使用 Gradle Kotlin DSL
- **所有思考和回答必须使用中文**
- **新增或修改的代码注释必须使用中文**
- **Git 提交信息必须使用中文**
- **所有新增页面必须继承项目基类；Compose 页面统一继承 `BaseComposeActivity`，不得直接继承系统 `Activity`/`ComponentActivity`**
- 实现新功能时请遵循现有代码模式
