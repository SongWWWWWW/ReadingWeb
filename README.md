# ReadingWeb

前后端分离的文学阅读网站项目

## 项目简介

这是一个经典的文学阅读网站项目，采用前后端分离架构：
- **后端**: Spring Boot + MyBatis Plus + MySQL + Redis
- **前端**: Vue 3 + Vite + Element Plus

项目包含读者端和作者端两部分功能，实现了完整的书籍阅读、管理功能。

## 技术栈

### 后端技术栈
- **Java**: 11
- **Spring Boot**: 2.7.18
- **MyBatis Plus**: 3.5.5
- **MySQL**: 8.0
- **Redis**: 用于缓存和会话管理
- **JWT**: 用于身份认证
- **Lombok**: 简化JavaBean开发
- **Maven**: 项目构建工具

### 前端技术栈
- **Vue 3**: 渐进式JavaScript框架
- **Vite**: 下一代前端构建工具
- **Vue Router**: 官方路由管理器
- **Pinia**: 状态管理
- **Element Plus**: UI组件库
- **Axios**: HTTP客户端

## 项目结构

```
ReadingWeb/
├── src/                          # 后端代码
│   ├── main/
│   │   ├── java/com/readingweb/
│   │   │   ├── controller/      # 控制器层（三层架构）
│   │   │   ├── service/           # 服务层
│   │   │   ├── mapper/          # 数据访问层
│   │   │   ├── entity/          # 实体类
│   │   │   ├── dto/             # 数据传输对象
│   │   │   └── ...
│   │   └── resources/
│   │       ├── application.yml
│   │       └── db/schema.sql
│   └── test/
└── frontend/                     # 前端代码
    ├── src/
    │   ├── layouts/             # 布局组件
    │   ├── views/               # 页面组件
    │   │   ├── reader/          # 读者端页面
    │   │   └── author/          # 作者端页面
    │   ├── router/              # 路由配置
    │   ├── stores/              # 状态管理
    │   └── utils/               # 工具函数
    ├── package.json
    └── vite.config.js
```

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### 2. 配置数据库

修改 `src/main/resources/application.yml` 中的数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/reading_web?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 3. 配置Redis

修改 `src/main/resources/application.yml` 中的Redis配置：

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password: your_password
```

### 4. 运行项目

```bash
# 编译项目
mvn clean install

# 运行项目
mvn spring-boot:run
```

或者直接运行主类 `ReadingWebApplication`

### 5. 初始化数据库

执行 `src/main/resources/db/schema.sql` 创建数据库表。

### 6. 启动前端项目

```bash
cd frontend
npm install
npm run dev
```

访问 http://localhost:3000

### 7. 测试接口

**测试接口：**
```
GET http://localhost:8080/api/test/hello
```

**用户注册：**
```
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "123456",
  "email": "test@example.com",
  "nickname": "测试用户"
}
```

**用户登录：**
```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "123456"
}
```

**获取用户信息（需要token）：**
```
GET http://localhost:8080/api/user/info
Authorization: Bearer {your_token}
```

**获取所有分类：**
```
GET http://localhost:8080/api/category/list
```

**创建书籍（需要token）：**
```
POST http://localhost:8080/api/book
Authorization: Bearer {your_token}
Content-Type: application/json

{
  "title": "测试书籍",
  "author": "测试作者",
  "description": "这是一本测试书籍",
  "categoryId": 1,
  "tags": "玄幻,热血",
  "wordCount": 100000,
  "isCompleted": 0
}
```

**分页查询书籍：**
```
GET http://localhost:8080/api/book/list?current=1&size=10&title=测试&categoryId=1&orderBy=readCount&orderDirection=desc
```

**根据ID查询书籍：**
```
GET http://localhost:8080/api/book/{id}
```

**创建章节（需要token）：**
```
POST http://localhost:8080/api/chapter
Authorization: Bearer {your_token}
Content-Type: application/json

{
  "bookId": 1,
  "title": "第一章 开始",
  "chapterNumber": 1,
  "content": "章节内容...",
  "isFree": 1
}
```

**查询书籍的章节列表：**
```
GET http://localhost:8080/api/chapter/book/{bookId}?current=1&size=20
```

**查询章节详情：**
```
GET http://localhost:8080/api/chapter/{id}
```

**保存阅读记录（需要token）：**
```
POST http://localhost:8080/api/reading-record
Authorization: Bearer {your_token}
Content-Type: application/json

{
  "bookId": 1,
  "chapterId": 1,
  "progress": 50
}
```

**获取阅读记录（需要token）：**
```
GET http://localhost:8080/api/reading-record/list
Authorization: Bearer {your_token}
```

**添加收藏（需要token）：**
```
POST http://localhost:8080/api/favorite/{bookId}
Authorization: Bearer {your_token}
```

**获取收藏列表（需要token）：**
```
GET http://localhost:8080/api/favorite/list?current=1&size=10
Authorization: Bearer {your_token}
```

## 功能特性

### 后端功能
- ✅ RESTful API设计
- ✅ 三层架构（Controller-Service-Mapper）
- ✅ 统一响应结果封装
- ✅ 全局异常处理（包括参数校验异常）
- ✅ JWT身份认证（登录、注册）
- ✅ JWT拦截器自动验证token
- ✅ 密码加密（BCrypt）
- ✅ Redis缓存支持
- ✅ MyBatis Plus分页插件
- ✅ 跨域配置
- ✅ 日志配置
- ✅ 用户管理模块（注册、登录、查询）
- ✅ 书籍管理模块（CRUD、分页查询、多条件搜索、我的书籍）
- ✅ 分类管理模块
- ✅ 章节管理模块（CRUD、字数统计、自动更新书籍章节数）
- ✅ 阅读记录模块（保存进度、查询历史）
- ✅ 收藏功能模块（添加、取消、查询收藏列表）
- ✅ 参数校验（Validation）
- ✅ 分页结果封装

### 前端功能

#### 读者端
- ✅ 首页展示（轮播图、分类导航、热门推荐、最新更新）
- ✅ 书籍列表（搜索、筛选、分页）
- ✅ 书籍详情（信息展示、章节列表、收藏功能）
- ✅ 阅读页面（章节内容、上下章导航）
- ✅ 个人中心（收藏管理、阅读记录）

#### 作者端
- ✅ 工作台（数据统计、最近更新）
- ✅ 书籍管理（创建、编辑、删除、列表）
- ✅ 章节管理（创建、编辑、删除、列表）

## 开发规范

### 后端开发规范
- **Controller层**：处理HTTP请求，参数校验
- **Service层**：业务逻辑处理
- **Mapper层**：数据访问，使用MyBatis Plus
- **Entity层**：实体类，使用Lombok简化代码

### 前端开发规范
- 使用 Vue 3 Composition API
- 使用 `<script setup>` 语法
- 组件命名使用 PascalCase
- 路由守卫自动处理认证
- API请求统一使用 axios 封装

## License

MIT

