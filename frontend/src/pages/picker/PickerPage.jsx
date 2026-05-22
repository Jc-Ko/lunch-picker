import usePickerStore from '../../store/usePickerStore'
import PickerStep from '../../components/picker/PickerStep'

// TODO: Dev B — 오늘의 메뉴 뽑기 페이지 구현
// - Step 1: 카테고리 선택 방식 (simple / weighted)
// - Step 2: 카테고리 또는 비중 입력
// - Step 3: 별점 필터 선택
// - Step 4: 가격대 / 거리 필터 선택
// - 뽑기 버튼 → usePick 호출
// - 결과 카드 표시 (애니메이션 추가 가능)
export default function PickerPage() {
  const { condition, setCondition, resetCondition } = usePickerStore()

  return (
    <div>
      <h1 className="text-2xl font-bold text-gray-800 mb-6">오늘의 메뉴 뽑기 🎲</h1>

      {/* TODO: Dev B — 조건 입력 스텝 */}
      <div className="space-y-4 mb-6 text-sm text-gray-400">
        스텝 영역 (TODO: Dev B)
      </div>

      {/* TODO: Dev B — 뽑기 버튼 */}
      <button
        className="w-full py-3 bg-orange-500 text-white font-bold rounded-lg hover:bg-orange-600 transition-colors"
        onClick={() => {/* TODO: Dev B */}}
      >
        뽑기!
      </button>

      {/* TODO: Dev B — 결과 카드 */}
      <div className="mt-6 text-sm text-gray-400">결과 영역 (TODO: Dev B)</div>
    </div>
  )
}
