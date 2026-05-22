import { useQuery } from '@tanstack/react-query'
import { pickerApi } from '../api/pickerApi'

// TODO: Dev B — 훅 구현 완성

export function usePick(params, enabled) {
  // TODO: Dev B — enabled 조건 확인 (뽑기 버튼 클릭 시에만 호출)
  // TODO: Dev B — params에서 "상관없음" 값 필터링 처리
  return useQuery({
    queryKey: ['pick', params],
    queryFn: () => pickerApi.pick(params),
    enabled: !!enabled,
  })
}
