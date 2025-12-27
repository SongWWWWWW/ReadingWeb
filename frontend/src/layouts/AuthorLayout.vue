<template>
  <div class="author-layout">
    <el-container>
      <el-aside width="200px" class="sidebar">
        <div class="logo">
          <h2>作者中心</h2>
        </div>
        <el-menu
          :default-active="activeMenu"
          router
          class="sidebar-menu"
        >
          <el-menu-item index="/author/dashboard">
            <el-icon><DataBoard /></el-icon>
            <span>工作台</span>
          </el-menu-item>
          <el-menu-item index="/author/books">
            <el-icon><Reading /></el-icon>
            <span>我的书籍</span>
          </el-menu-item>
          <el-menu-item index="/author/book/create">
            <el-icon><Plus /></el-icon>
            <span>创建新书</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header class="header">
          <div class="header-content">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/author/dashboard' }">作者中心</el-breadcrumb-item>
              <el-breadcrumb-item>{{ route.meta.title || '页面' }}</el-breadcrumb-item>
            </el-breadcrumb>
            <div class="user-info">
              <el-button type="text" @click="$router.push('/')">返回首页</el-button>
              <el-dropdown @command="handleCommand">
                <span class="user-dropdown">
                  <el-avatar :size="32" :src="userStore.userInfo?.avatar">
                    {{ userStore.userInfo?.nickname?.[0] }}
                  </el-avatar>
                  <span>{{ userStore.userInfo?.nickname }}</span>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
        </el-header>
        <el-main class="main-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { DataBoard, Reading, Plus } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)

const handleCommand = (command) => {
  if (command === 'logout') {
    userStore.logout()
  }
}
</script>

<style scoped>
.author-layout {
  min-height: 100vh;
  background: #f5f5f5;
}

.sidebar {
  background: #001529;
  min-height: 100vh;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  border-bottom: 1px solid #1f1f1f;
}

.logo h2 {
  margin: 0;
  font-size: 18px;
}

.sidebar-menu {
  border: none;
  background: #001529;
}

.sidebar-menu .el-menu-item {
  color: rgba(255, 255, 255, 0.65);
}

.sidebar-menu .el-menu-item:hover,
.sidebar-menu .el-menu-item.is-active {
  color: #fff;
  background: #1890ff;
}

.header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0 20px;
  height: 60px;
  line-height: 60px;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.main-content {
  padding: 20px;
  background: #fff;
  margin: 20px;
  border-radius: 4px;
  min-height: calc(100vh - 100px);
}
</style>

