<template>
  <div class="container">
    <el-card>
      <template #header>
        <h2>我的文章</h2>
      </template>
      <el-table :data="articles" stripe>
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="viewCount" label="浏览" width="80" />
        <el-table-column prop="likeCount" label="点赞" width="80" />
        <el-table-column prop="createdAt" label="发布时间" width="180" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button link type="primary" @click="$router.push(`/edit/${row.id}`)">编辑</el-button>
            <el-popconfirm title="确定删除?" @confirm="handleDelete(row.id)">
              <template #reference>
                <el-button link type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-if="total > 0"
        class="pagination"
        background
        layout="prev, pager, next"
        :current-page="page"
        :page-size="size"
        :total="total"
        @current-change="handlePageChange"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyArticles, deleteArticle } from '../api/article'
import { ElMessage } from 'element-plus'

const articles = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(10)

onMounted(loadArticles)

async function loadArticles() {
  const res = await getMyArticles({ page: page.value, size: size.value })
  articles.value = res.data.records
  total.value = res.data.total
}

function handlePageChange(newPage) {
  page.value = newPage
  loadArticles()
}

async function handleDelete(id) {
  await deleteArticle(id)
  ElMessage.success('删除成功')
  loadArticles()
}
</script>

<style scoped>
.container {
  max-width: 1000px;
  margin: 20px auto;
  padding: 0 20px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
