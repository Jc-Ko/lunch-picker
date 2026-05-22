import { create } from 'zustand'

const useMenuStore = create((set) => ({
  filter: {
    category: '',
    priceRange: '',
    distance: '',
  },
  setFilter: (key, value) =>
    set((state) => ({ filter: { ...state.filter, [key]: value } })),
  resetFilter: () =>
    set({ filter: { category: '', priceRange: '', distance: '' } }),
}))

export default useMenuStore
