<template>
  <div>
    <!-- 导航栏 -->
    <el-header class="header">
      <div class="header-content">
        <router-link to="/" class="logo">Smart Blog</router-link>
        <div class="nav-right">
          <el-input
            v-model="keyword"
            placeholder="搜索文章..."
            class="search-input"
            @keyup.enter="handleSearch"
            clearable
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
          <template v-if="userStore.isLoggedIn">
            <router-link to="/edit">
              <el-button type="primary">写文章</el-button>
            </router-link>
            <el-dropdown>
              <span class="user-name">{{ userStore.nickname }}</span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="$router.push('/my')">我的文章</el-dropdown-item>
                  <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <router-link to="/login">
              <el-button>登录</el-button>
            </router-link>
            <router-link to="/register">
              <el-button type="primary">注册</el-button>
            </router-link>
          </template>
        </div>
      </div>
    </el-header>

    <!-- 主内容 -->
    <div class="container">
      <div class="main">
        <!-- 分类标签 -->
        <el-tabs v-model="activeCategory" @tab-click="handleSearch">
          <el-tab-pane label="全部" :name="0" />
          <el-tab-pane
            v-for="cat in categories"
            :key="cat.id"
            :label="cat.name"
            :name="cat.id"
          />
        </el-tabs>

        <!-- 文章列表 -->
        <div class="article-list">
          <div v-for="article in articles" :key="article.id" class="article-card">
            <router-link :to="`/article/${article.id}`" class="article-link">
              <h3>{{ article.title }}</h3>
              <p>{{ article.summary || '暂无摘要' }}</p>
            </router-link>
            <div class="article-meta">
              <span>浏览 {{ article.viewCount }}</span>
              <span>点赞 {{ article.likeCount }}</span>
              <span>评论 {{ article.commentCount }}</span>
              <span>{{ article.createdAt }}</span>
            </div>
          </div>
          <el-empty v-if="articles.length === 0" description="暂无文章" />
        </div>

        <!-- 分页 -->
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
      </div>

      <!-- 侧边栏 -->
      <div class="sidebar">
        <el-card>
          <template #header>关于</template>
          <p>Smart Blog - Spring Boot 3 + Vue 3 全栈项目</p>
          <p class="tech-stack">技术栈: Spring Boot / MyBatis-Plus / Redis / Vue 3 / Element Plus</p>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getArticles } from '../api/article'
import { getCategories } from '../api/category'
import { useUserStore } from '../store/user'

const router = useRouter()
const userStore = useUserStore()

const articles = ref([])
const categories = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const keyword = ref('')
const activeCategory = ref(0)

onMounted(async () => {
  await loadCategories()
  await loadArticles()
})

async function loadCategories() {
  const res = await getCategories()
  categories.value = res.data
}

async function loadArticles() {
  const params = {
    page: page.value,
    size: size.value,
    keyword: keyword.value || undefined,
    categoryId: activeCategory.value > 0 ? activeCategory.value : undefined
  }
  const res = await getArticles(params)
  articles.value = res.data.records
  total.value = res.data.total
}

function handleSearch() {
  page.value = 1
  loadArticles()
}

function handlePageChange(newPage) {
  page.value = newPage
  loadArticles()
}

function handleLogout() {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 60px;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.search-input {
  width: 200px;
}

.user-name {
  cursor: pointer;
  color: #606266;
}

.container {
  max-width: 1200px;
  margin: 20px auto;
  display: flex;
  gap: 20px;
  padding: 0 20px;
}

.main {
  flex: 1;
  min-width: 0;
}

.sidebar {
  width: 280px;
  flex-shrink: 0;
}

.article-card {
  background: #fff;
  padding: 20px;
  margin-bottom: 12px;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.article-card h3 {
  font-size: 18px;
  margin-bottom: 8px;
  color: #303133;
}

.article-card h3:hover {
  color: #409eff;
}

.article-card p {
  color: #909399;
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-meta {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: #c0c4cc;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.tech-stack {
  margin-top: 12px;
  font-size: 12px;
  color: #909399;
}
</style>
