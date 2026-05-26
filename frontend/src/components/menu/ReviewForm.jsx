import { useState } from 'react'
import { useCreateReview } from '../../hooks/useMenus'
import StarRating from '../common/StarRating'

const EMPTY_FORM = { nickname: '', pin: '', rating: 5, comment: '' }

const inputClass = 'border border-gray-300 rounded-md px-3 py-1.5 text-sm focus:outline-none focus:ring-2 focus:ring-orange-400'

export default function ReviewForm({ menuId }) {
  const [form, setForm] = useState(EMPTY_FORM)
  const [error, setError] = useState('')
  const createReview = useCreateReview(menuId)

  function handleSubmit(e) {
    e.preventDefault()
    if (!form.nickname || !form.pin) {
      setError('닉네임과 PIN은 필수입니다.')
      return
    }
    setError('')
    createReview.mutate(
      { nickname: form.nickname, pin: form.pin, rating: form.rating, comment: form.comment || null },
      {
        onSuccess: () => setForm(EMPTY_FORM),
        onError: (err) => setError(err.response?.data?.message || '작성에 실패했습니다.'),
      }
    )
  }

  return (
    <form onSubmit={handleSubmit} className="border rounded-lg p-4 bg-white shadow-sm space-y-3">
      <h3 className="text-sm font-semibold text-gray-700">리뷰 작성</h3>

      <div className="flex gap-3">
        <input
          value={form.nickname}
          onChange={e => setForm(p => ({ ...p, nickname: e.target.value }))}
          placeholder="닉네임"
          className={`flex-1 ${inputClass}`}
        />
        <input
          value={form.pin}
          onChange={e => setForm(p => ({ ...p, pin: e.target.value }))}
          placeholder="PIN 4자리"
          maxLength={4}
          className={`w-28 ${inputClass}`}
        />
      </div>

      <div className="flex items-center gap-2">
        <span className="text-sm text-gray-600">별점</span>
        <StarRating value={form.rating} onChange={v => setForm(p => ({ ...p, rating: v }))} />
      </div>

      <input
        value={form.comment}
        onChange={e => setForm(p => ({ ...p, comment: e.target.value }))}
        placeholder="한줄평 (선택)"
        className={`w-full ${inputClass}`}
      />

      {error && <p className="text-xs text-red-500">{error}</p>}

      <button
        type="submit"
        disabled={createReview.isPending}
        className="bg-orange-500 hover:bg-orange-600 disabled:bg-orange-300 text-white text-sm font-medium px-4 py-2 rounded-md transition-colors"
      >
        {createReview.isPending ? '작성 중...' : '리뷰 작성'}
      </button>
    </form>
  )
}
