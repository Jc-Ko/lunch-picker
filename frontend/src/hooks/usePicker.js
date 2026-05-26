import { useQuery } from '@tanstack/react-query'
import { pickerApi } from '../api/pickerApi'

// pickCount가 바뀔 때마다 queryKey가 달라져 "다시 뽑기"도 항상 새 결과를 가져온다.
export function usePick(params, pickCount) {
  return useQuery({
    queryKey: ['pick', params, pickCount],
    queryFn: () => pickerApi.pick(params),
    enabled: pickCount > 0,
    retry: false,
  })
}
