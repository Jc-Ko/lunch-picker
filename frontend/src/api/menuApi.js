import axiosInstance from './axiosInstance'

export const menuApi = {
  getMenus: (params) => axiosInstance.get('/api/menus', { params }),
  getMenu: (id) => axiosInstance.get(`/api/menus/${id}`),
  createMenu: (body) => axiosInstance.post('/api/menus', body),
  updateMenu: (id, body) => axiosInstance.put(`/api/menus/${id}`, body),
  deleteMenu: (id) => axiosInstance.delete(`/api/menus/${id}`),
  eatMenu: (id) => axiosInstance.patch(`/api/menus/${id}/eat`),

  getReviews: (menuId) => axiosInstance.get(`/api/menus/${menuId}/reviews`),
  createReview: (menuId, body) => axiosInstance.post(`/api/menus/${menuId}/reviews`, body),
  updateReview: (id, body) => axiosInstance.put(`/api/reviews/${id}`, body),
  deleteReview: (id, pin) => axiosInstance.delete(`/api/reviews/${id}`, { params: { pin } }),
}
