import { useNavigate } from 'react-router-dom'

export default function MenuCard({ menu }) {
  const navigate = useNavigate()

  return (
    <div
      onClick={() => navigate(`/menus/${menu.id}`)}
      className="border rounded-lg bg-white shadow-sm cursor-pointer hover:shadow-md transition-shadow overflow-hidden"
    >
      {menu.imageUrl ? (
        <img
          src={menu.imageUrl}
          alt={menu.name}
          className="w-full h-40 object-cover"
        />
      ) : (
        <div className="w-full h-40 bg-gray-100 flex items-center justify-center text-gray-400 text-sm">
          이미지 없음
        </div>
      )}

      <div className="p-4">
        <h3 className="font-semibold text-gray-800 text-lg mb-1 truncate">{menu.name}</h3>
        <p className="text-sm text-gray-500 mb-3 truncate">{menu.restaurantName}</p>

        <div className="flex flex-wrap gap-1 mb-3">
          <span className="text-xs bg-orange-100 text-orange-700 px-2 py-0.5 rounded-full">
            {menu.category?.label}
          </span>
          <span className="text-xs bg-blue-100 text-blue-700 px-2 py-0.5 rounded-full">
            {menu.priceRange?.label}
          </span>
          <span className="text-xs bg-green-100 text-green-700 px-2 py-0.5 rounded-full">
            {menu.distance?.label}
          </span>
        </div>

        <div className="flex items-center gap-2 text-sm text-gray-500">
          <span>⭐ {menu.avgRating != null ? menu.avgRating : '—'}</span>
          <span>·</span>
          <span>리뷰 {menu.reviewCount}개</span>
        </div>
      </div>
    </div>
  )
}
