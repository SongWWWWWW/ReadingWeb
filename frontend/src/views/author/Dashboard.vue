<template>
  <div class="dashboard-page">
    <h1>工作台</h1>
    
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="12" :sm="8" :md="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalBooks }}</div>
            <div class="stat-label">我的书籍</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="8" :md="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalChapters }}</div>
            <div class="stat-label">总章节数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="8" :md="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalReadCount }}</div>
            <div class="stat-label">总阅读量</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="8" :md="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalFavoriteCount }}</div>
            <div class="stat-label">总收藏数</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最近更新的书籍 -->
    <el-card class="section-card">
      <template #header>
        <div class="card-header">
          <span>最近更新的书籍</span>
          <el-button type="text" @click="$router.push('/author/books')">查看全部</el-button>
        </div>
      </template>
      <el-table :data="recentBooks" style="width: 100%">
        <el-table-column prop="title" label="书名" />
        <el-table-column prop="chapterCount" label="章节数" width="100" />
        <el-table-column prop="readCount" label="阅读量" width="100" />
        <el-table-column prop="favoriteCount" label="收藏数" width="100" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.isCompleted" type="success">完结</el-tag>
            <el-tag v-else type="warning">连载</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="text" @click="$router.push(`/author/book/${row.id}/chapters`)">
              管理章节
            </el-button>
            <el-button type="text" @click="$router.push(`/author/book/edit/${row.id}`)">
              编辑
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { bookApi } from '@/utils/api'

const stats = ref({
  totalBooks: 0,
  totalChapters: 0,
  totalReadCount: 0,
  totalFavoriteCount: 0
})

const recentBooks = ref([])

const loadData = async () => {
  try {
    const res = await bookApi.getMyBooks({ current: 1, size: 10 })
    recentBooks.value = res.records || []
    
    // 计算统计数据
    stats.value.totalBooks = res.total || 0
    stats.value.totalChapters = recentBooks.value.reduce((sum, book) => sum + (book.chapterCount || 0), 0)
    stats.value.totalReadCount = recentBooks.value.reduce((sum, book) => sum + (book.readCount || 0), 0)
    stats.value.totalFavoriteCount = recentBooks.value.reduce((sum, book) => sum + (book.favoriteCount || 0), 0)
  } catch (error) {
    console.error('加载数据失败', error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.dashboard-page h1 {
  margin-bottom: 20px;
  font-size: 24px;
  color: #333;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  text-align: center;
}

.stat-content {
  padding: 20px 0;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 10px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.section-card {
  margin-top: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

