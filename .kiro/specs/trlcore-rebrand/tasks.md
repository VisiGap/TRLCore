# Implementation Plan: TRLCore Rebrand

## Overview

本实现计划将 Purpur 项目全面改名为 TRLCore，按照依赖顺序执行，确保每一步都能正确构建。

## Tasks

- [x] 1. 更新 Gradle 配置文件
  - [x] 1.1 更新 `settings.gradle.kts`
    - 将 `rootProject.name` 从 `"purpur"` 改为 `"trlcore"`
    - 将模块引用从 `purpur-api`, `purpur-server` 改为 `trlcore-api`, `trlcore-server`
    - _Requirements: 2.1, 2.2_

  - [x] 1.2 更新 `gradle.properties`
    - 将 `group` 从 `org.purpurmc.purpur` 改为 `org.trlcore.trlcore`
    - 添加 GitHub Packages 认证配置注释
    - _Requirements: 2.3, 11.2, 11.3_

  - [x] 1.3 更新 `build.gradle.kts`
    - 将所有 `purpur-api`, `purpur-server` 引用改为 `trlcore-api`, `trlcore-server`
    - 将 `printPurpurVersion` 任务改为 `printTRLCoreVersion`
    - 添加 GitHub Packages 发布配置
    - _Requirements: 2.4, 2.5, 11.1, 11.4_

- [x] 2. 重命名目录结构
  - [x] 2.1 重命名 `purpur-api` 目录为 `trlcore-api`
    - _Requirements: 1.1, 1.3_

  - [x] 2.2 重命名 `purpur-server` 目录为 `trlcore-server`
    - _Requirements: 1.2, 1.3_

- [-] 3. 更新 Patch 文件中的路径引用
  - [x] 3.1 更新 `trlcore-api/build.gradle.kts.patch`
    - 更新所有 `purpur` 相关引用为 `trlcore`
    - _Requirements: 2.6_

  - [x] 3.2 更新 `trlcore-server/build.gradle.kts.patch`
    - 更新所有 `purpur` 相关引用为 `trlcore`
    - _Requirements: 2.6_

  - [x] 3.3 更新 `trlcore-api/paper-patches/` 下的所有 patch 文件
    - 更新路径引用和包名引用
    - _Requirements: 6.2, 6.3_

  - [ ] 3.4 更新 `trlcore-server/paper-patches/` 下的所有 patch 文件
    - 更新路径引用和包名引用
    - _Requirements: 6.2, 6.3_

  - [ ] 3.5 更新 `trlcore-server/minecraft-patches/` 下的所有 patch 文件
    - 更新路径引用、包名引用和品牌标识
    - 更新 `BRAND_PURPUR_ID` 为 `BRAND_TRLCORE_ID`
    - _Requirements: 6.1, 6.2, 6.3, 6.4_

- [ ] 4. Checkpoint - 验证 Patch 应用
  - 执行 `./gradlew applyAllPatches` 确保 patch 能正确应用
  - 如有问题，检查路径引用是否完整更新

- [ ] 5. 迁移 Java 包名和更新类名
  - [ ] 5.1 迁移 `trlcore-api/src` 下的 Java 包结构
    - 将 `org/purpurmc/purpur` 目录结构改为 `org/trlcore/trlcore`
    - 更新所有 `package` 声明
    - 更新所有 `import` 语句
    - 重命名 `PurpurPermissions.java` 为 `TRLCorePermissions.java`
    - _Requirements: 3.1, 3.2, 3.3, 3.4, 5.4_

  - [ ] 5.2 迁移 `trlcore-server/src` 下的 Java 包结构
    - 将 `org/purpurmc/purpur` 目录结构改为 `org/trlcore/trlcore`
    - 更新所有 `package` 声明
    - 更新所有 `import` 语句
    - 重命名以下类文件：
      - `PurpurConfig.java` → `TRLCoreConfig.java`
      - `PurpurWorldConfig.java` → `TRLCoreWorldConfig.java`
      - `PurpurCommand.java` → `TRLCoreCommand.java`
      - `PurpurStoredBee.java` → `TRLCoreStoredBee.java`
    - 更新所有对旧类名的引用
    - _Requirements: 3.1, 3.2, 3.3, 3.4, 3.5, 5.1, 5.2, 5.3, 5.5, 5.6_

  - [ ] 5.3 更新配置文件引用
    - 将 `purpur.yml` 引用改为 `trlcore.yml`
    - 将配置节名 `purpur` 改为 `trlcore`
    - _Requirements: 7.1, 7.2, 7.3_

- [ ] 6. 更新品牌标识
  - [ ] 6.1 更新 JAR Manifest 属性
    - 在相关 patch 文件中更新：
      - `Implementation-Title: TRLCore`
      - `Specification-Title: TRLCore`
      - `Specification-Vendor: TRLCore Team`
      - `Brand-Id: trlcore:trlcore`
      - `Brand-Name: TRLCore`
    - _Requirements: 4.1, 4.2, 4.3, 4.4, 4.5_

  - [ ] 6.2 更新 ServerBuildInfo 相关 patch
    - 添加 `BRAND_TRLCORE_ID` 常量
    - _Requirements: 4.6_

- [ ] 7. Checkpoint - 验证构建
  - 执行 `./gradlew build` 确保项目能正确构建
  - 如有问题，检查类名引用和包名是否完整更新

- [ ] 8. 更新测试插件
  - [ ] 8.1 迁移 `test-plugin/src` 下的 Java 包结构
    - 将 `org/purpurmc/testplugin` 改为 `org/trlcore/testplugin`
    - 更新所有 `package` 声明和 `import` 语句
    - _Requirements: 9.1, 9.2, 9.3_

  - [ ] 8.2 更新 `test-plugin.settings.gradle.kts`
    - 更新任何 Purpur 相关引用
    - _Requirements: 9.3_

- [ ] 9. 更新文档
  - [ ] 9.1 更新 `README.md`
    - 替换所有 Purpur 引用为 TRLCore
    - 更新依赖坐标为 `org.trlcore.trlcore:trlcore-api`
    - 更新仓库 URL 为 GitHub Packages
    - 添加 GitHub Packages 认证说明
    - _Requirements: 8.1, 8.2, 8.3, 8.4_

  - [ ] 9.2 更新 `CONTRIBUTING.md`
    - 替换所有 Purpur 引用为 TRLCore
    - _Requirements: 8.5_

- [ ] 10. 创建 GitHub Actions 发布工作流
  - [ ] 10.1 创建 `.github/workflows/publish.yml`
    - 配置在 release 创建时触发
    - 配置 JDK 21 和 Gradle
    - 配置 GitHub Packages 发布
    - _Requirements: 11.5_

- [ ] 11. 最终验证
  - [ ] 11.1 验证无旧命名残留
    - 执行 `grep -r "org.purpurmc.purpur" trlcore-api/src trlcore-server/src --include="*.java"` 确保无结果
    - 执行 `grep -r "purpur-api\|purpur-server" trlcore-*/paper-patches trlcore-server/minecraft-patches --include="*.patch"` 检查路径引用
    - _Requirements: 3.5, Property 1, Property 2_

  - [ ] 11.2 执行完整构建验证
    - 执行 `./gradlew clean`
    - 执行 `./gradlew applyAllPatches`
    - 执行 `./gradlew build`
    - _Requirements: 10.1, 10.2, Property 3_

## Notes

- 任务按依赖顺序排列，必须按顺序执行
- Checkpoint 任务用于验证阶段性成果，确保后续步骤能正确执行
- 如果构建失败，需要回溯检查前面步骤的完整性
- Patch 文件中的 `// Purpur` 注释可以保留作为历史记录，但路径和包名引用必须更新
