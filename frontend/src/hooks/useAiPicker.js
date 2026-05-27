import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { aiApi } from '../api/aiApi'

export function useRecommend() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: async (body) => {
      const res = await aiApi.recommend(body)
      return res.data.data
    },
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['aiHistory'] }),
  })
}

export function useAiHistory(limit) {
  return useQuery({
    queryKey: ['aiHistory', limit],
    queryFn: () => aiApi.getHistory(limit),
    select: (res) => res.data.data,
  })
}

export function useDeleteHistory() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: () => aiApi.deleteHistory(),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['aiHistory'] }),
  })
}
