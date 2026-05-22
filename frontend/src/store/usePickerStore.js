import { create } from 'zustand'

const defaultCondition = {
  categoryMode: 'simple',
  category: '상관없음',
  koreanWeight: 33,
  westernWeight: 33,
  chineseWeight: 33,
  minRating: null,
  priceRange: '상관없음',
  distance: '상관없음',
}

const usePickerStore = create((set) => ({
  condition: { ...defaultCondition },
  setCondition: (key, value) =>
    set((state) => ({ condition: { ...state.condition, [key]: value } })),
  resetCondition: () => set({ condition: { ...defaultCondition } }),
}))

export default usePickerStore
