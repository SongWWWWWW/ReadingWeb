import request from './request'

// 认证相关
export const authApi = {
  login: (data) => request.post('/auth/login', data),
  register: (data) => request.post('/auth/register', data)
}

// 用户相关
export const userApi = {
  getInfo: () => request.get('/user/info'),
  updateInfo: (data) => request.put('/user/info', data)
}

// 书籍相关
export const bookApi = {
  getList: (params) => request.get('/book/list', { params }),
  getDetail: (id) => request.get(`/book/${id}`),
  create: (data) => request.post('/book', data),
  update: (id, data) => request.put(`/book/${id}`, data),
  delete: (id) => request.delete(`/book/${id}`),
  getMyBooks: (params) => request.get('/book/my', { params })
}

// 分类相关
export const categoryApi = {
  getList: () => request.get('/category/list')
}

// 章节相关
export const chapterApi = {
  getList: (bookId, params) => request.get(`/chapter/book/${bookId}`, { params }),
  getDetail: (id) => request.get(`/chapter/${id}`),
  create: (data) => request.post('/chapter', data),
  update: (id, data) => request.put(`/chapter/${id}`, data),
  delete: (id) => request.delete(`/chapter/${id}`)
}

// 阅读记录相关
export const readingRecordApi = {
  save: (data) => request.post('/reading-record', data),
  getList: (params) => request.get('/reading-record/list', { params }),
  getByBook: (bookId) => request.get(`/reading-record/book/${bookId}`)
}

// 收藏相关
export const favoriteApi = {
  add: (bookId) => request.post(`/favorite/${bookId}`),
  remove: (bookId) => request.delete(`/favorite/${bookId}`),
  getList: (params) => request.get('/favorite/list', { params }),
  check: (bookId) => request.get(`/favorite/check/${bookId}`)
}

