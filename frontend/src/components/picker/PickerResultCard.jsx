export default function PickerResultCard({ menu, onRepick }) {
  return (
    <div className="border rounded-lg bg-white shadow-sm overflow-hidden">
      {menu.imageUrl ? (
        <img src={menu.imageUrl} alt={menu.name} className="w-full h-48 object-cover" />
      ) : (
        <div className="w-full h-48 bg-gray-100 flex items-center justify-center text-gray-400 text-sm">
          이미지 없음
        </div>
      )}
      <div className="p-5">
        <h2 className="text-xl font-bold text-gray-800 mb-1">{menu.name}</h2>
        <p className="text-sm text-gray-500 mb-3">{menu.restaurantName}</p>
        <div className="flex flex-wrap gap-1 mb-3">
          <span className="text-xs bg-orange-100 text-orange-700 px-2 py-0.5 rounded-full">{menu.category.label}</span>
          <span className="text-xs bg-blue-100 text-blue-700 px-2 py-0.5 rounded-full">{menu.priceRange.label}</span>
          <span className="text-xs bg-green-100 text-green-700 px-2 py-0.5 rounded-full">{menu.distance.label}</span>
        </div>
        <p className="text-sm text-gray-500 mb-4">⭐ {menu.avgRating ?? '—'} · 리뷰 {menu.reviewCount}개</p>
        <button
          type="button"
          onClick={onRepick}
          className="w-full py-2 border border-orange-500 text-orange-500 hover:bg-orange-50 text-sm font-medium rounded-md transition-colors"
        >
          다시 뽑기
        </button>
      </div>
    </div>
  )
}
