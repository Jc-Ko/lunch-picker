const RANK_BADGE = {
  1: 'bg-yellow-400 text-yellow-900',
  2: 'bg-gray-300 text-gray-700',
  3: 'bg-orange-300 text-orange-800',
}

export default function RecommendCard({ result }) {
  const badgeColor = RANK_BADGE[result.rank] ?? 'bg-gray-200 text-gray-600'
  return (
    <div className="border rounded-lg p-4 bg-white shadow-sm">
      <div className="flex items-start gap-3 mb-3">
        <span className={`shrink-0 px-2.5 py-1 rounded-full text-xs font-bold ${badgeColor}`}>
          {result.rank}위
        </span>
        <div>
          <h3 className="text-base font-bold text-gray-800">{result.menuName}</h3>
          <p className="text-xs text-gray-500">{result.restaurantName}</p>
        </div>
      </div>
      <div className="flex flex-wrap gap-1 mb-2">
        <span className="text-xs bg-orange-100 text-orange-700 px-2 py-0.5 rounded-full">{result.category.label}</span>
        <span className="text-xs bg-blue-100 text-blue-700 px-2 py-0.5 rounded-full">{result.priceRange.label}</span>
        <span className="text-xs bg-green-100 text-green-700 px-2 py-0.5 rounded-full">{result.distance.label}</span>
        {result.avgRating != null && (
          <span className="text-xs bg-yellow-50 text-yellow-700 px-2 py-0.5 rounded-full">⭐ {result.avgRating}</span>
        )}
      </div>
      <p className="text-sm text-gray-600 leading-relaxed">{result.reason}</p>
    </div>
  )
}
