// TODO: Dev C — AI 추천 결과 카드 UI 구현
// props: { result }
// 표시 항목: 순위(rank), 메뉴명, 식당명, 카테고리, 가격대, 거리, 평균 별점, 추천 이유(reason)
export default function RecommendCard({ result }) {
  return (
    <div className="border rounded-lg p-4 bg-white shadow-sm">
      {/* TODO: Dev C */}
      <p className="text-gray-400 text-sm">RecommendCard — #{result?.rank} {result?.menuName}</p>
    </div>
  )
}
