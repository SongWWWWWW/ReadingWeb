<template>
  <div class="books-page">
    <div class="page-header">
      <h1>我的书籍</h1>
      <el-button type="primary" @click="$router.push('/author/book/create')">
        <el-icon><Plus /></el-icon>
        创建新书
      </el-button>
    </div>

    <!-- 书籍列表 -->
    <el-card>
      <el-table :data="bookList" style="width: 100%">
        <el-table-column prop="coverImage" label="封面" width="100">
          <template #default="{ row }">
            <img :src="row.coverImage || '/default-cover.jpg'" :alt="row.title" class="cover-img" />
          </template>
        </el-table-column>
        <el-table-column prop="title" label="书名" />
        <el-table-column prop="categoryName" label="分类" width="100" />
        <el-table-column prop="chapterCount" label="章节数" width="100" />
        <el-table-column prop="wordCount" label="字数" width="100">
          <template #default="{ row }">
            {{ formatNumber(row.wordCount) }}
          </template>
        </el-table-column>
        <el-table-column prop="readCount" label="阅读量" width="100" />
        <el-table-column prop="favoriteCount" label="收藏数" width="100" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.isCompleted" type="success">完结</el-tag>
            <el-tag v-else type="warning">连载</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" fixed="right">
          <template #default="{ row }">
            <el-button type="text" @click="$router.push(`/author/book/${row.id}/chapters`)">
              章节管理
            </el-button>
            <el-button type="text" @click="$router.push(`/author/book/edit/${row.id}`)">
              编辑
            </el-button>
            <el-button type="text" danger @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
      <el-pagination
        v-model:current-page="queryParams.current"
        v-model:page-size="queryParams.size"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadBooks"
        @current-change="loadBooks"
      />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { bookApi } from '@/utils/api'

const bookList = ref([])
const total = ref(0)

const queryParams = reactive({
  current: 1,
  size: 10
})

const loadBooks = async () => {
  try {
    const res = await bookApi.getMyBooks(queryParams)
    bookList.value = res.records || []
    total.value = res.total || 0
  } catch (error) {
    console.error('加载书籍失败', error)
  }
}

const handleDelete = async (book) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除《${book.title}》吗？此操作不可恢复！`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await bookApi.delete(book.id)
    ElMessage.success('删除成功')
    loadBooks()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败', error)
    }
  }
}

const formatNumber = (num) => {
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + '万'
  }
  return num
}

onMounted(() => {
  loadBooks()
})
</script>

<style scoped>
.books-page {
  padding: 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 0;
  font-size: 24px;
  color: #333;
}

.cover-img {
  width: 60px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>

