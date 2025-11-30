# TRLCore Network Troubleshooting Guide

## Problem
Build fails with `Remote host terminated the handshake` errors when downloading dependencies.

## Quick Fixes to Try (In Order)

### 1. Check Network Connectivity
```powershell
# Test if you can reach Maven repositories
Test-NetConnection -ComputerName repo.papermc.io -Port 443
Test-NetConnection -ComputerName repo.maven.apache.org -Port 443
Test-NetConnection -ComputerName plugins.gradle.org -Port 443
```

### 2. Clean Gradle Cache
```powershell
# Stop all Gradle daemons
.\gradlew.bat --stop

# Delete Gradle cache (WARNING: Will need to re-download all dependencies)
Remove-Item -Recurse -Force "$env:USERPROFILE\.gradle\caches"

# Try building again
.\gradlew.bat applyAllPatches
```

### 3. Check Firewall/Antivirus
- **Temporarily disable** your antivirus/firewall
- Check if your corporate network requires a proxy
- Try building again

### 4. Configure Proxy (If Behind Corporate Network)

Edit `gradle.properties` and add:
```properties
systemProp.http.proxyHost=your-proxy-host
systemProp.http.proxyPort=8080
systemProp.https.proxyHost=your-proxy-host
systemProp.https.proxyPort=8080

# If proxy requires authentication:
systemProp.http.proxyUser=username
systemProp.http.proxyPassword=password
systemProp.https.proxyUser=username
systemProp.https.proxyPassword=password
```

### 5. Use Mirror Repositories (China Users)

If you're in China, Maven Central can be slow/blocked. Add Aliyun mirror to `build.gradle.kts`:

```kotlin
repositories {
    maven("https://maven.aliyun.com/repository/public/")
    maven("https://maven.aliyun.com/repository/central/")
    mavenCentral()
    maven(paperMavenPublicUrl)
    maven(leafMavenPublicUrl)
}
```

Also update `settings.gradle.kts`:
```kotlin
pluginManagement {
    repositories {
        maven("https://maven.aliyun.com/repository/gradle-plugin/")
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}
```

### 6. Increase Gradle Daemon Memory

Already configured in `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4G -Xms1G
```

### 7. Try Building Offline (After Dependencies Downloaded)

```powershell
.\gradlew.bat applyAllPatches --offline
```

### 8. Use VPN (If in Restricted Network)

If your network blocks Maven repositories, try using a VPN to bypass restrictions.

### 9. Manual Dependency Download (Last Resort)

If specific dependencies fail, download them manually:

1. Find the failing dependency URL from error message
2. Download using browser/wget/curl
3. Place in local Maven cache: `%USERPROFILE%\.m2\repository\`

## Advanced Debugging

### Enable Gradle Debug Logging
```powershell
.\gradlew.bat applyAllPatches --debug --stacktrace 2>&1 | Tee-Object -FilePath build-log.txt
```

### Check Java Version
```powershell
java -version
```
Should show Java 21 or higher for TRLCore.

### Verify DNS Resolution
```powershell
nslookup repo.papermc.io
nslookup repo.maven.apache.org
```

### Check Windows Time Synchronization
SSL/TLS requires accurate system time:
```powershell
w32tm /query /status
```

## What We've Already Done

✅ Added TLS protocol compatibility settings to `gradle.properties`
✅ Increased connection and read timeouts (60 seconds)
✅ Configured DNS cache TTL
✅ Set Windows-ROOT trust store type

## Next Steps

1. Try the Quick Fixes above in order
2. If you're in China, strongly recommend adding Aliyun mirrors
3. Check if your network requires a proxy
4. Report which step resolves the issue

## Common Causes

- **🔥 Most Common**: Firewall/antivirus blocking HTTPS
- **🌍 Geographic**: China/restricted networks blocking Maven Central
- **🏢 Corporate**: Proxy authentication required
- **🕒 Rare**: System time drift causing SSL cert validation failure
