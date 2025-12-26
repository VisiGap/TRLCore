# Requirements Document

## Introduction

本文档定义了将 Purpur 项目全面改名为 TRLCore 的需求规范。改名涉及项目目录结构、Gradle 配置、Java 包名、品牌标识、文档等多个层面的修改。

## Glossary

- **TRLCore**: 新的项目名称，用于替换所有 Purpur 相关命名
- **Rebrand_System**: 负责执行品牌重塑的系统组件
- **Package_Migrator**: 负责 Java 包名迁移的工具
- **Config_Updater**: 负责更新 Gradle 和项目配置的组件
- **Brand_Identifier**: 项目的品牌标识符（如 `trlcore:trlcore`）

## Requirements

### Requirement 1: 目录结构重命名

**User Story:** As a developer, I want to rename all Purpur-related directories to TRLCore, so that the project structure reflects the new brand identity.

#### Acceptance Criteria

1. WHEN the Rebrand_System executes, THE directory `purpur-api` SHALL be renamed to `trlcore-api`
2. WHEN the Rebrand_System executes, THE directory `purpur-server` SHALL be renamed to `trlcore-server`
3. WHEN directories are renamed, THE internal subdirectory structure SHALL remain unchanged
4. IF a directory rename fails, THEN THE Rebrand_System SHALL log the error and halt execution

### Requirement 2: Gradle 配置更新

**User Story:** As a developer, I want to update all Gradle configuration files to use TRLCore naming, so that the build system correctly references the new project name.

#### Acceptance Criteria

1. WHEN updating `settings.gradle.kts`, THE Rebrand_System SHALL change `rootProject.name` from `"purpur"` to `"trlcore"`
2. WHEN updating `settings.gradle.kts`, THE Rebrand_System SHALL update module includes from `purpur-api`, `purpur-server` to `trlcore-api`, `trlcore-server`
3. WHEN updating `gradle.properties`, THE Rebrand_System SHALL change `group` from `org.purpurmc.purpur` to `org.trlcore.trlcore`
4. WHEN updating `build.gradle.kts`, THE Rebrand_System SHALL update all references from `purpur-api`, `purpur-server` to `trlcore-api`, `trlcore-server`
5. WHEN updating `build.gradle.kts`, THE Rebrand_System SHALL update `printPurpurVersion` task to `printTRLCoreVersion`
6. WHEN updating patch files, THE Rebrand_System SHALL update all `purpur-api`, `purpur-server` references to `trlcore-api`, `trlcore-server`

### Requirement 3: Java 包名迁移

**User Story:** As a developer, I want to migrate all Java package names from `org.purpurmc.purpur` to `org.trlcore.trlcore`, so that the codebase reflects the new brand identity.

#### Acceptance Criteria

1. WHEN the Package_Migrator executes, THE package `org.purpurmc.purpur` SHALL be renamed to `org.trlcore.trlcore`
2. WHEN the Package_Migrator executes, THE directory structure `org/purpurmc/purpur` SHALL be renamed to `org/trlcore/trlcore`
3. WHEN packages are renamed, THE Package_Migrator SHALL update all `package` declarations in Java files
4. WHEN packages are renamed, THE Package_Migrator SHALL update all `import` statements referencing the old package
5. IF a Java file contains references to `org.purpurmc.purpur`, THEN THE Package_Migrator SHALL replace them with `org.trlcore.trlcore`

### Requirement 4: 品牌标识更新

**User Story:** As a developer, I want to update all brand identifiers and display names, so that the server correctly identifies itself as TRLCore.

#### Acceptance Criteria

1. WHEN updating brand identifiers, THE Rebrand_System SHALL change `Brand-Id` from `purpurmc:purpur` to `trlcore:trlcore`
2. WHEN updating brand identifiers, THE Rebrand_System SHALL change `Brand-Name` from `Purpur` to `TRLCore`
3. WHEN updating brand identifiers, THE Rebrand_System SHALL change `Implementation-Title` from `Purpur` to `TRLCore`
4. WHEN updating brand identifiers, THE Rebrand_System SHALL change `Specification-Title` from `Purpur` to `TRLCore`
5. WHEN updating brand identifiers, THE Rebrand_System SHALL change `Specification-Vendor` from `Purpur Team` to `TRLCore Team`
6. WHEN updating `ServerBuildInfo.java`, THE Rebrand_System SHALL add `BRAND_TRLCORE_ID` constant with value `Key.key("trlcore", "trlcore")`

### Requirement 5: 类名和文件名更新

**User Story:** As a developer, I want to rename all Purpur-prefixed classes and files to TRLCore, so that the naming is consistent throughout the codebase.

#### Acceptance Criteria

1. WHEN renaming classes, THE Rebrand_System SHALL rename `PurpurConfig.java` to `TRLCoreConfig.java`
2. WHEN renaming classes, THE Rebrand_System SHALL rename `PurpurWorldConfig.java` to `TRLCoreWorldConfig.java`
3. WHEN renaming classes, THE Rebrand_System SHALL rename `PurpurCommand.java` to `TRLCoreCommand.java`
4. WHEN renaming classes, THE Rebrand_System SHALL rename `PurpurPermissions.java` to `TRLCorePermissions.java`
5. WHEN renaming classes, THE Rebrand_System SHALL rename `PurpurStoredBee.java` to `TRLCoreStoredBee.java`
6. WHEN classes are renamed, THE Rebrand_System SHALL update all references to the old class names

### Requirement 6: Patch 文件更新

**User Story:** As a developer, I want to update all patch files to reflect the new naming, so that the patch system works correctly with the rebranded project.

#### Acceptance Criteria

1. WHEN updating patch files, THE Rebrand_System SHALL replace all occurrences of `Purpur` with `TRLCore` in patch comments
2. WHEN updating patch files, THE Rebrand_System SHALL update file paths from `purpur-*` to `trlcore-*`
3. WHEN updating patch files, THE Rebrand_System SHALL update package references from `org.purpurmc.purpur` to `org.trlcore.trlcore`
4. WHEN updating the Rebrand patch, THE Rebrand_System SHALL update `BRAND_PURPUR_ID` to `BRAND_TRLCORE_ID`

### Requirement 7: 配置文件名更新

**User Story:** As a developer, I want to update configuration file names and references, so that server administrators see TRLCore branding.

#### Acceptance Criteria

1. WHEN the server generates configuration files, THE system SHALL create `trlcore.yml` instead of `purpur.yml`
2. WHEN configuration classes reference file names, THE Rebrand_System SHALL update `purpur.yml` references to `trlcore.yml`
3. WHEN configuration classes reference section names, THE Rebrand_System SHALL update `purpur` section names to `trlcore`

### Requirement 8: 文档和 README 更新

**User Story:** As a developer, I want to update all documentation to reflect the TRLCore branding, so that users understand this is a rebranded fork.

#### Acceptance Criteria

1. WHEN updating README.md, THE Rebrand_System SHALL replace all Purpur references with TRLCore
2. WHEN updating README.md, THE Rebrand_System SHALL update Maven/Gradle dependency coordinates to use `org.trlcore.trlcore:trlcore-api`
3. WHEN updating README.md, THE Rebrand_System SHALL update repository URL to GitHub Packages `https://maven.pkg.github.com/chuyuewei/TRLCore`
   - GitHub 用户名: `chuyuewei`
4. WHEN updating README.md, THE Rebrand_System SHALL include GitHub Packages authentication instructions
5. WHEN updating CONTRIBUTING.md, THE Rebrand_System SHALL replace Purpur references with TRLCore

### Requirement 11: GitHub Packages 发布配置

**User Story:** As a developer, I want to configure the project to publish artifacts to GitHub Packages, so that users can easily depend on TRLCore API.

#### Acceptance Criteria

1. WHEN configuring build.gradle.kts, THE Config_Updater SHALL add GitHub Packages Maven repository configuration
2. WHEN configuring publishing, THE Config_Updater SHALL use `GITHUB_ACTOR` and `GITHUB_TOKEN` environment variables for authentication
3. WHEN configuring publishing, THE Config_Updater SHALL also support `gpr.user` and `gpr.key` gradle properties as fallback
4. WHEN publishing artifacts, THE system SHALL publish to `https://maven.pkg.github.com/chuyuewei/TRLCore`
5. WHEN creating GitHub Actions workflow, THE Config_Updater SHALL create a publish workflow that triggers on release creation

### Requirement 9: 测试插件更新

**User Story:** As a developer, I want to update the test plugin to use TRLCore naming, so that testing infrastructure is consistent.

#### Acceptance Criteria

1. WHEN updating test-plugin, THE Rebrand_System SHALL rename package from `org.purpurmc.testplugin` to `org.trlcore.testplugin`
2. WHEN updating test-plugin, THE Rebrand_System SHALL update all import statements accordingly
3. WHEN updating test-plugin configuration, THE Rebrand_System SHALL update any Purpur-specific references

### Requirement 10: 构建验证

**User Story:** As a developer, I want to verify the build works after rebranding, so that I can ensure the changes are complete and correct.

#### Acceptance Criteria

1. WHEN all rebranding changes are complete, THE build system SHALL successfully execute `./gradlew applyAllPatches`
2. WHEN all rebranding changes are complete, THE build system SHALL successfully execute `./gradlew build`
3. IF the build fails, THEN THE system SHALL provide clear error messages indicating which rebranding step was incomplete
