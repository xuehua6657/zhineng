import request from './request'

export const getComments = (articleId) => request.get(`/comment/article/${articleId}`)
export const createComment = (articleId, data) => request.post(`/comment/article/${articleId}`, data)
export const deleteComment = (id) => request.delete(`/comment/${id}`)
export const likeComment = (id) => request.post(`/comment/${id}/like`)
