import useAiPickerStore from '../../store/useAiPickerStore'
import { useRecommend, useAiHistory } from '../../hooks/useAiPicker'
import RecommendCard from '../../components/aipicker/RecommendCard'

// TODO: Dev C — AI 메뉴 추천 페이지 구현
// - 자연어 입력창 (textarea, max 500자)
// - AI 추천 받기 버튼 → useRecommend 호출
// - TOP 3 추천 결과 카드 목록
// - 히스토리 섹션: 과거 추천 내역 (접기/펼치기)
export default function AiPickerPage() {
  const { userInput, setUserInput, resetInput } = useAiPickerStore()
  const recommend = useRecommend()
  const { data: historyData, isLoading: historyLoading } = useAiHistory(20)

  return (
    <div>
      <h1 className="text-2xl font-bold text-gray-800 mb-6">AI 메뉴 추천 🤖</h1>

      {/* TODO: Dev C — 자연어 입력창 */}
      <div className="mb-4">
        <textarea
          className="w-full border rounded-lg p-3 text-sm resize-none focus:outline-none focus:ring-2 focus:ring-orange-400"
          rows={3}
          maxLength={500}
          placeholder="원하는 메뉴 조건을 자유롭게 입력하세요. (예: 오늘 느끼한 거 말고 깔끔한 거 먹고 싶어)"
          value={userInput}
          onChange={(e) => setUserInput(e.target.value)}
        />
      </div>

      {/* TODO: Dev C — 추천 버튼 */}
      <button
        className="w-full py-3 bg-indigo-600 text-white font-bold rounded-lg hover:bg-indigo-700 transition-colors mb-8"
        onClick={() => {/* TODO: Dev C */}}
      >
        AI 추천 받기
      </button>

      {/* TODO: Dev C — TOP 3 결과 */}
      <div className="mb-8 text-sm text-gray-400">TOP 3 추천 결과 영역 (TODO: Dev C)</div>

      {/* TODO: Dev C — 히스토리 */}
      <h2 className="text-xl font-bold text-gray-700 mb-4">추천 히스토리</h2>
      {historyLoading && <p className="text-gray-400">히스토리 로딩 중...</p>}
      <div className="text-sm text-gray-400">히스토리 목록 (TODO: Dev C)</div>
    </div>
  )
}
