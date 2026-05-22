// TODO: Dev A — 리뷰 아이템 UI 구현
// props: { review, onDelete }
// 표시 항목: 닉네임, 별점, 댓글, 작성일, 삭제 버튼(PIN 입력 모달)
export default function ReviewItem({ review, onDelete }) {
  return (
    <div className="border-b py-3">
      {/* TODO: Dev A */}
      <p className="text-gray-400 text-sm">ReviewItem — {review?.nickname}</p>
    </div>
  )
}
