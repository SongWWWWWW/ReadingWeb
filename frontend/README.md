# ReadingWeb Frontend

文学阅读网站前端项目

## 技术栈

- **Vue 3**: 渐进式JavaScript框架
- **Vite**: 下一代前端构建工具
- **Vue Router**: 官方路由管理器
- **Pinia**: 状态管理
- **Element Plus**: UI组件库
- **Axios**: HTTP客户端

## 项目结构

```
frontend/
├── src/
│   ├── layouts/          # 布局组件
│   │   ├── ReaderLayout.vue    # 读者端布局
│   │   └── AuthorLayout.vue     # 作者端布局
│   ├── views/            # 页面组件
│   │   ├── reader/       # 读者端页面
│   │   │   ├── Home.vue         # 首页
│   │   │   ├── Books.vue        # 书籍列表
│   │   │   ├── BookDetail.vue   # 书籍详情
│   │   │   ├── Chapter.vue      # 阅读页面
│   │   │   └── Profile.vue      # 个人中心
│   │   ├── author/       # 作者端页面
│   │   │   ├── Dashboard.vue    # 工作台
│   │   │   ├── Books.vue        # 我的书籍
│   │   │   ├── CreateBook.vue   # 创建书籍
│   │   │   ├── EditBook.vue     # 编辑书籍
│   │   │   ├── Chapters.vue     # 章节管理
│   │   │   ├── CreateChapter.vue # 创建章节
│   │   │   └── EditChapter.vue  # 编辑章节
│   │   ├── Login.vue     # 登录页面
│   │   └── Register.vue  # 注册页面
│   ├── router/           # 路由配置
│   │   └── index.js
│   ├── stores/           # 状态管理
│   │   └── user.js
│   ├── utils/            # 工具函数
│   │   ├── api.js        # API接口封装
│   │   └── request.js    # HTTP请求封装
│   ├── App.vue           # 根组件
│   ├── main.js           # 入口文件
│   └── style.css         # 全局样式
├── index.html
├── package.json
├── vite.config.js
└── README.md
```

## 快速开始

### 1. 安装依赖

```bash
cd frontend
npm install
```

### 2. 开发运行

```bash
npm run dev
```

访问 http://localhost:3000

### 3. 构建生产版本

```bash
npm run build
```

## 功能特性

### 读者端功能
- ✅ 首页展示（轮播图、分类导航、热门推荐、最新更新）
- ✅ 书籍列表（搜索、筛选、分页）
- ✅ 书籍详情（信息展示、章节列表、收藏功能）
- ✅ 阅读页面（章节内容、上下章导航）
- ✅ 个人中心（收藏管理、阅读记录）

### 作者端功能
- ✅ 工作台（数据统计、最近更新）
- ✅ 书籍管理（创建、编辑、删除、列表）
- ✅ 章节管理（创建、编辑、删除、列表）

## API配置

前端通过代理访问后端API，配置在 `vite.config.js` 中：

```javascript
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

## 路由说明

### 读者端路由
- `/` - 首页
- `/books` - 书籍列表
- `/book/:id` - 书籍详情
- `/chapter/:id` - 阅读页面
- `/profile` - 个人中心（需登录）

### 作者端路由
- `/author/dashboard` - 工作台（需登录）
- `/author/books` - 我的书籍（需登录）
- `/author/book/create` - 创建书籍（需登录）
- `/author/book/edit/:id` - 编辑书籍（需登录）
- `/author/book/:id/chapters` - 章节管理（需登录）
- `/author/chapter/create/:bookId` - 创建章节（需登录）
- `/author/chapter/edit/:id` - 编辑章节（需登录）

### 认证路由
- `/login` - 登录
- `/register` - 注册

## 状态管理

使用 Pinia 管理用户状态：

- `userStore.token` - JWT token
- `userStore.userInfo` - 用户信息
- `userStore.login()` - 登录
- `userStore.logout()` - 登出
- `userStore.getUserInfo()` - 获取用户信息

## 开发规范

- 使用 Vue 3 Composition API
- 使用 `<script setup>` 语法
- 组件命名使用 PascalCase
- 路由守卫自动处理认证
- API请求统一使用 axios 封装

## License

MIT

