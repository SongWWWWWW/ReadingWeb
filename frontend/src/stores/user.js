import { defineStore } from 'pinia'
import { ref } from 'vue'
import { authApi, userApi } from '@/utils/api'
import router from '@/router'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(null)

  // 登录
  const login = async (loginData) => {
    const data = await authApi.login(loginData)
    token.value = data.token
    localStorage.setItem('token', data.token)
    await getUserInfo()
    return data
  }

  // 注册
  const register = async (registerData) => {
    await authApi.register(registerData)
  }

  // 获取用户信息
  const getUserInfo = async () => {
    if (!token.value) return
    try {
      userInfo.value = await userApi.getInfo()
    } catch (error) {
      console.error('获取用户信息失败', error)
    }
  }

  // 登出
  const logout = () => {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    router.push('/login')
  }

  // 初始化时获取用户信息
  if (token.value) {
    getUserInfo()
  }

  return {
    token,
    userInfo,
    login,
    register,
    getUserInfo,
    logout
  }
})

