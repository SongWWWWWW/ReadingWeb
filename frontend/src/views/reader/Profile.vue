<template>
  <div class="profile-page">
    <div class="container">
      <div class="profile-content">
        <h1>个人中心</h1>
        
        <!-- 用户信息 -->
        <el-card class="user-card">
          <div class="user-info">
            <el-avatar :size="80" :src="userStore.userInfo?.avatar">
              {{ userStore.userInfo?.nickname?.[0] }}
            </el-avatar>
            <div class="user-details">
              <h2>{{ userStore.userInfo?.nickname }}</h2>
              <p>用户名：{{ userStore.userInfo?.username }}</p>
              <p>邮箱：{{ userStore.userInfo?.email }}</p>
            </div>
          </div>
        </el-card>

        <!-- 我的收藏 -->
        <el-card class="section-card">
          <template #header>
            <div class="card-header">
              <span>我的收藏</span>
              <el-button type="text" @click="loadFavorites">刷新</el-button>
            </div>
          </template>
          <div v-if="favorites.length > 0" class="favorites-list">
            <div
              v-for="item in favorites"
              :key="item.id"
              class="favorite-item"
              @click="$router.push(`/book/${item.bookId}`)"
            >
              <img :src="item.coverImage || '/default-cover.jpg'" :alt="item.title" />
              <div class="item-info">
                <h3>{{ item.title }}</h3>
                <p>{{ item.author }}</p>
              </div>
              <el-button
                type="danger"
                size="small"
                @click.stop="removeFavorite(item.bookId)"
              >
                取消收藏
              </el-button>
            </div>
          </div>
          <el-empty v-else description="暂无收藏" />
        </el-card>

        <!-- 阅读记录 -->
        <el-card class="section-card">
          <template #header>
            <div class="card-header">
              <span>阅读记录</span>
              <el-button type="text" @click="loadReadingRecords">刷新</el-button>
            </div>
          </template>
          <div v-if="readingRecords.length > 0" class="records-list">
            <div
              v-for="record in readingRecords"
              :key="record.id"
              class="record-item"
              @click="continueReading(record)"
            >
              <img :src="record.coverImage || '/default-cover.jpg'" :alt="record.title" />
              <div class="item-info">
                <h3>{{ record.title }}</h3>
                <p>阅读进度：{{ record.progress }}%</p>
                <p class="time">最后阅读：{{ formatTime(record.lastReadTime) }}</p>
              </div>
              <el-button type="primary" size="small">继续阅读</el-button>
            </div>
          </div>
          <el-empty v-else description="暂无阅读记录" />
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { favoriteApi, readingRecordApi } from '@/utils/api'

const router = useRouter()
const userStore = useUserStore()

const favorites = ref([])
const readingRecords = ref([])

const loadFavorites = async () => {
  try {
    const res = await favoriteApi.getList({ current: 1, size: 100 })
    favorites.value = res.records || []
  } catch (error) {
    console.error('加载收藏失败', error)
  }
}

const loadReadingRecords = async () => {
  try {
    const res = await readingRecordApi.getList({ current: 1, size: 100 })
    readingRecords.value = res.records || []
  } catch (error) {
    console.error('加载阅读记录失败', error)
  }
}

const removeFavorite = async (bookId) => {
  try {
    await favoriteApi.remove(bookId)
    ElMessage.success('已取消收藏')
    loadFavorites()
  } catch (error) {
    console.error('取消收藏失败', error)
  }
}

const continueReading = (record) => {
  if (record.chapterId) {
    router.push(`/chapter/${record.chapterId}`)
  } else {
    router.push(`/book/${record.bookId}`)
  }
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleString('zh-CN')
}

onMounted(() => {
  loadFavorites()
  loadReadingRecords()
})
</script>

<style scoped>
.profile-page {
  padding: 20px 0;
}

.profile-content {
  max-width: 1000px;
  margin: 0 auto;
}

.profile-content h1 {
  margin-bottom: 20px;
  font-size: 24px;
  color: #333;
}

.user-card {
  margin-bottom: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-details h2 {
  margin: 0 0 10px;
  font-size: 20px;
  color: #333;
}

.user-details p {
  margin: 5px 0;
  color: #666;
  font-size: 14px;
}

.section-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.favorites-list,
.records-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.favorite-item,
.record-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px;
  background: #f9f9f9;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.favorite-item:hover,
.record-item:hover {
  background: #f0f0f0;
}

.favorite-item img,
.record-item img {
  width: 80px;
  height: 110px;
  object-fit: cover;
  border-radius: 4px;
}

.item-info {
  flex: 1;
}

.item-info h3 {
  margin: 0 0 8px;
  font-size: 16px;
  color: #333;
}

.item-info p {
  margin: 5px 0;
  font-size: 14px;
  color: #666;
}

.item-info .time {
  font-size: 12px;
  color: #999;
}
</style>

