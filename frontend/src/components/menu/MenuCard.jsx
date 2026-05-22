// TODO: Dev A — 메뉴 카드 UI 구현
// props: { menu }
// 표시 항목: 이미지, 이름, 식당명, 카테고리, 가격대, 거리, 평균 별점, 리뷰 수
export default function MenuCard({ menu }) {
  return (
    <div className="border rounded-lg p-4 bg-white shadow-sm">
      {/* TODO: Dev A */}
      <p className="text-gray-400 text-sm">MenuCard — {menu?.name}</p>
    </div>
  )
}
