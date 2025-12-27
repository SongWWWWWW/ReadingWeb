<template>
  <div class="chapter-page">
    <div class="container">
      <div v-if="chapter" class="chapter-content">
        <!-- 章节头部 -->
        <div class="chapter-header">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/books' }">书库</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: `/book/${book?.id}` }">{{ book?.title }}</el-breadcrumb-item>
            <el-breadcrumb-item>{{ chapter.title }}</el-breadcrumb-item>
          </el-breadcrumb>
          <h1 class="chapter-title">{{ chapter.title }}</h1>
          <div class="chapter-meta">
            <span>字数：{{ formatNumber(chapter.wordCount) }}</span>
            <span>阅读量：{{ formatNumber(chapter.readCount) }}</span>
          </div>
        </div>

        <!-- 章节内容 -->
        <div class="chapter-body">
          <div class="content" v-html="formatContent(chapter.content)"></div>
        </div>

        <!-- 章节导航 -->
        <div class="chapter-nav">
          <el-button
            v-if="prevChapter"
            @click="readChapter(prevChapter.id)"
          >
            <el-icon><ArrowLeft /></el-icon>
            上一章
          </el-button>
          <el-button @click="$router.push(`/book/${book?.id}`)">
            返回目录
          </el-button>
          <el-button
            v-if="nextChapter"
            type="primary"
            @click="readChapter(nextChapter.id)"
          >
            下一章
            <el-icon><ArrowRight /></el-icon>
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft, ArrowRight } from '@element-plus/icons-vue'
import { chapterApi, bookApi, readingRecordApi } from '@/utils/api'

const route = useRoute()
const router = useRouter()

const chapter = ref(null)
const book = ref(null)
const prevChapter = ref(null)
const nextChapter = ref(null)

const loadChapter = async () => {
  try {
    const chapterId = route.params.id
    chapter.value = await chapterApi.getDetail(chapterId)
    
    // 加载书籍信息
    if (chapter.value.bookId) {
      book.value = await bookApi.getDetail(chapter.value.bookId)
      
      // 加载章节列表以获取上下章
      const chapterRes = await chapterApi.getList(chapter.value.bookId, { current: 1, size: 1000 })
      const allChapters = chapterRes.records || []
      const currentIndex = allChapters.findIndex(c => c.id === chapterId)
      
      if (currentIndex > 0) {
        prevChapter.value = allChapters[currentIndex - 1]
      }
      if (currentIndex < allChapters.length - 1) {
        nextChapter.value = allChapters[currentIndex + 1]
      }
    }
    
    // 保存阅读记录
    if (book.value) {
      try {
        await readingRecordApi.save({
          bookId: book.value.id,
          chapterId: chapterId,
          progress: 100
        })
      } catch (error) {
        // 忽略错误
      }
    }
  } catch (error) {
    console.error('加载章节失败', error)
  }
}

const readChapter = (chapterId) => {
  router.push(`/chapter/${chapterId}`)
}

const formatContent = (content) => {
  if (!content) return ''
  return content.replace(/\n/g, '<br/>')
}

const formatNumber = (num) => {
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + '万'
  }
  return num
}

onMounted(() => {
  loadChapter()
})
</script>

<style scoped>
.chapter-page {
  padding: 20px 0;
}

.chapter-content {
  background: #fff;
  padding: 40px;
  border-radius: 8px;
  max-width: 900px;
  margin: 0 auto;
}

.chapter-header {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e0e0e0;
}

.chapter-title {
  font-size: 24px;
  margin: 15px 0;
  color: #333;
}

.chapter-meta {
  display: flex;
  gap: 20px;
  font-size: 14px;
  color: #999;
}

.chapter-body {
  margin-bottom: 40px;
}

.content {
  line-height: 2;
  font-size: 16px;
  color: #333;
  text-indent: 2em;
}

.chapter-nav {
  display: flex;
  justify-content: space-between;
  padding-top: 20px;
  border-top: 1px solid #e0e0e0;
}

@media (max-width: 768px) {
  .chapter-content {
    padding: 20px;
  }
  
  .chapter-nav {
    flex-direction: column;
    gap: 10px;
  }
  
  .chapter-nav .el-button {
    width: 100%;
  }
}
</style>

