<template>
  <div class="container">
    <el-card>
      <template #header>
        <h2>{{ isEdit ? '编辑文章' : '发布文章' }}</h2>
      </template>
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="请输入文章标题" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryId" placeholder="选择分类" style="width: 200px">
            <el-option
              v-for="cat in categories"
              :key="cat.id"
              :label="cat.name"
              :value="cat.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="摘要">
          <el-input
            v-model="form.summary"
            type="textarea"
            :rows="2"
            placeholder="文章摘要(选填)"
          />
        </el-form-item>
        <el-form-item label="内容">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="20"
            placeholder="支持 Markdown 语法"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="loading">
            {{ isEdit ? '更新' : '发布' }}
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
import { getArticle, createArticle, updateArticle } from '../api/article'
import { getCategories } from '../api/category'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()

const isEdit = ref(false)
const loading = ref(false)
const categories = ref([])

const form = reactive({
  title: '',
  categoryId: null,
  summary: '',
  content: '',
  status: 1
})

onMounted(async () => {
  await loadCategories()
  if (route.params.id) {
    isEdit.value = true
    const res = await getArticle(route.params.id)
    Object.assign(form, res.data)
  }
})

async function loadCategories() {
  const res = await getCategories()
  categories.value = res.data
}

async function handleSubmit() {
  if (!form.title || !form.content) {
    ElMessage.warning('标题和内容不能为空')
    return
  }
  loading.value = true
  try {
    if (isEdit.value) {
      await updateArticle(route.params.id, form)
      ElMessage.success('更新成功')
    } else {
      await createArticle(form)
      ElMessage.success('发布成功')
    }
    router.push('/my')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.container {
  max-width: 900px;
  margin: 20px auto;
  padding: 0 20px;
}
</style>
