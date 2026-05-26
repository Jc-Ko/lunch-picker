import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { menuApi } from '../api/menuApi'

export function useMenus(params) {
  return useQuery({
    queryKey: ['menus', params],
    queryFn: () => menuApi.getMenus(params),
  })
}

export function useMenu(id) {
  return useQuery({
    queryKey: ['menus', id],
    queryFn: () => menuApi.getMenu(id),
    enabled: !!id,
  })
}

export function useCreateMenu() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (body) => menuApi.createMenu(body),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['menus'] }),
  })
}

export function useUpdateMenu() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: ({ id, body }) => menuApi.updateMenu(id, body),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['menus'] }),
  })
}

export function useDeleteMenu() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (id) => menuApi.deleteMenu(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['menus'] }),
  })
}

export function useEatMenu() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (id) => menuApi.eatMenu(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['menus'] }),
  })
}

export function useReviews(menuId) {
  return useQuery({
    queryKey: ['reviews', menuId],
    queryFn: () => menuApi.getReviews(menuId),
    enabled: !!menuId,
  })
}

export function useCreateReview(menuId) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (body) => menuApi.createReview(menuId, body),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['reviews', menuId] })
      queryClient.invalidateQueries({ queryKey: ['menus'] })
    },
  })
}

export function useUpdateReview(menuId) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: ({ id, body }) => menuApi.updateReview(id, body),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['reviews', menuId] })
      queryClient.invalidateQueries({ queryKey: ['menus'] })
    },
  })
}

export function useDeleteReview(menuId) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: ({ id, pin }) => menuApi.deleteReview(id, pin),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['reviews', menuId] })
      queryClient.invalidateQueries({ queryKey: ['menus'] })
    },
  })
}
