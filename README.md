# Smart Blog - 智能博客管理系统

> Spring Boot 3 + Vue 3 全栈项目 | 面向面试的实战项目

一个前后端分离的博客管理系统，包含文章发布、评论、点赞、分类搜索等功能。技术栈主流，包含 Redis 缓存、JWT 认证、接口限流、AOP 日志等面试亮点。

## 技术栈

### 后端
- **Spring Boot 3.2** - Java 主流框架
- **MyBatis-Plus 3.5** - 高效 ORM
- **MySQL 8.0** - 关系型数据库
- **Redis 7** - 缓存 & 限流
- **Spring Security + JWT** - 安全认证
- **Knife4j** - API 文档

### 前端
- **Vue 3** - 渐进式框架
- **Element Plus** - UI 组件库
- **Pinia** - 状态管理
- **Vue Router** - 路由管理
- **Axios** - HTTP 客户端

### 部署
- **Docker + Docker Compose** - 容器化部署

## 功能清单

| 模块 | 功能 |
|------|------|
| 认证 | 注册、登录、JWT Token 认证 |
| 文章 | 发布、编辑、删除、分页列表、搜索 |
| 分类 | 分类管理 |
| 评论 | 发表、删除、列表 |
| 互动 | 文章点赞、评论点赞 |
| 缓存 | Redis 缓存热门文章 |
| 限流 | 自定义注解 + Redis 限流 |
| 日志 | AOP 切面记录操作日志 |
| 文档 | Knife4j 在线 API 文档 |

## 技术亮点（面试加分项）

1. **统一响应 + 全局异常处理** — 工程化思维
2. **JWT 无状态认证** — 安全意识
3. **Redis 缓存** — 性能优化
4. **自定义注解限流** — 元编程 + AOP 能力
5. **操作日志切面** — 面向切面编程
6. **Docker 一键部署** — DevOps 能力
7. **RESTful API 规范** — 接口设计能力
8. **逻辑删除 + 自动填充** — 框架进阶

## 快速开始

### 环境要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0
- Redis 7
- Node.js 18+

### 方式一：Docker 一键启动

```bash
# 克隆项目
git clone https://github.com/xuehua6657/zhineng.git
cd zhineng

# 一键启动（MySQL + Redis + Backend）
docker-compose up -d

# 查看日志
docker-compose logs -f backend
```

### 方式二：本地开发

#### 1. 初始化数据库

```bash
mysql -u root -p < sql/init.sql
```

#### 2. 启动后端

```bash
cd blog-backend
# 修改 application.yml 中的数据库密码
mvn spring-boot:run
```

后端启动后访问: `http://localhost:8080/doc.html` 查看 API 文档

#### 3. 启动前端

```bash
cd blog-frontend
npm install
npm run dev
```

前端访问: `http://localhost:3000`

## 项目结构

```
smart-blog/
├── blog-backend/              # Spring Boot 后端
│   ├── src/main/java/com/blog/
│   │   ├── controller/        # 接口层
│   │   ├── service/           # 业务层
│   │   ├── mapper/            # 数据访问层
│   │   ├── entity/            # 实体类
│   │   ├── dto/               # 数据传输对象
│   │   ├── common/            # 统一响应、异常
│   │   ├── config/            # 配置类
│   │   ├── security/          # JWT 认证
│   │   ├── aspect/            # AOP 切面
│   │   └── annotation/        # 自定义注解
│   └── src/main/resources/
├── blog-frontend/             # Vue 3 前端
│   └── src/
│       ├── views/             # 页面
│       ├── api/               # 接口调用
│       ├── router/            # 路由
│       └── store/             # 状态管理
├── sql/                       # 建表脚本
└── docker-compose.yml         # Docker 编排
```

## API 接口

| 方法 | 路径 | 说明 | 需要登录 |
|------|------|------|----------|
| POST | /api/auth/register | 用户注册 | 否 |
| POST | /api/auth/login | 用户登录 | 否 |
| GET | /api/article/list | 文章列表 | 否 |
| GET | /api/article/{id} | 文章详情 | 否 |
| POST | /api/article | 发布文章 | 是 |
| PUT | /api/article/{id} | 更新文章 | 是 |
| DELETE | /api/article/{id} | 删除文章 | 是 |
| POST | /api/article/{id}/like | 点赞文章 | 是 |
| GET | /api/category/list | 分类列表 | 否 |
| GET | /api/comment/article/{id} | 评论列表 | 否 |
| POST | /api/comment/article/{id} | 发表评论 | 是 |

## 测试账号

| 用户名 | 密码 |
|--------|------|
| admin | 123456 |
| test | 123456 |

## 开发说明

### 自定义限流注解

```java
// 限制 60 秒内最多 10 次请求
@RateLimit(seconds = 60, maxCount = 10)
@PostMapping("/api/sensitive")
public Result<?> sensitiveApi() { ... }
```

### Redis 缓存使用

```java
// 文章详情带缓存, 1 小时过期
public Article getById(Long id) {
    String key = "article:" + id;
    Object cached = redisTemplate.opsForValue().get(key);
    if (cached != null) return (Article) cached;

    Article article = articleMapper.selectById(id);
    redisTemplate.opsForValue().set(key, article, 1, TimeUnit.HOURS);
    return article;
}
```

## License

MIT
