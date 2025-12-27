<template>
  <div class="books-page">
    <div class="container">
      <div class="page-header">
        <h1>书库</h1>
      </div>

      <!-- 搜索和筛选 -->
      <div class="filter-bar">
        <el-input
          v-model="queryParams.title"
          placeholder="搜索书名、作者"
          clearable
          style="width: 300px"
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-select
          v-model="queryParams.categoryId"
          placeholder="选择分类"
          clearable
          style="width: 150px"
          @change="handleSearch"
        >
          <el-option
            v-for="category in categories"
            :key="category.id"
            :label="category.name"
            :value="category.id"
          />
        </el-select>
        <el-select
          v-model="queryParams.status"
          placeholder="状态"
          clearable
          style="width: 120px"
          @change="handleSearch"
        >
          <el-option label="连载中" value="0" />
          <el-option label="已完结" value="1" />
        </el-select>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
      </div>

      <!-- 书籍列表 -->
      <div class="books-list">
        <el-row :gutter="20">
          <el-col
            v-for="book in bookList"
            :key="book.id"
            :xs="12"
            :sm="8"
            :md="6"
            :lg="4"
          >
            <div class="book-card" @click="$router.push(`/book/${book.id}`)">
              <div class="book-cover">
                <img :src="book.coverImage || '/default-cover.jpg'" :alt="book.title" />
                <div class="book-status">
                  <el-tag v-if="book.isCompleted" type="success" size="small">完结</el-tag>
                  <el-tag v-else type="warning" size="small">连载</el-tag>
                </div>
              </div>
              <div class="book-info">
                <h3 class="book-title">{{ book.title }}</h3>
                <p class="book-author">{{ book.author }}</p>
                <p class="book-desc">{{ book.description }}</p>
                <div class="book-stats">
                  <span>阅读 {{ book.readCount }}</span>
                  <span>收藏 {{ book.favoriteCount }}</span>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 分页 -->
      <div class="pagination">
      <el-pagination
        v-model:current-page="queryParams.current"
        v-model:page-size="queryParams.size"
        :total="total"
        :page-sizes="[12, 24, 48, 96]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { Search } from '@element-plus/icons-vue'
import { bookApi, categoryApi } from '@/utils/api'

const route = useRoute()

const categories = ref([])
const bookList = ref([])
const total = ref(0)

const queryParams = reactive({
  current: 1,
  size: 12,
  title: '',
  categoryId: route.query.categoryId || '',
  status: '',
  orderBy: 'updateTime'
})

const loadCategories = async () => {
  try {
    categories.value = await categoryApi.getList()
  } catch (error) {
    console.error('加载分类失败', error)
  }
}

const loadBooks = async () => {
  try {
    const res = await bookApi.getList(queryParams)
    bookList.value = res.records || []
    total.value = res.total || 0
  } catch (error) {
    console.error('加载书籍失败', error)
  }
}

const handleSearch = () => {
  queryParams.current = 1
  loadBooks()
}

const handleSizeChange = () => {
  queryParams.current = 1
  loadBooks()
}

const handlePageChange = () => {
  loadBooks()
}

onMounted(() => {
  loadCategories()
  loadBooks()
})
</script>

<style scoped>
.books-page {
  padding: 20px 0;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h1 {
  font-size: 24px;
  color: #333;
  margin: 0;
}

.filter-bar {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.books-list {
  min-height: 400px;
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
  position: relative;
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

.book-status {
  position: absolute;
  top: 10px;
  right: 10px;
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
  margin: 0 0 8px;
}

.book-desc {
  font-size: 12px;
  color: #999;
  margin: 0 0 10px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  min-height: 32px;
}

.book-stats {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #999;
}

.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}
</style>

