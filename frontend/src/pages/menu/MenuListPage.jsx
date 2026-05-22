import { useMenus } from '../../hooks/useMenus'
import useMenuStore from '../../store/useMenuStore'
import MenuCard from '../../components/menu/MenuCard'

// TODO: Dev A — 메뉴 목록 페이지 구현
// - 필터 UI: category / priceRange / distance 셀렉트
// - 메뉴 카드 그리드 (3열)
// - 메뉴 등록 버튼 → 등록 모달 or 별도 폼
export default function MenuListPage() {
  const { filter } = useMenuStore()
  const { data, isLoading, isError } = useMenus(filter)

  return (
    <div>
      <h1 className="text-2xl font-bold text-gray-800 mb-6">메뉴 목록</h1>

      {/* TODO: Dev A — 필터 UI */}
      <div className="mb-4 text-sm text-gray-400">필터 영역 (TODO: Dev A)</div>

      {/* TODO: Dev A — 메뉴 카드 그리드 */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {isLoading && <p className="text-gray-400">로딩 중...</p>}
        {isError && <p className="text-red-400">데이터를 불러올 수 없습니다.</p>}
        {/* TODO: Dev A — data를 MenuCard 목록으로 렌더링 */}
      </div>
    </div>
  )
}
