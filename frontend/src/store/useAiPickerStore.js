import { create } from 'zustand'

const useAiPickerStore = create((set) => ({
  userInput: '',
  setUserInput: (value) => set({ userInput: value }),
  resetInput: () => set({ userInput: '' }),
}))

export default useAiPickerStore
