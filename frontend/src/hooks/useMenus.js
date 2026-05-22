import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { menuApi } from '../api/menuApi'

// TODO: Dev A — 각 훅 구현 완성

export function useMenus(params) {
  // TODO: Dev A — queryKey에 params 포함, select로 response.data.data 추출
  return useQuery({
    queryKey: ['menus', params],
    queryFn: () => menuApi.getMenus(params),
  })
}

export function useMenu(id) {
  // TODO: Dev A — id가 없으면 enabled: false
  return useQuery({
    queryKey: ['menus', id],
    queryFn: () => menuApi.getMenu(id),
    enabled: !!id,
  })
}

export function useCreateMenu() {
  const queryClient = useQueryClient()
  // TODO: Dev A — 성공 시 ['menus'] 쿼리 invalidate
  return useMutation({
    mutationFn: (body) => menuApi.createMenu(body),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['menus'] }),
  })
}

export function useUpdateMenu() {
  const queryClient = useQueryClient()
  // TODO: Dev A — 성공 시 ['menus'] 쿼리 invalidate
  return useMutation({
    mutationFn: ({ id, body }) => menuApi.updateMenu(id, body),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['menus'] }),
  })
}

export function useDeleteMenu() {
  const queryClient = useQueryClient()
  // TODO: Dev A — 성공 시 ['menus'] 쿼리 invalidate
  return useMutation({
    mutationFn: (id) => menuApi.deleteMenu(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['menus'] }),
  })
}

export function useReviews(menuId) {
  // TODO: Dev A — menuId가 없으면 enabled: false
  return useQuery({
    queryKey: ['reviews', menuId],
    queryFn: () => menuApi.getReviews(menuId),
    enabled: !!menuId,
  })
}

export function useCreateReview(menuId) {
  const queryClient = useQueryClient()
  // TODO: Dev A — 성공 시 ['reviews', menuId] 쿼리 invalidate
  return useMutation({
    mutationFn: (body) => menuApi.createReview(menuId, body),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['reviews', menuId] }),
  })
}
