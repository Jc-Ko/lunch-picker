import axiosInstance from './axiosInstance'

export const pickerApi = {
  pick: (params) => {
    const p = { categoryMode: params.categoryMode }

    if (params.categoryMode === 'simple') {
      if (params.category && params.category !== '전체' && params.category !== '상관없음') {
        p.category = params.category
      }
    } else {
      if (params.koreanWeight > 0)  p.koreanWeight  = params.koreanWeight
      if (params.westernWeight > 0) p.westernWeight = params.westernWeight
      if (params.chineseWeight > 0) p.chineseWeight = params.chineseWeight
    }

    if (params.minRating != null) p.minRating = params.minRating
    if (params.priceRange && params.priceRange !== '상관없음') p.priceRange = params.priceRange
    if (params.distance  && params.distance  !== '상관없음') p.distance  = params.distance

    return axiosInstance.get('/api/picker/pick', { params: p }).then(r => r.data.data)
  },
}
