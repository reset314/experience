# 个人数据平台 - 数据模型与架构设计文档

**版本**: 2.0  
**日期**: 2026-07-25  
**状态**: 已定稿（待实现）

---

## 1. 项目定位

本项目是一个**自托管多用户个人数据全记录平台**：

- 支持多用户注册与登录
- 记录用户在多个平台（微信、QQ、B站、本地等）的操作事件
- 统一管理操作涉及的“实体”（人、文件、视频、会话等）
- 维护用户的关系人网络（联系人、好友等）
- 管理数据源同步（自动/手动）及认证凭证
- 追踪同步任务日志
- 支持平台和同步适配器的动态注册与扩展

部署方式：**直接运行 JAR** 或 **Docker Compose** 均可，配置以外置环境变量/配置文件为主。

---

## 2. 设计决策

| 决策 | 选择 | 说明 |
|---|---|---|
| 主键生成 | UUID7 | 全表统一使用 UUID7，不采用自增 ID |
| 权限模型 | RBAC | roles + permissions + role_permissions + user_roles |
| 用户表拆分 | users + user_profiles | 认证与实名/偏好信息分离 |
| 媒体存储 | 独立 media_files 表 | events.payload 通过 `media_file_ids` 引用 |
| 代码组织 | 分层架构 | domain / application / infrastructure / interfaces |
| 部署 | 双模式 | 直接运行 JAR + Docker Compose |
| DDD 模块 | 暂不切分 | 等业务域复杂后再抽模块 |

---

## 3. 数据模型

### 3.1 系统账户与用户资料

#### `users` — 系统账户

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `id` | UUID7 | PRIMARY KEY | 用户唯一标识 |
| `username` | VARCHAR(64) | UNIQUE NOT NULL | 登录用户名 |
| `password_hash` | VARCHAR(255) | NOT NULL | 密码哈希（bcrypt/scrypt） |
| `email` | VARCHAR(255) | NULLABLE | 邮箱 |
| `phone` | VARCHAR(32) | NULLABLE | 手机号 |
| `status` | VARCHAR(16) | NOT NULL DEFAULT 'active' | active / suspended / deleted |
| `created_at` | TIMESTAMP | NOT NULL DEFAULT NOW() | 注册时间 |
| `updated_at` | TIMESTAMP | NOT NULL DEFAULT NOW() | 更新时间 |
| `last_login_at` | TIMESTAMP | NULLABLE | 最后登录时间 |

#### `user_profiles` — 用户资料（一对一）

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `id` | UUID7 | PRIMARY KEY | |
| `user_id` | UUID7 | FK → users(id), UNIQUE NOT NULL | 一对一 |
| `display_name` | VARCHAR(128) | NULLABLE | 显示名称 |
| `real_name` | VARCHAR(64) | NULLABLE | 真实姓名（认证） |
| `id_card_hash` | VARCHAR(255) | NULLABLE | 身份证号哈希（查重） |
| `id_card_encrypted` | TEXT | NULLABLE | 身份证号加密存储（AES-256-GCM） |
| `id_verified_at` | TIMESTAMP | NULLABLE | 实名认证通过时间 |
| `settings` | JSONB | NULLABLE | 用户偏好设置 |
| `created_at` | TIMESTAMP | NOT NULL DEFAULT NOW() | |
| `updated_at` | TIMESTAMP | NOT NULL DEFAULT NOW() | |

---

### 3.2 RBAC

#### `roles` — 角色

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `id` | UUID7 | PRIMARY KEY | |
| `name` | VARCHAR(64) | UNIQUE NOT NULL | superadmin / admin / user / auditor |
| `display_name` | VARCHAR(128) | NOT NULL | 显示名 |
| `description` | TEXT | NULLABLE | |
| `is_system` | BOOLEAN | DEFAULT FALSE | 系统内置角色不可删 |
| `created_at` | TIMESTAMP | DEFAULT NOW() | |
| `updated_at` | TIMESTAMP | DEFAULT NOW() | |

#### `permissions` — 权限原子

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `id` | UUID7 | PRIMARY KEY | |
| `resource` | VARCHAR(64) | NOT NULL | user / datasource / event / sync / platform / setting |
| `action` | VARCHAR(64) | NOT NULL | read / write / delete / trigger / manage |
| `description` | TEXT | NULLABLE | |
| UNIQUE | (resource, action) | | |

#### `role_permissions` — 角色权限关联

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `role_id` | UUID7 | FK → roles(id), PK | |
| `permission_id` | UUID7 | FK → permissions(id), PK | |

#### `user_roles` — 用户角色关联

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `user_id` | UUID7 | FK → users(id), PK | |
| `role_id` | UUID7 | FK → roles(id), PK | |

#### 默认角色权限

- **superadmin**：`*:*`（所有权限）
- **admin**：管理用户、数据源、平台；不能修改系统设置、不能删除 superadmin
- **user**：只能操作自己的数据（datasource:read/write, event:read, sync:trigger）
- **auditor**（可选）：只读所有数据

---

### 3.3 操作事件

#### `events`

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `id` | UUID7 | PRIMARY KEY | |
| `user_id` | UUID7 | FK → users(id), NOT NULL | 数据归属用户 |
| `operator_id` | UUID7 | FK → entities(id), NULLABLE | 实际操作者 |
| `target_id` | UUID7 | FK → entities(id), NULLABLE | 操作目标 |
| `device_mac` | VARCHAR(32) | NOT NULL | 设备 MAC |
| `platform` | VARCHAR(64) | NOT NULL | 平台名称 |
| `event_type` | VARCHAR(64) | NOT NULL | 操作类型 |
| `occurred_at` | TIMESTAMP | NOT NULL | 实际发生时间 |
| `payload` | JSONB | NOT NULL | 平台特有字段，含 `media_file_ids` |
| `source_type` | VARCHAR(16) | DEFAULT 'auto_sync' | auto_sync / manual / import |
| `deleted_at` | TIMESTAMP | NULLABLE | 软删除 |
| `created_at` | TIMESTAMP | DEFAULT NOW() | |

`payload` 是 JSONB，**不强制 schema**，由各个 platform/adapter 自行决定内部结构。建议约定：

```json
{
  "text": "...",
  "media_file_ids": ["uuid7-1", "uuid7-2"],
  "raw_payload": { }
}
```

- `media_file_ids` 是平台间可选约定，用于关联 `media_files`
- `raw_payload` 建议保留原始平台数据结构，便于回溯与调试

---

### 3.4 泛实体

#### `entities`

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `id` | UUID7 | PRIMARY KEY | |
| `type` | VARCHAR(32) | NOT NULL | PERSON / VIDEO / FILE / SESSION / POST / ... |
| `display_name` | VARCHAR(255) | NULLABLE | 可读名称 |
| `canonical_entity_id` | UUID7 | NULLABLE | 等同分组标识符（多条记录指向同一现实实体） |
| `relations` | JSON | NULLABLE | 实体间关系 |
| `metadata` | JSONB | NULLABLE | 额外属性 |
| `created_at` | TIMESTAMP | DEFAULT NOW() | |
| `updated_at` | TIMESTAMP | DEFAULT NOW() | |

> 原字段名 `isoneids` 改为 `canonical_entity_id`，含义更清晰。

#### `persons` — 关系人网络

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `id` | UUID7 | PRIMARY KEY | |
| `user_id` | UUID7 | FK → users(id), NOT NULL | 归属用户 |
| `canonical_entity_id` | UUID7 | NULLABLE | 指向 entities 分组 |
| `display_name` | VARCHAR(255) | NULLABLE | |
| `relations` | JSON | NULLABLE | 关系人关系 |
| `metadata` | JSONB | NULLABLE | |
| `created_at` | TIMESTAMP | DEFAULT NOW() | |
| `updated_at` | TIMESTAMP | DEFAULT NOW() | |

边界说明：`entities` 是事件中出现过的客观实体；`persons` 是用户主动维护的联系人网络，可关联到某个 `canonical_entity_id`。

---

### 3.5 数据源与凭证

#### `user_data_sources`

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `id` | UUID7 | PRIMARY KEY | |
| `user_id` | UUID7 | FK → users(id), NOT NULL | |
| `platform_name` | VARCHAR(64) | NOT NULL | 关联 platforms.name |
| `source_identifier` | VARCHAR(255) | NOT NULL | 数据源唯一标识 |
| `display_name` | VARCHAR(128) | NULLABLE | 别名 |
| `sync_status` | VARCHAR(32) | DEFAULT 'idle' | idle / syncing / success / error / disabled |
| `auth_status` | VARCHAR(32) | DEFAULT 'authorized' | authorized / requires_interaction / pending_verification |
| `requires_user_interaction` | BOOLEAN | DEFAULT FALSE | 是否需要手动操作 |
| `interaction_hint` | TEXT | NULLABLE | 操作提示 |
| `last_sync_at` | TIMESTAMP | NULLABLE | |
| `last_sync_error` | TEXT | NULLABLE | |
| `total_events_count` | BIGINT | DEFAULT 0 | |
| `total_storage_bytes` | BIGINT | DEFAULT 0 | |
| `sync_config` | JSONB | NULLABLE | |
| `activated_at` | TIMESTAMP | DEFAULT NOW() | |
| `updated_at` | TIMESTAMP | DEFAULT NOW() | |
| `deleted_at` | TIMESTAMP | NULLABLE | 软删除 |

UNIQUE: `(user_id, platform_name, source_identifier)`

#### `user_credentials`

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `id` | UUID7 | PRIMARY KEY | |
| `user_id` | UUID7 | FK → users(id), NOT NULL | |
| `data_source_id` | UUID7 | FK → user_data_sources(id), NOT NULL | |
| `credential_type` | VARCHAR(32) | NOT NULL | OAUTH2_REFRESH / SESSION_COOKIE / PASSWORD / MANUAL_TICKET |
| `encrypted_credential` | TEXT | NOT NULL | AES-256-GCM 加密 |
| `encrypted_extra` | TEXT | NULLABLE | 加密后的额外元数据 |
| `status` | VARCHAR(32) | DEFAULT 'active' | active / expired / revoked / ... |
| `expires_at` | TIMESTAMP | NULLABLE | |
| `last_used_at` | TIMESTAMP | NULLABLE | |
| `auth_context` | JSONB | NULLABLE | 授权上下文 |
| `created_at` | TIMESTAMP | DEFAULT NOW() | |
| `updated_at` | TIMESTAMP | DEFAULT NOW() | |

---

### 3.6 同步

#### `sync_logs`

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `id` | UUID7 | PRIMARY KEY | |
| `data_source_id` | UUID7 | FK → user_data_sources(id), NOT NULL | |
| `started_at` | TIMESTAMP | NOT NULL | |
| `finished_at` | TIMESTAMP | NULLABLE | |
| `status` | VARCHAR(32) | NOT NULL | pending / running / success / failed / partial |
| `events_fetched` | BIGINT | DEFAULT 0 | |
| `events_inserted` | BIGINT | DEFAULT 0 | |
| `media_downloaded` | BIGINT | DEFAULT 0 | |
| `media_bytes` | BIGINT | DEFAULT 0 | |
| `error_message` | TEXT | NULLABLE | |
| `context` | JSONB | NULLABLE | |
| `created_at` | TIMESTAMP | DEFAULT NOW() | |

> 只追加，不更新（除 `finished_at`）。

#### `sync_adapters`

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `id` | UUID7 | PRIMARY KEY | |
| `adapter_key` | VARCHAR(128) | UNIQUE NOT NULL | 适配器标识，如 `wechat-adapter` |
| `class_name` | VARCHAR(255) | NULLABLE | 运行时映射的 Java 类全名 |
| `display_name` | VARCHAR(128) | NOT NULL | |
| `description` | TEXT | NULLABLE | |
| `created_by` | VARCHAR(64) | NOT NULL | system / admin_xxx / user_xxx |
| `visibility` | VARCHAR(32) | DEFAULT 'private' | public / private / shared |
| `status` | VARCHAR(32) | DEFAULT 'active' | active / disabled / deprecated |
| `version` | VARCHAR(16) | NULLABLE | |
| `metadata` | JSONB | NULLABLE | |
| `created_at` | TIMESTAMP | DEFAULT NOW() | |
| `updated_at` | TIMESTAMP | DEFAULT NOW() | |

> 新增 `adapter_key` 解耦数据库与 Java 类全名。

---

### 3.7 平台定义

#### `platforms`

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `id` | UUID7 | PRIMARY KEY | |
| `name` | VARCHAR(64) | NOT NULL | 平台名称 |
| `display_name` | VARCHAR(128) | NOT NULL | 显示名 |
| `description` | TEXT | NULLABLE | |
| `adapter_id` | UUID7 | FK → sync_adapters(id), NULLABLE | 关联适配器 |
| `created_by` | VARCHAR(64) | NOT NULL | |
| `visibility` | VARCHAR(32) | DEFAULT 'private' | |
| `icon` | VARCHAR(255) | NULLABLE | |
| `metadata` | JSONB | NULLABLE | |
| `created_at` | TIMESTAMP | DEFAULT NOW() | |
| `updated_at` | TIMESTAMP | DEFAULT NOW() | |
| `deleted_at` | TIMESTAMP | NULLABLE | 软删除 |

UNIQUE:
- `(name, created_by)`
- `(created_by, adapter_id)`（adapter_id 为 NULL 时允许多条）

---

### 3.8 媒体文件

#### `media_files`

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `id` | UUID7 | PRIMARY KEY | |
| `user_id` | UUID7 | FK → users(id), NOT NULL | 归属用户 |
| `data_source_id` | UUID7 | FK → user_data_sources(id), NULLABLE | 来源 |
| `original_name` | VARCHAR(255) | NULLABLE | 原始文件名 |
| `mime_type` | VARCHAR(128) | NULLABLE | 类型 |
| `size_bytes` | BIGINT | NOT NULL DEFAULT 0 | |
| `storage_path` | VARCHAR(512) | NOT NULL UNIQUE | MinIO 路径 |
| `storage_bucket` | VARCHAR(64) | NOT NULL DEFAULT 'experience' | |
| `checksum_sha256` | VARCHAR(64) | NULLABLE | 去重/完整性校验 |
| `metadata` | JSONB | NULLABLE | 宽高、时长、缩略图路径等 |
| `created_at` | TIMESTAMP | DEFAULT NOW() | |
| `updated_at` | TIMESTAMP | DEFAULT NOW() | |

索引：`(user_id)`, `(data_source_id)`, `(checksum_sha256)`

---

### 3.9 审计日志（待细化）

#### `audit_logs`

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `id` | UUID7 | PRIMARY KEY | |
| `user_id` | UUID7 | FK → users(id), NULLABLE | 操作者 |
| `action` | VARCHAR(64) | NOT NULL | login / role_change / credential_access / data_export / data_delete |
| `resource_type` | VARCHAR(64) | NULLABLE | user / datasource / event / credential |
| `resource_id` | UUID7 | NULLABLE | 被操作对象 |
| `ip_address` | VARCHAR(64) | NULLABLE | |
| `user_agent` | TEXT | NULLABLE | |
| `details` | JSONB | NULLABLE | 变更前后快照 |
| `created_at` | TIMESTAMP | DEFAULT NOW() | |

---

## 4. 代码包结构

```text
com.example.experience
├── ExperienceApplication.java
├── common
│   ├── exception
│   ├── util
│   └── config
├── domain
│   ├── user
│   │   ├── entity
│   │   └── repository
│   ├── rbac
│   │   ├── entity
│   │   └── repository
│   ├── event
│   │   ├── entity
│   │   └── repository
│   ├── entity
│   │   ├── entity
│   │   └── repository
│   ├── datasource
│   │   ├── entity
│   │   └── repository
│   ├── sync
│   │   ├── entity
│   │   └── repository
│   └── platform
│       ├── entity
│       └── repository
├── application
│   ├── user
│   ├── rbac
│   ├── event
│   ├── entity
│   ├── datasource
│   ├── sync
│   └── platform
├── infrastructure
│   ├── persistence
│   ├── storage
│   ├── security
│   └── config
└── interfaces
    ├── rest
    └── web
```

说明：
- `domain`：实体 + 仓库接口，无业务逻辑
- `application`：Service / DTO / 用例编排
- `infrastructure`：技术实现（MinIO、加密、数据库配置）
- `interfaces`：REST 入口、全局异常、拦截器

---

## 5. 部署

### 5.1 直接运行

```bash
java -jar experience.jar
```

配置写在 `application.properties`：

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/experience
spring.datasource.username=postgres
spring.datasource.password=postgres

experience.minio.endpoint=http://localhost:9000
experience.minio.access-key=minioadmin
experience.minio.secret-key=minioadmin
```

### 5.2 环境配置（Spring Profiles）

通过切换配置文件管理 dev / prod 环境：

```text
src/main/resources/
├── application.properties       # 公共默认配置
├── application-dev.properties   # 开发环境
└── application-prod.properties  # 生产环境
```

切换方式：

```bash
# 开发
java -jar experience.jar --spring.profiles.active=dev

# 生产
java -jar experience.jar --spring.profiles.active=prod
```

`application-dev.properties` 示例：

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/experience_dev
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=create-drop

experience.minio.endpoint=http://localhost:9000
experience.minio.access-key=minioadmin
experience.minio.secret-key=minioadmin
```

`application-prod.properties` 示例：

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
spring.jpa.hibernate.ddl-auto=validate

experience.minio.endpoint=${EXPERIENCE_MINIO_ENDPOINT}
experience.minio.access-key=${EXPERIENCE_MINIO_ACCESS_KEY}
experience.minio.secret-key=${EXPERIENCE_MINIO_SECRET_KEY}
```

### 5.3 Docker Compose

```yaml
services:
  postgres:
    image: postgres:17
    volumes:
      - pg_data:/var/lib/postgresql/data

  minio:
    image: minio/minio
    command: server /data --console-address ":9001"
    volumes:
      - minio_data:/data

  app:
    build: .
    depends_on:
      - postgres
      - minio
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/experience
      - EXPERIENCE_MINIO_ENDPOINT=http://minio:9000
```

### 5.4 初始化

- 数据库：开发用 `ddl-auto=create`，稳定环境用 Flyway/Liquibase
- MinIO bucket：应用启动时自动检查并创建
- 默认 superadmin：首次启动时从环境变量或配置创建

---

## 6. 待细化事项

1. **数据保留/删除策略**：用户级与实例级 retention policy，导出权
2. **审计日志**：具体审计范围与存储周期
3. **同步适配器热加载**：`sync_adapters` 运行时挂载机制
4. **多实体归一算法**：`canonical_entity_id` 如何自动分组
5. **OAuth/扫码登录流程**：`user_credentials.auth_context` 具体结构

---

## 7. 表间关系

```text
users (1) ─────── (1) user_profiles
users (1) ─────── (N) user_roles
users (1) ─────── (N) events
users (1) ─────── (N) persons
users (1) ─────── (N) user_data_sources
users (1) ─────── (N) user_credentials
users (1) ─────── (N) media_files

roles (1) ─────── (N) role_permissions
permissions (1) ── (N) role_permissions

user_data_sources (1) ─── (N) user_credentials
user_data_sources (1) ─── (N) sync_logs
user_data_sources (1) ─── (N) media_files
user_data_sources.platform_name ───→ platforms.name

platforms.adapter_id ───→ sync_adapters.id

events.operator_id ────→ entities.id
events.target_id ──────→ entities.id
persons.canonical_entity_id ──────→ entities.canonical_entity_id
```
