import { useNavigate } from 'react-router-dom'
import { useMenus } from '../../hooks/useMenus'
import useMenuStore from '../../store/useMenuStore'
import MenuCard from '../../components/menu/MenuCard'
import MenuFilter from '../../components/menu/MenuFilter'

export default function MenuListPage() {
  const navigate = useNavigate()
  const { filter } = useMenuStore()
  const { data: menus, isLoading, isError } = useMenus(filter)

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold text-gray-800">메뉴 목록</h1>
        <button
          onClick={() => navigate('/menus/new')}
          className="bg-orange-500 hover:bg-orange-600 text-white text-sm font-medium px-4 py-2 rounded-md transition-colors"
        >
          + 메뉴 등록
        </button>
      </div>

      <MenuFilter />

      {isLoading && (
        <p className="text-gray-400 text-center py-16">로딩 중...</p>
      )}
      {isError && (
        <p className="text-red-400 text-center py-16">데이터를 불러올 수 없습니다.</p>
      )}
      {!isLoading && !isError && menus?.length === 0 && (
        <p className="text-gray-400 text-center py-16">조건에 맞는 메뉴가 없습니다.</p>
      )}

      <div className="grid grid-cols-3 gap-4">
        {menus?.map(menu => (
          <MenuCard key={menu.id} menu={menu} />
        ))}
      </div>
    </div>
  )
}
