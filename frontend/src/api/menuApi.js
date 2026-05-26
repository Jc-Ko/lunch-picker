import axiosInstance from './axiosInstance'

export const menuApi = {
  getMenus: (params) => {
    const p = {
      category: params.category || undefined,
      priceRange: params.priceRange || undefined,
      distance: params.distance || undefined,
    }
    return axiosInstance.get('/api/menus', { params: p }).then(r => r.data.data)
  },
  getMenu: (id) =>
    axiosInstance.get(`/api/menus/${id}`).then(r => r.data.data),
  createMenu: (body) =>
    axiosInstance.post('/api/menus', body).then(r => r.data.data),
  updateMenu: (id, body) =>
    axiosInstance.put(`/api/menus/${id}`, body).then(r => r.data.data),
  deleteMenu: (id) =>
    axiosInstance.delete(`/api/menus/${id}`).then(r => r.data),
  eatMenu: (id) =>
    axiosInstance.patch(`/api/menus/${id}/eat`).then(r => r.data.data),

  getReviews: (menuId) =>
    axiosInstance.get(`/api/menus/${menuId}/reviews`).then(r => r.data.data),
  createReview: (menuId, body) =>
    axiosInstance.post(`/api/menus/${menuId}/reviews`, body).then(r => r.data.data),
  updateReview: (id, body) =>
    axiosInstance.put(`/api/reviews/${id}`, body).then(r => r.data.data),
  deleteReview: (id, pin) =>
    axiosInstance.delete(`/api/reviews/${id}`, { data: { pin } }).then(r => r.data),
}
