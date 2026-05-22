import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { aiApi } from '../api/aiApi'

// TODO: Dev C — 훅 구현 완성

export function useRecommend() {
  const queryClient = useQueryClient()
  // TODO: Dev C — 성공 시 ['aiHistory'] 쿼리 invalidate
  return useMutation({
    mutationFn: (body) => aiApi.recommend(body),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['aiHistory'] }),
  })
}

export function useAiHistory(limit) {
  // TODO: Dev C — select로 response.data.data 추출
  return useQuery({
    queryKey: ['aiHistory', limit],
    queryFn: () => aiApi.getHistory(limit),
  })
}
