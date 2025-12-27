<template>
  <div class="home-page">
    <div class="container">
      <!-- 轮播图/推荐区域 -->
      <el-carousel height="300px" class="banner">
        <el-carousel-item v-for="item in banners" :key="item.id">
          <div class="banner-item" @click="$router.push(`/book/${item.id}`)">
            <img :src="item.coverImage" :alt="item.title" />
            <div class="banner-info">
              <h3>{{ item.title }}</h3>
              <p>{{ item.description }}</p>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>

      <!-- 分类导航 -->
      <div class="categories">
        <h2>分类浏览</h2>
        <div class="category-list">
          <div
            v-for="category in categories"
            :key="category.id"
            class="category-item"
            @click="goToCategory(category.id)"
          >
            {{ category.name }}
          </div>
        </div>
      </div>

      <!-- 热门推荐 -->
      <div class="section">
        <h2>热门推荐</h2>
        <el-row :gutter="20">
          <el-col
            v-for="book in hotBooks"
            :key="book.id"
            :xs="12"
            :sm="8"
            :md="6"
            :lg="4"
          >
            <div class="book-card" @click="$router.push(`/book/${book.id}`)">
              <div class="book-cover">
                <img :src="book.coverImage || '/default-cover.jpg'" :alt="book.title" />
              </div>
              <div class="book-info">
                <h3 class="book-title">{{ book.title }}</h3>
                <p class="book-author">{{ book.author }}</p>
                <div class="book-stats">
                  <span>阅读 {{ book.readCount }}</span>
                  <span>收藏 {{ book.favoriteCount }}</span>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 最新更新 -->
      <div class="section">
        <h2>最新更新</h2>
        <el-row :gutter="20">
          <el-col
            v-for="book in latestBooks"
            :key="book.id"
            :xs="12"
            :sm="8"
            :md="6"
            :lg="4"
          >
            <div class="book-card" @click="$router.push(`/book/${book.id}`)">
              <div class="book-cover">
                <img :src="book.coverImage || '/default-cover.jpg'" :alt="book.title" />
              </div>
              <div class="book-info">
                <h3 class="book-title">{{ book.title }}</h3>
                <p class="book-author">{{ book.author }}</p>
                <div class="book-stats">
                  <span>阅读 {{ book.readCount }}</span>
                  <span>收藏 {{ book.favoriteCount }}</span>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { bookApi, categoryApi } from '@/utils/api'

const router = useRouter()

const banners = ref([])
const categories = ref([])
const hotBooks = ref([])
const latestBooks = ref([])

const loadData = async () => {
  try {
    // 加载分类
    categories.value = await categoryApi.getList()
    
    // 加载热门书籍
    const hotRes = await bookApi.getList({ current: 1, size: 8, orderBy: 'readCount' })
    hotBooks.value = hotRes.records || []
    
    // 加载最新书籍
    const latestRes = await bookApi.getList({ current: 1, size: 8, orderBy: 'updateTime' })
    latestBooks.value = latestRes.records || []
    
    // 设置轮播图（取前3本热门书籍）
    banners.value = hotBooks.value.slice(0, 3)
  } catch (error) {
    console.error('加载数据失败', error)
  }
}

const goToCategory = (categoryId) => {
  router.push({ path: '/books', query: { categoryId } })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.home-page {
  padding: 20px 0;
}

.banner {
  margin-bottom: 40px;
  border-radius: 8px;
  overflow: hidden;
}

.banner-item {
  position: relative;
  height: 100%;
  cursor: pointer;
}

.banner-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.banner-info {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
  color: #fff;
  padding: 30px 20px 20px;
}

.banner-info h3 {
  margin: 0 0 10px;
  font-size: 24px;
}

.banner-info p {
  margin: 0;
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.categories {
  margin-bottom: 40px;
}

.categories h2 {
  margin-bottom: 20px;
  font-size: 20px;
  color: #333;
}

.category-list {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.category-item {
  padding: 10px 20px;
  background: #fff;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid #e0e0e0;
}

.category-item:hover {
  background: #409eff;
  color: #fff;
  border-color: #409eff;
}

.section {
  margin-bottom: 40px;
}

.section h2 {
  margin-bottom: 20px;
  font-size: 20px;
  color: #333;
}

.book-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.book-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.book-cover {
  width: 100%;
  height: 200px;
  overflow: hidden;
  background: #f5f5f5;
}

.book-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.book-info {
  padding: 15px;
}

.book-title {
  font-size: 16px;
  margin: 0 0 8px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.book-author {
  font-size: 14px;
  color: #666;
  margin: 0 0 10px;
}

.book-stats {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #999;
}
</style>

