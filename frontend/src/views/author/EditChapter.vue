<template>
  <div class="edit-chapter-page">
    <h1>编辑章节</h1>
    
    <el-card v-if="chapter">
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        style="max-width: 1000px"
      >
        <el-form-item label="章节标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入章节标题" />
        </el-form-item>
        
        <el-form-item label="章节序号" prop="chapterNumber">
          <el-input-number
            v-model="form.chapterNumber"
            :min="1"
            placeholder="请输入章节序号"
          />
        </el-form-item>
        
        <el-form-item label="章节内容" prop="content">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="20"
            placeholder="请输入章节内容"
          />
        </el-form-item>
        
        <el-form-item label="是否免费">
          <el-radio-group v-model="form.isFree">
            <el-radio :label="1">免费</el-radio>
            <el-radio :label="0">收费</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">已发布</el-radio>
            <el-radio :label="0">草稿</el-radio>
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
import { chapterApi } from '@/utils/api'

const route = useRoute()
const router = useRouter()

const formRef = ref(null)
const loading = ref(false)
const chapter = ref(null)

const form = reactive({
  title: '',
  chapterNumber: 1,
  content: '',
  isFree: 1,
  status: 1
})

const rules = {
  title: [
    { required: true, message: '请输入章节标题', trigger: 'blur' }
  ],
  chapterNumber: [
    { required: true, message: '请输入章节序号', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入章节内容', trigger: 'blur' }
  ]
}

const loadChapter = async () => {
  try {
    const chapterId = route.params.id
    chapter.value = await chapterApi.getDetail(chapterId)
    
    // 填充表单
    form.title = chapter.value.title
    form.chapterNumber = chapter.value.chapterNumber
    form.content = chapter.value.content || ''
    form.isFree = chapter.value.isFree ? 1 : 0
    form.status = chapter.value.status
  } catch (error) {
    console.error('加载章节失败', error)
    ElMessage.error('加载失败')
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await chapterApi.update(route.params.id, form)
        ElMessage.success('保存成功')
        router.push(`/author/book/${chapter.value.bookId}/chapters`)
      } catch (error) {
        console.error('保存失败', error)
      } finally {
        loading.value = false
      }
    }
  })
}

onMounted(() => {
  loadChapter()
})
</script>

<style scoped>
.edit-chapter-page h1 {
  margin-bottom: 20px;
  font-size: 24px;
  color: #333;
}
</style>

