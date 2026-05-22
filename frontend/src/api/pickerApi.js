import axiosInstance from './axiosInstance'

export const pickerApi = {
  pick: (params) => axiosInstance.get('/api/picker/pick', { params }),
}
