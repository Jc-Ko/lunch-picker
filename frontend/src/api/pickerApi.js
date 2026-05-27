import axiosInstance from './axiosInstance'

export const pickerApi = {
  pick: (params) => {
    if (params.categoryMode === 'weighted') {
      return axiosInstance.post('/api/picker/pick', {
        weights: params.weights || {},
        minRating: params.minRating,
        priceRange: params.priceRange,
        distance: params.distance,
      }).then(r => r.data.data)
    }

    const p = {}
    if (params.category) p.category = params.category
    if (params.minRating != null) p.minRating = params.minRating
    if (params.priceRange) p.priceRange = params.priceRange
    if (params.distance) p.distance = params.distance
    return axiosInstance.get('/api/picker/pick', { params: p }).then(r => r.data.data)
  },
}
