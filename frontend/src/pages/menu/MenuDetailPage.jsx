import { useParams } from 'react-router-dom'
import { useMenu, useReviews } from '../../hooks/useMenus'
import ReviewItem from '../../components/menu/ReviewItem'

// TODO: Dev A — 메뉴 상세 페이지 구현
// - 메뉴 정보 표시 (이미지, 이름, 식당명, 카테고리, 가격대, 거리, 마지막 먹은 날)
// - 오늘 먹었어요 버튼
// - 수정 / 삭제 버튼
// - 리뷰 목록
// - 리뷰 작성 폼 (닉네임, PIN, 별점, 댓글)
export default function MenuDetailPage() {
  const { id } = useParams()
  const { data: menuData, isLoading: menuLoading } = useMenu(id)
  const { data: reviewData, isLoading: reviewLoading } = useReviews(id)

  return (
    <div>
      {/* TODO: Dev A — 메뉴 정보 */}
      {menuLoading && <p className="text-gray-400">메뉴 로딩 중...</p>}
      <div className="mb-8 text-sm text-gray-400">메뉴 정보 영역 (TODO: Dev A)</div>

      <h2 className="text-xl font-bold text-gray-700 mb-4">리뷰</h2>

      {/* TODO: Dev A — 리뷰 목록 */}
      {reviewLoading && <p className="text-gray-400">리뷰 로딩 중...</p>}
      <div className="mb-6 text-sm text-gray-400">리뷰 목록 영역 (TODO: Dev A)</div>

      {/* TODO: Dev A — 리뷰 작성 폼 */}
      <div className="text-sm text-gray-400">리뷰 작성 폼 (TODO: Dev A)</div>
    </div>
  )
}
