import { useState } from 'react'
import useAiPickerStore from '../../store/useAiPickerStore'
import { useRecommend, useAiHistory, useDeleteHistory } from '../../hooks/useAiPicker'
import RecommendCard from '../../components/aipicker/RecommendCard'

export default function AiPickerPage() {
  const { userInput, setUserInput } = useAiPickerStore()
  const recommend = useRecommend()
  const deleteHistory = useDeleteHistory()
  const { data: history, isLoading: historyLoading } = useAiHistory(20)
  const [expandedId, setExpandedId] = useState(null)

  function handleDeleteHistory() {
    if (!window.confirm('추천 히스토리를 모두 삭제할까요?')) return
    deleteHistory.mutate()
  }

  function handleRecommend() {
    if (!userInput.trim()) return
    recommend.mutate({ userInput })
  }

  function toggleHistory(id) {
    setExpandedId((prev) => (prev === id ? null : id))
  }

  return (
    <div className="max-w-2xl space-y-6">
      <h1 className="text-2xl font-bold text-gray-800">AI 메뉴 추천 🤖</h1>

      {/* 입력 */}
      <div>
        <textarea
          className="w-full border rounded-lg p-3 text-sm resize-none focus:outline-none focus:ring-2 focus:ring-indigo-400"
          rows={3}
          maxLength={500}
          placeholder="원하는 메뉴 조건을 자유롭게 입력하세요. (예: 오늘 느끼한 거 말고 깔끔한 거 먹고 싶어)"
          value={userInput}
          onChange={(e) => setUserInput(e.target.value)}
          disabled={recommend.isPending}
        />
        <p className="text-xs text-gray-400 text-right mt-1">{userInput.length}/500</p>
      </div>

      <button
        type="button"
        onClick={handleRecommend}
        disabled={recommend.isPending || !userInput.trim()}
        className="w-full py-3 bg-indigo-600 hover:bg-indigo-700 disabled:bg-indigo-300 text-white font-bold rounded-lg transition-colors"
      >
        {recommend.isPending ? 'AI가 추천 중...' : 'AI 추천 받기'}
      </button>

      {/* 에러 */}
      {recommend.isError && (
        <p className="text-center text-red-500 text-sm">
          {recommend.error?.response?.data?.message || '오류가 발생했습니다.'}
        </p>
      )}

      {/* TOP 3 결과 */}
      {recommend.data && (
        <div className="space-y-3">
          <h2 className="text-lg font-bold text-gray-700">추천 결과</h2>
          {recommend.data.results.map((result) => (
            <RecommendCard key={result.rank} result={result} />
          ))}
        </div>
      )}

      {/* 히스토리 */}
      <div>
        <div className="flex justify-between items-center mb-3">
          <h2 className="text-xl font-bold text-gray-700">추천 히스토리</h2>
          {history?.length > 0 && (
            <span
              className="text-sm text-gray-400 hover:text-red-500 cursor-pointer transition-colors"
              onClick={handleDeleteHistory}
            >
              히스토리 삭제
            </span>
          )}
        </div>
        {historyLoading ? (
          <p className="text-gray-400 text-sm">로딩 중...</p>
        ) : !history?.length ? (
          <p className="text-gray-400 text-sm">추천 히스토리가 없습니다.</p>
        ) : (
          <div className="space-y-2">
            {history.map((item) => (
              <div key={item.id} className="border rounded-lg bg-white shadow-sm overflow-hidden">
                <button
                  type="button"
                  className="w-full text-left px-4 py-3 flex justify-between items-center hover:bg-gray-50"
                  onClick={() => toggleHistory(item.id)}
                >
                  <span className="text-sm text-gray-700 truncate">{item.userInput}</span>
                  <span className="text-gray-400 text-xs ml-2 shrink-0">
                    {expandedId === item.id ? '▲' : '▼'}
                  </span>
                </button>
                {expandedId === item.id && (
                  <div className="px-4 pb-4 space-y-2">
                    {item.results.map((result) => (
                      <RecommendCard key={result.rank} result={result} />
                    ))}
                  </div>
                )}
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}
