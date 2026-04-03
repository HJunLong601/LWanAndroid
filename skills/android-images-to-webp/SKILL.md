---
name: android-images-to-webp
description: 批量把 Android 工程中的 PNG、JPG、JPEG 图片资源转换为 WebP，并保留同名资源名以避免代码引用变更。用于需要批量瘦身图片资源、统一图片格式、或执行 Android 资源压缩治理时。默认跳过启动图标、`mipmap*/` 目录、`.9.png` 图片，以及常见构建输出目录。
---

# Android Images To Webp

## 概览

使用 `scripts/convert_android_images_to_webp.py` 扫描 Android 工程图片资源并原地转换为 `.webp`。

默认规则：

- 转换 `.png`、`.jpg`、`.jpeg`
- 跳过 `.9.png`
- 跳过 `mipmap*/` 目录
- 跳过常见启动图标文件，如 `ic_launcher*`
- 跳过 `build/`、`.git/`、`.gradle/`、`localMaven/`、`skills/` 等目录

## 工作流

1. 先执行 dry-run，确认会转换哪些文件。
2. 再执行真实转换，默认会删除原始图片并保留同名 `.webp`。
3. 转换完成后执行工程编译验证，例如 `gradlew assembleDebug`。
4. 检查 Git diff，确认没有误伤启动图标、`.9` 图片和非资源文件。

## 快速开始

在工程根目录执行：

```powershell
python skills\android-images-to-webp\scripts\convert_android_images_to_webp.py . --dry-run
python skills\android-images-to-webp\scripts\convert_android_images_to_webp.py .
```

常用参数：

```powershell
python skills\android-images-to-webp\scripts\convert_android_images_to_webp.py . --quality 88
python skills\android-images-to-webp\scripts\convert_android_images_to_webp.py . --keep-originals
python skills\android-images-to-webp\scripts\convert_android_images_to_webp.py . --skip-if-larger
python skills\android-images-to-webp\scripts\convert_android_images_to_webp.py . --include-dir app\src\main\res
```

## 行为约束

- JPG/JPEG 一律按有损 WebP 输出，质量默认 `90`。
- PNG 小于 `20KB` 时默认按无损 WebP 输出，尽量避免小图标视觉回退。
- PNG 大于等于 `20KB` 时默认按有损 WebP 输出，质量默认 `90`。
- 转换后资源名保持不变，例如 `foo.png` 会变成 `foo.webp`，`R.drawable.foo` 不需要改。
- 如果用户明确要求保留原图，使用 `--keep-originals`。
- 如果用户担心个别图片转成 WebP 后变大，使用 `--skip-if-larger`。
- 如果需要调整 PNG 无损阈值，使用 `--png-lossless-threshold-kb`。

示例：

```powershell
python skills\android-images-to-webp\scripts\convert_android_images_to_webp.py . --png-lossless-threshold-kb 16
```

## 脚本

脚本路径：

`skills/android-images-to-webp/scripts/convert_android_images_to_webp.py`

如果环境没有 Pillow，先安装：

```powershell
python -m pip install pillow
```
