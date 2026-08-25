# 默认本地用户

项目在首次启动时会自动创建一个内置的本地用户。

## 默认账号

| 字段 | 值 |
|------|-----|
| 用户名 | `user` |
| 密码 | `password01` |
| 角色 | `superadmin` |

## 使用方式

启动应用后，直接调用登录接口获取 token：

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "user",
  "password": "password01"
}
```

## 修改默认账号

可以通过环境变量覆盖默认值：

```bash
EXPERIENCE_INITIAL_ADMIN_USERNAME=yourname
EXPERIENCE_INITIAL_ADMIN_PASSWORD=yourpassword
```

或在 `application.properties` 中修改：

```properties
experience.initial-admin.username=${EXPERIENCE_INITIAL_ADMIN_USERNAME:user}
experience.initial-admin.password=${EXPERIENCE_INITIAL_ADMIN_PASSWORD:password01}
```

## 安全提醒

- 这是个人本地应用的默认账号，**请勿在公网环境使用默认密码**。
- 首次部署到生产环境前，请修改默认密码。
