---
inclusion: always
---
<!------------------------------------------------------------------------------------
   Add rules to this file or a short description and have Kiro refine them for you.
   
   Learn about inclusion modes: https://kiro.dev/docs/steering/#inclusion-modes
-------------------------------------------------------------------------------------> 

# Kiro Steering Rules: Purpur/Paper Server Development (Java 21)

## 🎯 Project Context
- **Core Base**: Purpur (Fork of PaperMC).
- **Java Version**: Java 21 (LTS).
- **Objective**: Develop high-performance, scalable server-side logic or core modifications.

## 🛠️ Tech Stack & Syntax Standards

### 1. Java 21 Modern Features (MANDATORY)
You must utilize modern Java features to ensure code conciseness and performance:
- **Records**: Use `record` for data carriers (DTOs, Config objects) instead of POJOs with getters/setters.
- **Pattern Matching**: Use `instanceof` pattern matching and Switch Expressions to reduce boilerplate.
- **Virtual Threads**: Where applicable for I/O bound tasks (Database/Web requests), prefer Virtual Threads over heavy thread pools.
- **Var**: Use `var` for local variables where type is obvious.
- **Text Blocks**: Use `"""` for multi-line SQL or JSON strings.

### 2. Minecraft/Purpur API Best Practices
- **Text & UI**: 
    - **FORBIDDEN**: `net.md_5.bungee.api.ChatColor` or the `§` symbol.
    - **REQUIRED**: Use **Adventure API** (`net.kyori.adventure`). Use `MiniMessage` for parsing strings (e.g., `<red>Error!`).
    - Use `Component` for all message sending.
- **Data Persistence**:
    - Prefer **PersistentDataContainer (PDC)** API over NBT strings or external files for item/entity metadata.
- **Scheduler**:
    - Use `Folia`-compatible schedulers if possible (Region/Entity schedulers) to future-proof the code, or standard Paper schedulers.
- **NMS (Net Minecraft Server)**:
    - Only use NMS (`net.minecraft.server.*`) if the Paper API cannot achieve the goal.
    - If using NMS, verify mappings (Mojang Mappings are standard for Paper dev).

## ⚡ Performance & Safety Constraints
1.  **Main Thread Sanctity**: 
    - NEVER perform Database queries, Web requests, or heavy File I/O on the main server thread.
    - Use `CompletableFuture` or `runTaskAsynchronously`.
2.  **Collection Optimization**:
    - Use `fastutil` (Int2ObjectMap, etc.) for primitive collections to save memory.
3.  **Memory Leaks**:
    - Always unregister listeners/tasks in `onDisable()`.
    - Be cautious with static maps storing Player objects (Store `UUID` instead).

## 📝 Code Generation Workflow
When asked to write functionality:
1.  **Check Imports**: Ensure `net.kyori.adventure` and `org.bukkit` imports are correct.
2.  **Modernize**: If the user provides legacy code (e.g., Spigot 1.12 style), automatically refactor it to Paper 1.20+ / Java 21 standards.
3.  **Config**: If creating a Configurable feature, suggest a structured configuration class (using Records or specific Config libs).

## 🚀 Interaction Examples

### ❌ Bad (Legacy/Spigot Style)
```java
// Don't do this
public void onJoin(PlayerJoinEvent e) {
    e.getPlayer().sendMessage("§aWelcome, " + e.getPlayer().getName());
}

## 🔧 Core Modification (Patching) Specifics
- **Diff Management**: When modifying existing logic, explain *why* the patch is needed (Performance? New Feature?).
- **Mixin/Transformers**: If using Mixins (if applicable in your build setup), clearly separate the injection point.
- **Upstream Sync**: Avoid changing method signatures in API classes to maintain compatibility with existing plugins.