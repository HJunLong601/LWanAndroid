---
name: project-upgrade
description: Analyze Android/Kotlin engineering status, identify upgrade blockers, and produce phased upgrade plans for multi-module projects. Use when Codex needs to assess technical debt, dependency age, SDK targets, build tooling, permissions, tests, security configuration, or modernization work for an Android project, especially LWanAndroid.
---

# Project Upgrade

Use this skill to turn a codebase scan into an actionable upgrade plan.

## Workflow

1. Read the root build files first.
   Check `settings.gradle*`, root `build.gradle*`, `gradle.properties`, version catalogs or central
   version files, and module `build.gradle*` files.
2. Map the module graph.
   Identify the app shell, feature modules, base libraries, plugins, build logic, and optional
   modules.
3. Confirm the runtime entry path.
   Read `Application`, main activities, shared base classes, network/bootstrap code, and persistence
   setup.
4. Evaluate upgrade posture.
   Focus on:
    - `compileSdk`, `targetSdk`, AGP, Gradle, Kotlin, Java compatibility
    - major library age and migration pressure
    - Android permissions and storage model compatibility
    - signing and secret handling
    - custom Gradle plugins, ASM transforms, and local Maven artifacts
    - test coverage and refactor safety
    - encoding issues or broken resource text
5. Write findings as ordered risks.
   Prioritize security, build break risk, platform compatibility, then maintainability.
6. Produce a phased plan.
   Split into:
    - immediate cleanup
    - baseline upgrade
    - framework/library upgrade
    - behavior verification and regression coverage

## Output Shape

Return:

- current architecture summary
- strengths worth preserving
- risk list ordered by severity
- phased upgrade plan with dependencies between steps
- explicit note if the analysis is static only and no build/test was run

## LWanAndroid Notes

When working in this repository,
read [references/lwanandroid-upgrade-audit.md](./references/lwanandroid-upgrade-audit.md) first.
Treat it as project context captured from a prior scan, then refresh it only if new facts are
discovered.

Pay extra attention to:

- old Android baseline and library versions
- signing secrets committed in repo
- permission model drift for modern Android
- custom skin plugin and ASM transform compatibility
- garbled text caused by encoding inconsistencies
- low test coverage before refactors
