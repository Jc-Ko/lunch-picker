import { useState } from 'react'
import { useUpdateReview, useDeleteReview } from '../../hooks/useMenus'
import StarRating from '../common/StarRating'

export default function ReviewItem({ review, menuId }) {
  const [mode, setMode] = useState('view') // 'view' | 'editing' | 'deleting'
  const [editForm, setEditForm] = useState({ rating: review.rating, comment: review.comment ?? '', pin: '' })
  const [deletePin, setDeletePin] = useState('')
  const [error, setError] = useState('')

  const updateReview = useUpdateReview(menuId)
  const deleteReview = useDeleteReview(menuId)

  function handleEditSubmit(e) {
    e.preventDefault()
    setError('')
    updateReview.mutate(
      { id: review.id, body: { rating: editForm.rating, comment: editForm.comment, pin: editForm.pin } },
      {
        onSuccess: () => { setMode('view'); setEditForm(f => ({ ...f, pin: '' })) },
        onError: (err) => setError(err.response?.data?.message || '수정에 실패했습니다.'),
      }
    )
  }

  function handleDeleteSubmit(e) {
    e.preventDefault()
    setError('')
    deleteReview.mutate(
      { id: review.id, pin: deletePin },
      {
        onError: (err) => setError(err.response?.data?.message || '삭제에 실패했습니다.'),
      }
    )
  }

  function handleCancel() {
    setMode('view')
    setError('')
    setEditForm({ rating: review.rating, comment: review.comment ?? '', pin: '' })
    setDeletePin('')
  }

  const formattedDate = new Date(review.createdAt).toLocaleDateString('ko-KR')

  const inputClass = 'border border-gray-300 rounded-md px-3 py-1.5 text-sm focus:outline-none focus:ring-2 focus:ring-orange-400'

  return (
    <div className="border-b py-4">
      {mode === 'view' && (
        <>
          <div className="flex items-center justify-between mb-1">
            <div className="flex items-center gap-2">
              <span className="font-medium text-gray-800 text-sm">{review.nickname}</span>
              <StarRating value={review.rating} readonly />
            </div>
            <span className="text-xs text-gray-400">{formattedDate}</span>
          </div>
          {review.comment && <p className="text-sm text-gray-600 mb-2">{review.comment}</p>}
          <div className="flex gap-3">
            <button onClick={() => setMode('editing')} className="text-xs text-blue-500 hover:text-blue-700">수정</button>
            <button onClick={() => setMode('deleting')} className="text-xs text-red-400 hover:text-red-600">삭제</button>
          </div>
        </>
      )}

      {mode === 'editing' && (
        <form onSubmit={handleEditSubmit} className="space-y-2">
          <div className="flex items-center gap-3">
            <span className="text-sm font-medium text-gray-700">{review.nickname}</span>
            <StarRating value={editForm.rating} onChange={v => setEditForm(p => ({ ...p, rating: v }))} />
          </div>
          <input
            value={editForm.comment}
            onChange={e => setEditForm(p => ({ ...p, comment: e.target.value }))}
            placeholder="한줄평"
            className={`w-full ${inputClass}`}
          />
          <div className="flex items-center gap-2">
            <input
              value={editForm.pin}
              onChange={e => setEditForm(p => ({ ...p, pin: e.target.value }))}
              placeholder="PIN 4자리"
              maxLength={4}
              className={`w-28 ${inputClass}`}
            />
            <button type="submit" disabled={updateReview.isPending}
              className="text-sm text-white bg-orange-500 hover:bg-orange-600 disabled:bg-orange-300 px-3 py-1.5 rounded-md">
              저장
            </button>
            <button type="button" onClick={handleCancel} className="text-sm text-gray-500 hover:text-gray-700">취소</button>
          </div>
          {error && <p className="text-xs text-red-500">{error}</p>}
        </form>
      )}

      {mode === 'deleting' && (
        <form onSubmit={handleDeleteSubmit}>
          <div className="flex items-center gap-2 flex-wrap">
            <span className="text-sm text-gray-600">{review.nickname}의 리뷰를 삭제할까요?</span>
            <input
              value={deletePin}
              onChange={e => setDeletePin(e.target.value)}
              placeholder="PIN 4자리"
              maxLength={4}
              className={`w-28 ${inputClass}`}
            />
            <button type="submit" disabled={deleteReview.isPending}
              className="text-sm text-white bg-red-500 hover:bg-red-600 disabled:bg-red-300 px-3 py-1.5 rounded-md">
              확인
            </button>
            <button type="button" onClick={handleCancel} className="text-sm text-gray-500 hover:text-gray-700">취소</button>
          </div>
          {error && <p className="text-xs text-red-500 mt-1">{error}</p>}
        </form>
      )}
    </div>
  )
}
