Great 👍
Let’s **design this properly from day 0** for a **Spring Boot Auth / Role / Permission system** using:

* Spring Boot
* Spring Security
* JWT (auth + expiration)
* Hibernate / JPA
* SQL (MySQL / PostgreSQL)
* DB migration (Flyway – recommended)

I’ll show **folder structure first**, then **models**, then **migration SQL**.

---

# 1️⃣ RECOMMENDED PROJECT STRUCTURE (CLEAN & SCALABLE)

```text
com.pickleball.identity
│
├── IdentityApplication.java
│
├── config
│   ├── SecurityConfig.java
│   ├── JwtConfig.java
│   └── PasswordConfig.java
│
├── security
│   ├── jwt
│   │   ├── JwtTokenProvider.java
│   │   ├── JwtAuthenticationFilter.java
│   │   └── JwtAuthenticationEntryPoint.java
│   │
│   └── UserPrincipal.java
│
├── controller
│   ├── api
│   │   ├── AuthController.java
│   │   ├── UserController.java
│   │   └── AdminController.java
│   │
│   └── HealthController.java
│
├── service
│   ├── AuthService.java
│   ├── UserService.java
│   └── RoleService.java
│
├── repository
│   ├── UserRepository.java
│   ├── RoleRepository.java
│   ├── PermissionRepository.java
│   └── UserJwtRepository.java
│
├── model
│   ├── entity
│   │   ├── User.java
│   │   ├── Role.java
│   │   ├── Permission.java
│   │   ├── UserRole.java
│   │   ├── RolePermission.java
│   │   └── UserJwt.java
│   │
│   └── dto
│       ├── LoginRequest.java
│       ├── LoginResponse.java
│       └── RegisterRequest.java
│
├── exception
│   ├── ApiException.java
│   └── GlobalExceptionHandler.java
│
└── util
    └── DateUtil.java
```

👉 This structure is **industry-standard** and easy to grow.

---

# 2️⃣ DOMAIN MODEL (AUTH / ROLE / PERMISSION)

## 🔹 RELATION OVERVIEW

```text
User
 ├── UserRole
 │    └── Role
 │         └── RolePermission
 │              └── Permission
 │
 └── UserJwt (tokens)
```

---

# 3️⃣ ENTITY MODELS (IMPORTANT PART)

## ✅ User

```java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private boolean enabled = true;

    @OneToMany(mappedBy = "user")
    private Set<UserRole> roles;
}
```

---

## ✅ Role

```java
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name; // ADMIN, USER

    @OneToMany(mappedBy = "role")
    private Set<RolePermission> permissions;
}
```

---

## ✅ Permission

```java
@Entity
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name; // USER_READ, USER_WRITE
}
```

---

## ✅ UserRole (JOIN)

```java
@Entity
@Table(name = "user_roles")
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    @ManyToOne
    @MapsId("userId")
    private User user;

    @ManyToOne
    @MapsId("roleId")
    private Role role;
}
```

---

## ✅ RolePermission (JOIN)

```java
@Entity
@Table(name = "role_permissions")
public class RolePermission {

    @EmbeddedId
    private RolePermissionId id;

    @ManyToOne
    @MapsId("roleId")
    private Role role;

    @ManyToOne
    @MapsId("permissionId")
    private Permission permission;
}
```

---

## ✅ UserJwt (TOKEN STORAGE)

```java
@Entity
@Table(name = "user_jwt")
public class UserJwt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(unique = true, nullable = false)
    private String tokenId;

    private Long expiresAt;

    private boolean revoked = false;
}
```

---

# 4️⃣ JWT CONCEPT (HOW AUTH WORKS)

```text
Login
 → Validate username/password
 → Load roles & permissions
 → Generate JWT (sub, roles, permissions, exp)
 → Save tokenId in user_jwt
 → Return JWT to client
```

Request:

```
Authorization: Bearer <JWT>
```

Filter:

* Verify signature
* Check exp
* Check tokenId not revoked
* Set SecurityContext

---

# 5️⃣ DATABASE MIGRATION (FLYWAY)

## 📁 Folder

```text
src/main/resources/db/migration
```

---

## 🧱 V1__init_auth_schema.sql

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE user_roles (
    user_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE role_permissions (
    role_id BIGINT,
    permission_id BIGINT,
    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE user_jwt (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    token_id VARCHAR(255) UNIQUE NOT NULL,
    expires_at BIGINT NOT NULL,
    revoked BOOLEAN DEFAULT FALSE
);
```

---

## 🧱 V2__seed_auth_data.sql

```sql
INSERT INTO roles (name) VALUES ('ADMIN'), ('USER');

INSERT INTO permissions (name)
VALUES
 ('USER_READ'),
 ('USER_WRITE'),
 ('ADMIN_ACCESS');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name = 'ADMIN';

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name = 'USER'
AND p.name = 'USER_READ';
```

---

# 6️⃣ WHY THIS DESIGN IS GOOD

✅ Clean separation
✅ Scales to microservices
✅ JWT revocation supported
✅ Role + permission flexible
✅ Hibernate-friendly
✅ Industry standard

---

# 🔜 NEXT STEPS (WE BUILD STEP-BY-STEP)

Say which one you want **next**:

1️⃣ JWT token provider + filter
2️⃣ Login API implementation
3️⃣ Permission → `@PreAuthorize`
4️⃣ Refresh token
5️⃣ Logout (revoke token)

I recommend **1️⃣ JWT token provider next** 🚀

---

# 7️⃣ API ENDPOINTS AND DOCUMENTATION

> 📘 **Interactive API Docs (Swagger UI):** http://localhost:8080/swagger-ui.html

## 1. Authentication (`ApiAuthController`)

| Method | URL | Protected | Description | Request Body / Parameters |
| :--- | :--- | :--- | :--- | :--- |
| **POST** | `/api/auth/register` | No | Register a new user | **Body (JSON):**<br>`{ "username": "...", "password": "..." }` |
| **POST** | `/api/auth/login` | No | Login and get tokens | **Body (JSON):**<br>`{ "username": "...", "password": "..." }`<br>**Returns:** Access Token, Refresh Token |
| **POST** | `/api/auth/refresh` | No | Refresh access token | **Body (JSON):**<br>`{ "refreshToken": "..." }` |
| **POST** | `/api/auth/change-password` | **Yes** | Change password | **Body (JSON):**<br>`{ "currentPassword": "...", "newPassword": "...", "confirmationPassword": "..." }` |
| **POST** | `/api/auth/logout` | **Yes** | Logout (revoke token) | **Header:** `Authorization: Bearer <token>` |
| **POST** | `/api/auth/login-auth` | No | Alias for login | **Body (JSON):**<br>`{ "username": "...", "password": "..." }` |

## 2. User Management (`UserController`)

| Method | URL | Protected | Description | Request Body / Parameters |
| :--- | :--- | :--- | :--- | :--- |
| **GET** | `/api/users/me` | **Yes** | Get current user profile | **Header:** `Authorization: Bearer <token>` |
| **PUT** | `/api/users/me` | **Yes** | Update current user profile | **Body (JSON):**<br>`{ "firstName": "...", "lastName": "...", "email": "..." }` |
| **GET** | `/api/users` | **Admin** | List all users | **Header:** `Authorization: Bearer <admin_token>` |
| **GET** | `/api/users/role/{role}` | **Admin** | List users by role | **Param:** `role` (e.g. USER, ADMIN)<br>**Header:** `Authorization: Bearer <admin_token>` |
| **POST** | `/api/users/{id}/roles` | **Admin** | Assign role to user | **Path:** `id` (User ID)<br>**Body (JSON):**<br>`{ "roleName": "..." }` |

## 3. Internal / System

| Method | URL | Protected | Description | Request Body / Parameters |
| :--- | :--- | :--- | :--- | :--- |
| **POST** | `/internal/auth/validate` | **Internal** | Validate token (Microservice) | **Header:** `Authorization: Bearer <token>`<br>**Header:** `X-API-KEY: <internal_key>` |
| **GET** | `/` | No | Home / Health check | None |

## 4. Actuator (Spring Boot)

| Method | URL | Protected | Description |
| :--- | :--- | :--- | :--- |
| **GET** | `/actuator/health` | No | Health check |
| **GET** | `/actuator/info` | No | Application info |
| **GET** | `/actuator` | No | List actuator endpoints |
