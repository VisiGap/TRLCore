# GitLab CI/CD 配置指南

## 📋 概述
本文档描述了 Leaf 项目从 GitHub Actions 迁移到 GitLab CI/CD 的配置和使用方法。

## 🔧 配置文件
主配置文件：`.gitlab-ci.yml`

## 🚀 CI/CD 流水线阶段

### 1. Build 阶段
- **作业**: `build`
- **功能**: 应用补丁并构建项目
- **触发条件**:
  - 推送到 `main` 分支
  - 合并请求
  - 手动触发
- **跳过条件**: 提交消息包含 `[ci-skip]`

### 2. Test 阶段
- **作业**: `code-quality`, `security-scan`
- **功能**: 代码质量检查和安全扫描
- **触发条件**: 合并请求或主分支推送

### 3. Package 阶段
- **作业**: `package`
- **功能**: 创建 Paperclip JAR 文件
- **触发条件**: 主分支推送或标签创建

### 4. Deploy 阶段
- **作业**: `publish-api`, `release`
- **功能**: 发布 API 到 Maven 仓库，创建 Release
- **触发条件**: 主分支推送或标签创建

## 🔑 GitLab CI/CD 变量配置

### 自动提供的变量 (无需配置)
GitLab CI/CD 自动提供以下变量用于 Maven 仓库认证：
```bash
CI_JOB_TOKEN          # GitLab CI Job Token (自动生成)
CI_PROJECT_ID         # 项目 ID (自动提供)
CI_PROJECT_URL        # 项目 URL (自动提供)
```

### 可选的自定义变量 (Settings > CI/CD > Variables)
```bash
# 自定义构建配置
GRADLE_OPTS="-Dorg.gradle.daemon=false -Dorg.gradle.caching=true"
JAVA_VERSION="21"
MC_VERSION="1.21.10"
```

> **注意**: 使用 GitLab 自带的 Package Registry 后，不再需要配置 `REPO_USER` 和 `REPO_PASSWORD`

## 📦 GitLab Package Registry

### Maven 仓库地址
```
https://gitlab.com/api/v4/projects/{PROJECT_ID}/packages/maven
```

### 在其他项目中使用 Leaf API
**Maven**:
```xml
<repositories>
    <repository>
        <id>gitlab-maven</id>
        <url>https://gitlab.com/api/v4/projects/{PROJECT_ID}/packages/maven</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>cn.dreeam.leaf</groupId>
        <artifactId>leaf-api</artifactId>
        <version>1.21.10-R0.1-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

**Gradle**:
```kotlin
repositories {
    maven {
        url = uri("https://gitlab.com/api/v4/projects/{PROJECT_ID}/packages/maven")
        // 如果是私有仓库，需要认证
        credentials(HttpHeaderCredentials::class) {
            name = "Private-Token"
            value = "your-access-token"
        }
        authentication {
            create<HttpHeaderAuthentication>("header")
        }
    }
}

dependencies {
    compileOnly("cn.dreeam.leaf:leaf-api:1.21.10-R0.1-SNAPSHOT")
}
```

### 访问已发布的包
1. 在 GitLab 项目中访问 **Packages & Registries > Package Registry**
2. 查看已发布的 Maven 包
3. 获取具体的依赖配置信息

## 🔄 缓存策略
- **Gradle Wrapper**: `.gradle/wrapper`
- **Gradle 缓存**: `.gradle/caches`
- **缓存键**: 基于分支名称 (`$CI_COMMIT_REF_SLUG`)

## 🐳 Docker 镜像
- **主镜像**: `eclipse-temurin:21-jdk`
- **Release 镜像**: `registry.gitlab.com/gitlab-org/release-cli:latest`

## 🔧 本地测试
```bash
# 安装 GitLab Runner
curl -L https://packages.gitlab.com/install/repositories/runner/gitlab-runner/script.deb.sh | sudo bash
sudo apt-get install gitlab-runner

# 本地执行 CI 作业
gitlab-runner exec docker build
```

## 📊 监控和报告

### 测试报告
- JUnit 测试结果自动解析
- 覆盖率报告 (JaCoCo)

### 安全报告
- 依赖安全扫描
- 漏洞报告

### 代码质量
- 构建状态徽章
- 测试覆盖率徽章

## 🚨 故障排除

### 常见问题
1. **构建失败**: 检查 Java 版本和 Gradle 配置
2. **权限错误**: 确保 `gradlew` 有执行权限
3. **缓存问题**: 清理缓存重新构建

### 调试命令
```bash
# 清理缓存
./gradlew clean cleanCache --refresh-dependencies

# 详细日志
./gradlew build --stacktrace --debug
```

## 🔄 迁移对比

| 功能 | GitHub Actions | GitLab CI/CD |
|------|----------------|--------------|
| 配置文件 | `.github/workflows/*.yml` | `.gitlab-ci.yml` |
| 触发器 | `on:` | `rules:` |
| 作业 | `jobs:` | `stages:` + 作业名 |
| 环境变量 | `env:` | `variables:` |
| 缓存 | `actions/cache` | `cache:` |
| 产物 | `actions/upload-artifact` | `artifacts:` |

## 📝 最佳实践
1. 使用 `--no-daemon` 避免 Gradle 守护进程问题
2. 合理设置产物过期时间
3. 使用条件规则优化流水线执行
4. 配置适当的重试策略
5. 监控构建时间和资源使用

## 🔗 相关链接
- [GitLab CI/CD 官方文档](https://docs.gitlab.com/ee/ci/)
- [GitLab Runner 安装](https://docs.gitlab.com/runner/install/)
- [YAML 语法参考](https://docs.gitlab.com/ee/ci/yaml/)