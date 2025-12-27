<template>
  <div class="chapters-page">
    <div class="page-header">
      <div>
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/author/books' }">我的书籍</el-breadcrumb-item>
          <el-breadcrumb-item>{{ book?.title }}</el-breadcrumb-item>
        </el-breadcrumb>
        <h1>{{ book?.title }} - 章节管理</h1>
      </div>
      <el-button type="primary" @click="$router.push(`/author/chapter/create/${bookId}`)">
        <el-icon><Plus /></el-icon>
        添加章节
      </el-button>
    </div>

    <el-card>
      <el-table :data="chapterList" style="width: 100%">
        <el-table-column prop="chapterNumber" label="序号" width="80" />
        <el-table-column prop="title" label="章节标题" />
        <el-table-column prop="wordCount" label="字数" width="100">
          <template #default="{ row }">
            {{ formatNumber(row.wordCount) }}
          </template>
        </el-table-column>
        <el-table-column prop="readCount" label="阅读量" width="100" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">已发布</el-tag>
            <el-tag v-else type="info">草稿</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="是否免费" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.isFree" type="success">免费</el-tag>
            <el-tag v-else type="warning">收费</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="text" @click="$router.push(`/author/chapter/edit/${row.id}`)">
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
        @size-change="loadChapters"
        @current-change="loadChapters"
      />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { chapterApi, bookApi } from '@/utils/api'

const route = useRoute()
const router = useRouter()

const bookId = route.params.id
const book = ref(null)
const chapterList = ref([])
const total = ref(0)

const queryParams = reactive({
  current: 1,
  size: 20
})

const loadBook = async () => {
  try {
    book.value = await bookApi.getDetail(bookId)
  } catch (error) {
    console.error('加载书籍失败', error)
  }
}

const loadChapters = async () => {
  try {
    const res = await chapterApi.getList(bookId, queryParams)
    chapterList.value = res.records || []
    total.value = res.total || 0
  } catch (error) {
    console.error('加载章节失败', error)
  }
}

const handleDelete = async (chapter) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除《${chapter.title}》吗？此操作不可恢复！`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await chapterApi.delete(chapter.id)
    ElMessage.success('删除成功')
    loadChapters()
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
  loadBook()
  loadChapters()
})
</script>

<style scoped>
.chapters-page {
  padding: 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 10px 0 0;
  font-size: 24px;
  color: #333;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>

