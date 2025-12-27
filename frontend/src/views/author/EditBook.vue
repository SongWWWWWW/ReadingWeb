<template>
  <div class="edit-book-page">
    <h1>编辑书籍</h1>
    
    <el-card v-if="book">
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        style="max-width: 800px"
      >
        <el-form-item label="书名" prop="title">
          <el-input v-model="form.title" placeholder="请输入书名" />
        </el-form-item>
        
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类" style="width: 100%">
            <el-option
              v-for="category in categories"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="标签" prop="tags">
          <el-input
            v-model="form.tags"
            placeholder="多个标签用逗号分隔"
          />
        </el-form-item>
        
        <el-form-item label="简介" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="6"
            placeholder="请输入书籍简介"
          />
        </el-form-item>
        
        <el-form-item label="封面图片" prop="coverImage">
          <el-input v-model="form.coverImage" placeholder="请输入封面图片URL" />
        </el-form-item>
        
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">上架</el-radio>
            <el-radio :label="0">下架</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="是否完结">
          <el-radio-group v-model="form.isCompleted">
            <el-radio :label="0">连载中</el-radio>
            <el-radio :label="1">已完结</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleSubmit">
            保存
          </el-button>
          <el-button @click="$router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { bookApi, categoryApi } from '@/utils/api'

const route = useRoute()
const router = useRouter()

const formRef = ref(null)
const loading = ref(false)
const book = ref(null)
const categories = ref([])

const form = reactive({
  title: '',
  categoryId: '',
  tags: '',
  description: '',
  coverImage: '',
  status: 1,
  isCompleted: 0
})

const rules = {
  title: [
    { required: true, message: '请输入书名', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择分类', trigger: 'change' }
  ],
  description: [
    { required: true, message: '请输入简介', trigger: 'blur' }
  ]
}

const loadBook = async () => {
  try {
    const bookId = route.params.id
    book.value = await bookApi.getDetail(bookId)
    
    // 填充表单
    form.title = book.value.title
    form.categoryId = book.value.categoryId
    form.tags = book.value.tags || ''
    form.description = book.value.description || ''
    form.coverImage = book.value.coverImage || ''
    form.status = book.value.status
    form.isCompleted = book.value.isCompleted ? 1 : 0
  } catch (error) {
    console.error('加载书籍失败', error)
    ElMessage.error('加载失败')
  }
}

const loadCategories = async () => {
  try {
    categories.value = await categoryApi.getList()
  } catch (error) {
    console.error('加载分类失败', error)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await bookApi.update(route.params.id, form)
        ElMessage.success('保存成功')
        router.push('/author/books')
      } catch (error) {
        console.error('保存失败', error)
      } finally {
        loading.value = false
      }
    }
  })
}

onMounted(() => {
  loadCategories()
  loadBook()
})
</script>

<style scoped>
.edit-book-page h1 {
  margin-bottom: 20px;
  font-size: 24px;
  color: #333;
}
</style>

