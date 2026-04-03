from __future__ import annotations

import argparse
import sys
from dataclasses import dataclass
from pathlib import Path

try:
    from PIL import Image
except ModuleNotFoundError as exc:
    raise SystemExit(
        "缺少 Pillow，请先执行: python -m pip install pillow"
    ) from exc


IMAGE_SUFFIXES = {".png", ".jpg", ".jpeg"}
LOSSLESS_PNG_THRESHOLD_BYTES = 20 * 1024
DEFAULT_EXCLUDE_DIRS = {
    ".git",
    ".gradle",
    ".idea",
    "build",
    "out",
    "localMaven",
    "skills",
    ".android-local",
    ".gradle-local",
    ".jdk17",
    ".localappdata",
    ".temp-local",
}
LAUNCHER_PREFIXES = (
    "ic_launcher",
    "ic_round_launcher",
    "launcher",
    "round_launcher",
)


@dataclass
class ConvertResult:
    converted: int = 0
    skipped: int = 0
    kept_originals: int = 0


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="把 Android 工程中的 PNG/JPG 资源批量转换为 WebP。"
    )
    parser.add_argument("root", type=Path, help="待扫描的工程根目录")
    parser.add_argument(
        "--quality",
        type=int,
        default=90,
        help="有损 WebP 的质量，默认 90",
    )
    parser.add_argument(
        "--dry-run",
        action="store_true",
        help="只打印会转换的文件，不实际写入",
    )
    parser.add_argument(
        "--keep-originals",
        action="store_true",
        help="保留原始 PNG/JPG 文件",
    )
    parser.add_argument(
        "--skip-if-larger",
        action="store_true",
        help="如果生成的 WebP 更大，则保留原图并跳过替换",
    )
    parser.add_argument(
        "--png-lossless-threshold-kb",
        type=int,
        default=20,
        help="PNG 小于该阈值时使用无损 WebP，默认 20KB",
    )
    parser.add_argument(
        "--include-dir",
        action="append",
        default=[],
        help="只转换指定子目录，可重复传入，例如 app\\src\\main\\res",
    )
    return parser.parse_args()


def should_skip_dir(path: Path) -> bool:
    return any(part in DEFAULT_EXCLUDE_DIRS for part in path.parts)


def is_launcher_icon(path: Path) -> bool:
    parent_name = path.parent.name.lower()
    file_name = path.name.lower()
    stem = file_name
    if file_name.endswith(".9.png"):
        stem = file_name[:-6]
    else:
        stem = path.stem.lower()
    if parent_name.startswith("mipmap"):
        return True
    return any(stem.startswith(prefix) for prefix in LAUNCHER_PREFIXES)


def should_skip_file(path: Path) -> bool:
    name = path.name.lower()
    if path.suffix.lower() not in IMAGE_SUFFIXES:
        return True
    if name.endswith(".9.png"):
        return True
    if should_skip_dir(path):
        return True
    if is_launcher_icon(path):
        return True
    return False


def should_include_path(path: Path, include_dirs: list[Path]) -> bool:
    if not include_dirs:
        return True
    return any(include_dir in path.parents or path == include_dir for include_dir in include_dirs)


def iter_images(root: Path, include_dirs: list[Path]) -> list[Path]:
    files: list[Path] = []
    for path in root.rglob("*"):
        if not path.is_file():
            continue
        if not should_include_path(path, include_dirs):
            continue
        if should_skip_file(path):
            continue
        files.append(path)
    return sorted(files)


def convert_image(
    source: Path,
    quality: int,
    dry_run: bool,
    keep_originals: bool,
    skip_if_larger: bool,
    png_lossless_threshold_bytes: int,
) -> tuple[bool, bool]:
    target = source.with_suffix(".webp")
    if dry_run:
        print(f"[DRY-RUN] {source} -> {target}")
        return False, False

    with Image.open(source) as image:
        save_args = {"format": "WEBP", "method": 6}
        if source.suffix.lower() == ".png":
            if source.stat().st_size < png_lossless_threshold_bytes:
                save_args["lossless"] = True
            else:
                save_args["quality"] = quality
        else:
            save_args["quality"] = quality
        image.save(target, **save_args)

    if skip_if_larger and target.stat().st_size >= source.stat().st_size:
        target.unlink(missing_ok=True)
        print(f"[SKIP-LARGER] {source}")
        return False, False

    if not keep_originals:
        source.unlink()
        return True, False

    return True, True


def main() -> int:
    args = parse_args()
    root = args.root.resolve()
    if not root.exists():
        print(f"目录不存在: {root}", file=sys.stderr)
        return 1

    include_dirs = [(root / item).resolve() for item in args.include_dir]
    files = iter_images(root, include_dirs)
    result = ConvertResult()

    if not files:
        print("没有找到可转换的 PNG/JPG/JPEG 文件。")
        return 0

    for file_path in files:
        try:
            converted, kept_original = convert_image(
                source=file_path,
                quality=args.quality,
                dry_run=args.dry_run,
                keep_originals=args.keep_originals,
                skip_if_larger=args.skip_if_larger,
                png_lossless_threshold_bytes=args.png_lossless_threshold_kb * 1024,
            )
        except Exception as exc:  # noqa: BLE001
            result.skipped += 1
            print(f"[ERROR] {file_path}: {exc}", file=sys.stderr)
            continue

        if args.dry_run:
            continue
        if converted:
            result.converted += 1
            if kept_original:
                result.kept_originals += 1
            print(f"[OK] {file_path}")
        else:
            result.skipped += 1

    if args.dry_run:
        print(f"Dry-run 完成，共命中 {len(files)} 个文件。")
        return 0

    print(
        "转换完成："
        f" converted={result.converted},"
        f" skipped={result.skipped},"
        f" kept_originals={result.kept_originals}"
    )
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
