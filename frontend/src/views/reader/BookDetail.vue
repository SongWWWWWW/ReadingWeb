<template>
  <div class="book-detail-page">
    <div class="container">
      <div v-if="book" class="book-detail">
        <!-- 书籍信息 -->
        <div class="book-header">
          <div class="book-cover-large">
            <img :src="book.coverImage || '/default-cover.jpg'" :alt="book.title" />
          </div>
          <div class="book-info">
            <h1 class="book-title">{{ book.title }}</h1>
            <div class="book-meta">
              <span class="author">作者：{{ book.author }}</span>
              <span class="category">分类：{{ book.categoryName }}</span>
              <el-tag v-if="book.isCompleted" type="success">完结</el-tag>
              <el-tag v-else type="warning">连载中</el-tag>
            </div>
            <div class="book-stats">
              <span>字数：{{ formatNumber(book.wordCount) }}</span>
              <span>章节：{{ book.chapterCount }}</span>
              <span>阅读：{{ formatNumber(book.readCount) }}</span>
              <span>收藏：{{ formatNumber(book.favoriteCount) }}</span>
            </div>
            <p class="book-description">{{ book.description }}</p>
            <div class="book-actions">
              <el-button
                v-if="readingRecord"
                type="primary"
                @click="continueReading"
              >
                继续阅读
              </el-button>
              <el-button
                v-else
                type="primary"
                @click="startReading"
              >
                开始阅读
              </el-button>
              <el-button
                :type="isFavorite ? 'danger' : 'default'"
                @click="toggleFavorite"
              >
                {{ isFavorite ? '取消收藏' : '加入收藏' }}
              </el-button>
            </div>
          </div>
        </div>

        <!-- 章节列表 -->
        <div class="chapters-section">
          <h2>目录</h2>
          <div class="chapters-list">
            <div
              v-for="chapter in chapters"
              :key="chapter.id"
              class="chapter-item"
              @click="readChapter(chapter.id)"
            >
              <span class="chapter-number">{{ chapter.chapterNumber }}</span>
              <span class="chapter-title">{{ chapter.title }}</span>
              <span class="chapter-word-count">{{ formatNumber(chapter.wordCount) }}字</span>
              <el-tag v-if="chapter.isFree" type="success" size="small">免费</el-tag>
            </div>
          </div>
          <div v-if="chapters.length === 0" class="empty">
            暂无章节
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { bookApi, chapterApi, readingRecordApi, favoriteApi } from '@/utils/api'

const route = useRoute()
const router = useRouter()

const book = ref(null)
const chapters = ref([])
const readingRecord = ref(null)
const isFavorite = ref(false)

const loadBookDetail = async () => {
  try {
    const bookId = route.params.id
    book.value = await bookApi.getDetail(bookId)
    
    // 加载章节列表
    const chapterRes = await chapterApi.getList(bookId, { current: 1, size: 1000 })
    chapters.value = chapterRes.records || []
    
    // 加载阅读记录
    try {
      readingRecord.value = await readingRecordApi.getByBook(bookId)
    } catch (error) {
      // 没有阅读记录
    }
    
    // 检查是否收藏
    try {
      const favoriteRes = await favoriteApi.check(bookId)
      isFavorite.value = favoriteRes || false
    } catch (error) {
      // 未登录或未收藏
    }
  } catch (error) {
    console.error('加载书籍详情失败', error)
    ElMessage.error('加载失败')
  }
}

const startReading = () => {
  if (chapters.value.length > 0) {
    readChapter(chapters.value[0].id)
  } else {
    ElMessage.warning('暂无章节')
  }
}

const continueReading = () => {
  if (readingRecord.value && readingRecord.value.chapterId) {
    readChapter(readingRecord.value.chapterId)
  } else {
    startReading()
  }
}

const readChapter = (chapterId) => {
  router.push(`/chapter/${chapterId}`)
}

const toggleFavorite = async () => {
  try {
    if (isFavorite.value) {
      await favoriteApi.remove(route.params.id)
      isFavorite.value = false
      ElMessage.success('已取消收藏')
    } else {
      await favoriteApi.add(route.params.id)
      isFavorite.value = true
      ElMessage.success('收藏成功')
    }
  } catch (error) {
    console.error('操作失败', error)
  }
}

const formatNumber = (num) => {
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + '万'
  }
  return num
}

onMounted(() => {
  loadBookDetail()
})
</script>

<style scoped>
.book-detail-page {
  padding: 20px 0;
}

.book-header {
  display: flex;
  gap: 30px;
  background: #fff;
  padding: 30px;
  border-radius: 8px;
  margin-bottom: 30px;
}

.book-cover-large {
  flex-shrink: 0;
  width: 200px;
  height: 280px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.book-cover-large img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.book-info {
  flex: 1;
}

.book-title {
  font-size: 28px;
  margin: 0 0 15px;
  color: #333;
}

.book-meta {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 15px;
  font-size: 14px;
  color: #666;
}

.book-stats {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  font-size: 14px;
  color: #666;
}

.book-description {
  line-height: 1.8;
  color: #666;
  margin-bottom: 20px;
}

.book-actions {
  display: flex;
  gap: 15px;
}

.chapters-section {
  background: #fff;
  padding: 30px;
  border-radius: 8px;
}

.chapters-section h2 {
  margin: 0 0 20px;
  font-size: 20px;
  color: #333;
}

.chapters-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.chapter-item {
  display: flex;
  align-items: center;
  padding: 15px;
  background: #f9f9f9;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.chapter-item:hover {
  background: #f0f0f0;
  transform: translateX(5px);
}

.chapter-number {
  width: 40px;
  color: #999;
  font-size: 14px;
}

.chapter-title {
  flex: 1;
  color: #333;
  font-size: 14px;
}

.chapter-word-count {
  margin-right: 10px;
  color: #999;
  font-size: 12px;
}

.empty {
  text-align: center;
  padding: 40px;
  color: #999;
}

@media (max-width: 768px) {
  .book-header {
    flex-direction: column;
  }
  
  .book-cover-large {
    width: 150px;
    height: 210px;
    margin: 0 auto;
  }
}
</style>

