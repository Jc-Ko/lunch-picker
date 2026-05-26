import axiosInstance from './axiosInstance'

export const aiApi = {
  recommend: (body) => axiosInstance.post('/api/ai/recommend', body, { timeout: 30000 }),
  getHistory: (limit = 20) => axiosInstance.get('/api/ai/history', { params: { limit } }),
}
