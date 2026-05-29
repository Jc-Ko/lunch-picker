import { create } from 'zustand'

const defaultCondition = {
  categoryMode: 'simple',
  category: null,
  weights: {},
  minRating: null,
  priceRange: null,
  distance: null,
}

const usePickerStore = create((set) => ({
  condition: { ...defaultCondition },
  setCondition: (key, value) =>
    set((state) => ({ condition: { ...state.condition, [key]: value } })),
  resetCondition: () => set({ condition: { ...defaultCondition } }),
}))

export default usePickerStore
