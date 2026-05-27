import axiosInstance from './axiosInstance'

export const commonCodeApi = {
  getCodes: (group) =>
    axiosInstance.get('/api/common-codes', { params: { group } }).then(r => r.data.data),
}
