<template>
  <div class="container">
    <div class="main">
      <el-card v-if="article" class="article-card">
        <h1>{{ article.title }}</h1>
        <div class="article-meta">
          <span>浏览 {{ article.viewCount }}</span>
          <span>点赞 {{ article.likeCount }}</span>
          <span>{{ article.createdAt }}</span>
        </div>
        <el-divider />
        <div class="article-content" v-html="article.content"></div>
        <el-divider />
        <div class="actions">
          <el-button :type="liked ? 'danger' : ''" @click="handleLike">
            {{ liked ? '已点赞' : '点赞' }}
          </el-button>
        </div>
      </el-card>

      <!-- 评论区 -->
      <el-card v-if="article" class="comment-section">
        <template #header>评论</template>
        <div v-if="userStore.isLoggedIn" class="comment-input">
          <el-input
            v-model="commentContent"
            type="textarea"
            :rows="3"
            placeholder="写下你的评论..."
          />
          <el-button type="primary" @click="handleComment" style="margin-top: 8px">
            发表评论
          </el-button>
        </div>
        <el-alert v-else title="请先登录后再评论" type="info" :closable="false" />

        <div class="comment-list">
          <div v-for="comment in comments" :key="comment.id" class="comment-item">
            <p>{{ comment.content }}</p>
            <div class="comment-meta">
              <span>{{ comment.createdAt }}</span>
              <span>点赞 {{ comment.likeCount }}</span>
            </div>
          </div>
          <el-empty v-if="comments.length === 0" description="暂无评论" />
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getArticle, likeArticle } from '../api/article'
import { getComments, createComment, likeComment } from '../api/comment'
import { useUserStore } from '../store/user'
import { ElMessage } from 'element-plus'

const route = useRoute()
const userStore = useUserStore()

const article = ref(null)
const comments = ref([])
const commentContent = ref('')
const liked = ref(false)

onMounted(async () => {
  await loadArticle()
  await loadComments()
})

async function loadArticle() {
  const res = await getArticle(route.params.id)
  article.value = res.data
}

async function loadComments() {
  const res = await getComments(route.params.id)
  comments.value = res.data
}

async function handleComment() {
  if (!commentContent.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  await createComment(route.params.id, { content: commentContent.value })
  commentContent.value = ''
  ElMessage.success('评论成功')
  await loadComments()
}

async function handleLike() {
  await likeArticle(route.params.id)
  liked.value = true
  ElMessage.success('点赞成功')
}
</script>

<style scoped>
.container {
  max-width: 800px;
  margin: 20px auto;
  padding: 0 20px;
}

.article-card h1 {
  font-size: 24px;
  color: #303133;
}

.article-meta {
  display: flex;
  gap: 16px;
  color: #c0c4cc;
  font-size: 14px;
}

.article-content {
  line-height: 1.8;
  color: #303133;
}

.actions {
  display: flex;
  gap: 12px;
}

.comment-section {
  margin-top: 20px;
}

.comment-input {
  margin-bottom: 20px;
}

.comment-item {
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.comment-item p {
  margin-bottom: 8px;
  line-height: 1.6;
}

.comment-meta {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: #c0c4cc;
}
</style>
