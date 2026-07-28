# 个人数据平台 - Repository 数据库操作汇总

**版本**: 1.0  
**日期**: 2026-07-28  
**说明**: 本文档汇总所有 `domain` 层 Spring Data JPA Repository 接口及其可用的数据库操作方法。

---

## 1. 通用说明

所有 Repository 均继承自 `JpaRepository<Entity, String>`，主键类型统一为 `String`（UUID7）。因此每个 Repository 默认拥有以下 CRUD 与分页能力：

| 方法 | 说明 |
|---|---|
| `S save(S entity)` | 保存或更新单个实体 |
| `Iterable<S> saveAll(Iterable<S> entities)` | 批量保存或更新 |
| `Optional<T> findById(String id)` | 根据主键查询 |
| `boolean existsById(String id)` | 判断主键是否存在 |
| `List<T> findAll()` | 查询全部 |
| `List<T> findAllById(Iterable<String> ids)` | 根据主键列表查询 |
| `long count()` | 统计总数 |
| `void deleteById(String id)` | 根据主键删除 |
| `void delete(T entity)` | 删除实体 |
| `void deleteAll(Iterable<? extends T> entities)` | 批量删除 |
| `void deleteAll()` | 删除全部 |
| `List<T> findAll(Sort sort)` | 排序查询 |
| `Page<T> findAll(Pageable pageable)` | 分页查询 |

> 以下各表仅列出**自定义查询方法**，通用 CRUD 方法不再重复。

---

## 2. 用户与用户资料

### `UserRepository`

**实体**: `com.example.experience.domain.user.entity.User`  
**主键**: `String` (UUID7)

| 方法 | 返回类型 | 说明 |
|---|---|---|
| `findByUsername(String username)` | `Optional<User>` | 根据用户名查询 |

### `UserProfileRepository`

**实体**: `com.example.experience.domain.user.entity.UserProfile`  
**主键**: `String` (UUID7)

| 方法 | 返回类型 | 说明 |
|---|---|---|
| `findByUserId(String userId)` | `Optional<UserProfile>` | 根据用户 ID 查询一对一资料 |

### `UserSessionRepository`

**实体**: `com.example.experience.domain.user.entity.UserSession`  
**主键**: `String` (UUID7)

| 方法 | 返回类型 | 说明 |
|---|---|---|
| `findByRefreshTokenHash(String refreshTokenHash)` | `Optional<UserSession>` | 根据 refresh token 哈希查询 |
| `findByUserIdAndStatus(String userId, String status)` | `List<UserSession>` | 查询用户指定状态的会话 |
| `findByUserId(String userId)` | `List<UserSession>` | 查询用户的所有会话 |

---

## 3. RBAC

### `RoleRepository`

**实体**: `com.example.experience.domain.rbac.entity.Role`  
**主键**: `String` (UUID7)

| 方法 | 返回类型 | 说明 |
|---|---|---|
| `findByName(String name)` | `Optional<Role>` | 根据角色名查询 |

### `PermissionRepository`

**实体**: `com.example.experience.domain.rbac.entity.Permission`  
**主键**: `String` (UUID7)

| 方法 | 返回类型 | 说明 |
|---|---|---|
| `findByResourceAndAction(String resource, String action)` | `Optional<Permission>` | 根据资源与操作查询 |

### `UserRoleRepository`

**实体**: `com.example.experience.domain.rbac.entity.UserRole`  
**主键**: `String` (UUID7)

| 方法 | 返回类型 | 说明 |
|---|---|---|
| `findByUserId(String userId)` | `List<UserRole>` | 查询用户的所有角色关联 |

### `RolePermissionRepository`

**实体**: `com.example.experience.domain.rbac.entity.RolePermission`  
**主键**: `String` (UUID7)

| 方法 | 返回类型 | 说明 |
|---|---|---|
| `findByRoleId(String roleId)` | `List<RolePermission>` | 查询角色的所有权限关联 |

---

## 4. 事件

### `EventRepository`

**实体**: `com.example.experience.domain.event.entity.Event`  
**主键**: `String` (UUID7)

| 方法 | 返回类型 | 说明 |
|---|---|---|
| `findByPlatform(String platform)` | `List<RolePermission>` | 查询指定平台的所有event |

---

## 5. 泛实体

### `EntityRecordRepository`

**实体**: `com.example.experience.domain.entity.entity.EntityRecord`  
**主键**: `String` (UUID7)

当前无自定义方法，使用通用 CRUD。

### `PersonRepository`

**实体**: `com.example.experience.domain.entity.entity.Person`  
**主键**: `String` (UUID7)

| 方法 | 返回类型 | 说明 |
|---|---|---|
| `findByUserId(String userId)` | `List<Person>` | 查询用户的联系人网络 |

---

## 6. 数据源与凭证

### `UserDataSourceRepository`

**实体**: `com.example.experience.domain.datasource.entity.UserDataSource`  
**主键**: `String` (UUID7)

| 方法 | 返回类型 | 说明 |
|---|---|---|
| `findByUserIdAndPlatformNameAndSourceIdentifier(String userId, String platformName, String sourceIdentifier)` | `Optional<UserDataSource>` | 根据用户、平台、源标识查询唯一数据源 |
| `findByUserId(String userId)` | `List<UserDataSource>` | 查询用户的所有数据源 |

### `UserCredentialRepository`

**实体**: `com.example.experience.domain.datasource.entity.UserCredential`  
**主键**: `String` (UUID7)

| 方法 | 返回类型 | 说明 |
|---|---|---|
| `findByUserId(String userId)` | `List<UserCredential>` | 查询用户的所有凭证 |
| `findByDataSourceId(String dataSourceId)` | `List<UserCredential>` | 查询数据源下的所有凭证 |

---

## 7. 同步

### `SyncLogRepository`

**实体**: `com.example.experience.domain.sync.entity.SyncLog`  
**主键**: `String` (UUID7)

| 方法 | 返回类型 | 说明 |
|---|---|---|
| `findByDataSourceId(String dataSourceId)` | `List<SyncLog>` | 查询数据源的所有同步日志 |

### `SyncAdapterRepository`

**实体**: `com.example.experience.domain.sync.entity.SyncAdapter`  
**主键**: `String` (UUID7)

| 方法 | 返回类型 | 说明 |
|---|---|---|
| `findByAdapterKey(String adapterKey)` | `Optional<SyncAdapter>` | 根据适配器 key 查询 |

---

## 8. 平台

### `PlatformRepository`

**实体**: `com.example.experience.domain.platform.entity.Platform`  
**主键**: `String` (UUID7)

| 方法 | 返回类型 | 说明 |
|---|---|---|
| `findByNameAndCreatedBy(String name, String createdBy)` | `Optional<Platform>` | 根据名称和创建者查询 |
| `findByCreatedBy(String createdBy)` | `List<Platform>` | 查询某创建者的所有平台 |

---

## 9. 媒体文件

### `MediaFileRepository`

**实体**: `com.example.experience.domain.media.entity.MediaFile`  
**主键**: `String` (UUID7)

| 方法 | 返回类型 | 说明 |
|---|---|---|
| `findByUserId(String userId)` | `List<MediaFile>` | 查询用户的所有媒体文件 |
| `findByDataSourceId(String dataSourceId)` | `List<MediaFile>` | 查询数据源下的所有媒体文件 |
| `findByChecksumSha256(String checksumSha256)` | `Optional<MediaFile>` | 根据 SHA-256 校验值查询（去重） |

---

## 10. 扩展指南

如需新增查询，按 Spring Data JPA 派生查询方法命名规则即可，例如：

```java
public interface EventRepository extends JpaRepository<Event, String> {
    List<Event> findByUserIdAndPlatform(String userId, String platform);
    List<Event> findByUserIdAndOccurredAtBetween(String userId, Instant start, Instant end);
}
```

对于复杂 SQL，可添加 `@Query` 注解或自定义 Repository 实现。
