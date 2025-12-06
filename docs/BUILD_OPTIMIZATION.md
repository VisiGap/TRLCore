# TRLCore 构建优化指南

## 快速构建命令

```bash
# 标准构建
./gradlew build

# 跳过配置缓存 (首次或配置更改后)
./gradlew build --no-configuration-cache

# 离线构建 (已有依赖)
./gradlew build --offline

# 清理后构建
./gradlew clean build

# 仅编译不测试
./gradlew assemble
```

## 优化后的构建配置

### gradle.properties 优化项

| 配置项 | 说明 |
|--------|------|
| `org.gradle.parallel=true` | 并行构建 |
| `org.gradle.workers.max=8` | 最大工作线程 |
| `org.gradle.caching=true` | 启用构建缓存 |
| `org.gradle.daemon=true` | 启用守护进程 |
| `org.gradle.jvmargs=-Xmx4g` | 4GB 堆内存 |
| `kotlin.incremental=true` | Kotlin 增量编译 |

### 自定义构建任务

```bash
# 显示构建信息
./gradlew showBuildInfo

# 清理构建缓存
./gradlew cleanBuildCache

# 生成构建报告
./gradlew buildReport

# 列出依赖
./gradlew listDependencies
```

## 依赖版本管理

使用 `gradle/libs.versions.toml` 统一管理版本:

```toml
[versions]
guava = "33.4.0-jre"

[libraries]
guava = { group = "com.google.guava", name = "guava", version.ref = "guava" }

[bundles]
testing = ["junit-jupiter", "mockito-core"]
```

在 build.gradle.kts 中使用:
```kotlin
dependencies {
    implementation(libs.guava)
    testImplementation(libs.bundles.testing)
}
```

## 常见问题

### 构建慢?
1. 确保使用 Gradle Daemon: `org.gradle.daemon=true`
2. 增加内存: `org.gradle.jvmargs=-Xmx4g`
3. 启用并行: `org.gradle.parallel=true`

### 网络超时?
使用国内镜像或增加超时:
```properties
org.gradle.internal.http.connectionTimeout=60000
```

### 配置缓存错误?
```bash
./gradlew build --no-configuration-cache
```
