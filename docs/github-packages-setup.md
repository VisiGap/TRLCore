# GitHub Packages Maven 仓库配置指南

## 📋 概述
本项目使用 GitHub Packages 作为 Maven 仓库，用于发布和分发 TRLCore API。

**仓库地址**: https://github.com/chuyuewei/TRLCore

## 🔧 在其他项目中使用 TRLCore API

### Maven 配置

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/chuyuewei/TRLCore</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.purpurmc.purpur</groupId>
        <artifactId>purpur-api</artifactId>
        <version>1.21.11-R0.1-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### Gradle 配置

```kotlin
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/chuyuewei/TRLCore")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    compileOnly("org.purpurmc.purpur:purpur-api:1.21.11-R0.1-SNAPSHOT")
}
```

### 认证配置

GitHub Packages 需要认证才能访问。创建 Personal Access Token (PAT)：

1. 访问 GitHub Settings > Developer settings > Personal access tokens
2. 创建新的 token，勾选 `read:packages` 权限
3. 配置认证：

**Maven (`~/.m2/settings.xml`)**:
```xml
<settings>
    <servers>
        <server>
            <id>github</id>
            <username>YOUR_GITHUB_USERNAME</username>
            <password>YOUR_GITHUB_TOKEN</password>
        </server>
    </servers>
</settings>
```

**Gradle (`~/.gradle/gradle.properties`)**:
```properties
gpr.user=YOUR_GITHUB_USERNAME
gpr.token=YOUR_GITHUB_TOKEN
```

## 🚀 CI/CD 工作流

### 构建工作流 (`build.yml`)
- **触发条件**: 推送到 `main` 分支、Pull Request、手动触发
- **功能**: 应用补丁、构建项目、上传构建产物
- **跳过**: 提交消息包含 `[ci-skip]`

### 发布工作流 (`release.yml`)
- **触发条件**: 创建标签 (`v*`)、手动触发
- **功能**: 构建 Paperclip JAR、创建 GitHub Release

### API 发布工作流 (`publish.yml`)
- **触发条件**: 推送到 `main` 分支、手动触发
- **功能**: 发布 API 到 GitHub Packages Maven 仓库

## 📦 发布新版本

### 自动发布
```bash
# 创建并推送标签
git tag v1.21.11-1
git push origin v1.21.11-1
```

### 手动发布
1. 访问 https://github.com/chuyuewei/TRLCore/actions
2. 选择 "Release" 工作流
3. 点击 "Run workflow"
4. 输入版本号并运行

## 🔗 相关链接
- [GitHub Packages 文档](https://docs.github.com/en/packages)
- [GitHub Actions 文档](https://docs.github.com/en/actions)
- [TRLCore 仓库](https://github.com/chuyuewei/TRLCore)
