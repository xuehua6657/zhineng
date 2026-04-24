import request from './request'

export const getArticles = (params) => request.get('/article/list', { params })
export const getArticle = (id) => request.get(`/article/${id}`)
export const createArticle = (data) => request.post('/article', data)
export const updateArticle = (id, data) => request.put(`/article/${id}`, data)
export const deleteArticle = (id) => request.delete(`/article/${id}`)
export const getMyArticles = (params) => request.get('/article/my', { params })
export const likeArticle = (id) => request.post(`/article/${id}/like`)
export const unLikeArticle = (id) => request.delete(`/article/${id}/like`)
