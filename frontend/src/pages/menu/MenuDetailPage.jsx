import { useParams, useNavigate, Link } from 'react-router-dom'
import { useMenu, useReviews, useDeleteMenu, useEatMenu } from '../../hooks/useMenus'
import ReviewItem from '../../components/menu/ReviewItem'
import ReviewForm from '../../components/menu/ReviewForm'

export default function MenuDetailPage() {
  const { id } = useParams()
  const navigate = useNavigate()

  const { data: menu, isLoading, isError } = useMenu(id)
  const { data: reviews, isLoading: reviewsLoading } = useReviews(id)
  const deleteMenu = useDeleteMenu()
  const eatMenu = useEatMenu()

  function handleDelete() {
    if (!window.confirm(`"${menu.name}"을(를) 삭제할까요?`)) return
    deleteMenu.mutate(id, { onSuccess: () => navigate('/') })
  }

  if (isLoading) return <p className="text-gray-400 text-center py-16">로딩 중...</p>
  if (isError) return <p className="text-red-400 text-center py-16">메뉴를 불러올 수 없습니다.</p>

  return (
    <div className="max-w-2xl">
      {/* 헤더 액션 */}
      <div className="flex items-center justify-between mb-6">
        <button onClick={() => navigate('/')} className="text-sm text-gray-500 hover:text-gray-700">
          ← 목록
        </button>
        <div className="flex gap-2">
          <button
            onClick={() => eatMenu.mutate(id)}
            disabled={eatMenu.isPending}
            className="bg-green-500 hover:bg-green-600 disabled:bg-green-300 text-white text-sm font-medium px-3 py-1.5 rounded-md transition-colors"
          >
            오늘 먹었어요
          </button>
          <Link
            to={`/menus/${id}/edit`}
            className="border border-gray-300 text-gray-600 hover:bg-gray-50 text-sm font-medium px-3 py-1.5 rounded-md transition-colors"
          >
            수정
          </Link>
          <button
            onClick={handleDelete}
            disabled={deleteMenu.isPending}
            className="border border-red-300 text-red-500 hover:bg-red-50 disabled:opacity-50 text-sm font-medium px-3 py-1.5 rounded-md transition-colors"
          >
            삭제
          </button>
        </div>
      </div>

      {/* 이미지 */}
      {menu.imageUrl ? (
        <img src={menu.imageUrl} alt={menu.name} className="w-full h-56 object-cover rounded-lg mb-6" />
      ) : (
        <div className="w-full h-56 bg-gray-100 flex items-center justify-center text-gray-400 text-sm rounded-lg mb-6">
          이미지 없음
        </div>
      )}

      {/* 메뉴 정보 */}
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-gray-800 mb-1">{menu.name}</h1>
        <p className="text-gray-500 mb-3">{menu.restaurantName}</p>
        <div className="flex flex-wrap gap-1 mb-3">
          <span className="text-xs bg-orange-100 text-orange-700 px-2 py-0.5 rounded-full">{menu.category?.label}</span>
          <span className="text-xs bg-blue-100 text-blue-700 px-2 py-0.5 rounded-full">{menu.priceRange?.label}</span>
          <span className="text-xs bg-green-100 text-green-700 px-2 py-0.5 rounded-full">{menu.distance?.label}</span>
        </div>
        <div className="flex items-center gap-2 text-sm text-gray-500">
          <span>⭐ {menu.avgRating ?? '—'}</span>
          <span>·</span>
          <span>리뷰 {menu.reviewCount}개</span>
          {menu.lastEatenAt && (
            <>
              <span>·</span>
              <span>마지막으로 먹은 날: {new Date(menu.lastEatenAt).toLocaleDateString('ko-KR')}</span>
            </>
          )}
        </div>
      </div>

      {/* 리뷰 영역 */}
      <h2 className="text-xl font-bold text-gray-700 mb-4">리뷰</h2>

      <ReviewForm menuId={id} />

      <div className="mt-4">
        {reviewsLoading && <p className="text-gray-400 text-sm py-4">리뷰 로딩 중...</p>}
        {!reviewsLoading && reviews?.length === 0 && (
          <p className="text-gray-400 text-sm py-4">아직 리뷰가 없습니다.</p>
        )}
        {reviews?.map(review => (
          <ReviewItem key={review.id} review={review} menuId={id} />
        ))}
      </div>
    </div>
  )
}
