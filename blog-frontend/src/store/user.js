import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const nickname = ref(localStorage.getItem('nickname') || '')
  const userId = ref(localStorage.getItem('userId') || '')

  function setToken(t) {
    token.value = t
    localStorage.setItem('token', t)
  }

  function setUserInfo(info) {
    nickname.value = info.nickname || ''
    userId.value = info.id || ''
    localStorage.setItem('nickname', nickname.value)
    localStorage.setItem('userId', userId.value)
  }

  function logout() {
    token.value = ''
    nickname.value = ''
    userId.value = ''
    localStorage.removeItem('token')
    localStorage.removeItem('nickname')
    localStorage.removeItem('userId')
  }

  const isLoggedIn = () => !!token.value

  return { token, nickname, userId, setToken, setUserInfo, logout, isLoggedIn }
})
