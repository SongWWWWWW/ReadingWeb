<template>
  <div class="reader-layout">
    <el-header class="header">
      <div class="container">
        <div class="header-content">
          <div class="logo" @click="$router.push('/')">
            <h1>文学阅读</h1>
          </div>
          <el-menu
            mode="horizontal"
            :default-active="activeMenu"
            router
            class="nav-menu"
          >
            <el-menu-item index="/home">首页</el-menu-item>
            <el-menu-item index="/books">书库</el-menu-item>
          </el-menu>
          <div class="user-actions">
            <template v-if="userStore.token">
              <el-button type="text" @click="$router.push('/author')">
                作者中心
              </el-button>
              <el-dropdown @command="handleCommand">
                <span class="user-info">
                  <el-avatar :size="32" :src="userStore.userInfo?.avatar">
                    {{ userStore.userInfo?.nickname?.[0] }}
                  </el-avatar>
                  <span class="username">{{ userStore.userInfo?.nickname }}</span>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                    <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
            <template v-else>
              <el-button type="text" @click="$router.push('/login')">登录</el-button>
              <el-button type="primary" @click="$router.push('/register')">注册</el-button>
            </template>
          </div>
        </div>
      </div>
    </el-header>
    <el-main class="main-content">
      <router-view />
    </el-main>
    <el-footer class="footer">
      <div class="container">
        <p>&copy; 2024 文学阅读网站. All rights reserved.</p>
      </div>
    </el-footer>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)

const handleCommand = (command) => {
  if (command === 'profile') {
    router.push('/profile')
  } else if (command === 'logout') {
    userStore.logout()
  }
}
</script>

<style scoped>
.reader-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0;
  height: 60px;
  line-height: 60px;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
}

.logo {
  cursor: pointer;
}

.logo h1 {
  font-size: 24px;
  color: #409eff;
  margin: 0;
}

.nav-menu {
  flex: 1;
  border: none;
  margin: 0 40px;
}

.user-actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.username {
  font-size: 14px;
}

.main-content {
  flex: 1;
  padding: 0;
}

.footer {
  background: #f5f5f5;
  text-align: center;
  padding: 20px 0;
  margin-top: auto;
}

.footer p {
  margin: 0;
  color: #666;
}
</style>

