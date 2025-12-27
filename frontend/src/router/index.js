import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/',
    component: () => import('@/layouts/ReaderLayout.vue'),
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/views/reader/Home.vue')
      },
      {
        path: 'books',
        name: 'Books',
        component: () => import('@/views/reader/Books.vue')
      },
      {
        path: 'book/:id',
        name: 'BookDetail',
        component: () => import('@/views/reader/BookDetail.vue')
      },
      {
        path: 'chapter/:id',
        name: 'Chapter',
        component: () => import('@/views/reader/Chapter.vue')
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/reader/Profile.vue'),
        meta: { requiresAuth: true }
      }
    ]
  },
  {
    path: '/author',
    component: () => import('@/layouts/AuthorLayout.vue'),
    redirect: '/author/dashboard',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'dashboard',
        name: 'AuthorDashboard',
        component: () => import('@/views/author/Dashboard.vue')
      },
      {
        path: 'books',
        name: 'AuthorBooks',
        component: () => import('@/views/author/Books.vue')
      },
      {
        path: 'book/create',
        name: 'CreateBook',
        component: () => import('@/views/author/CreateBook.vue')
      },
      {
        path: 'book/edit/:id',
        name: 'EditBook',
        component: () => import('@/views/author/EditBook.vue')
      },
      {
        path: 'book/:id/chapters',
        name: 'BookChapters',
        component: () => import('@/views/author/Chapters.vue')
      },
      {
        path: 'chapter/create/:bookId',
        name: 'CreateChapter',
        component: () => import('@/views/author/CreateChapter.vue')
      },
      {
        path: 'chapter/edit/:id',
        name: 'EditChapter',
        component: () => import('@/views/author/EditChapter.vue')
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  if (to.meta.requiresAuth && !userStore.token) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
  } else {
    next()
  }
})

export default router

