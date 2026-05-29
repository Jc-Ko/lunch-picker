import { useQuery } from '@tanstack/react-query'
import { commonCodeApi } from '../api/commonCodeApi'

export function useCommonCodes(group) {
  return useQuery({
    queryKey: ['commonCodes', group],
    queryFn: () => commonCodeApi.getCodes(group),
    staleTime: 1000 * 60 * 60,
  })
}
