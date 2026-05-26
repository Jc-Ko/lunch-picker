import useMenuStore from '../../store/useMenuStore'

const CATEGORIES = ['한식', '양식', '중식']
const PRICE_RANGES = ['1만원이하', '1~2만원', '2만원이상']
const DISTANCES = ['도보5분', '도보10분', '배달가능']

const selectClass =
  'border border-gray-300 rounded-md px-3 py-2 text-sm bg-white focus:outline-none focus:ring-2 focus:ring-orange-400'

export default function MenuFilter() {
  const { filter, setFilter, resetFilter } = useMenuStore()

  return (
    <div className="flex gap-3 mb-6 items-center flex-wrap">
      <select
        value={filter.category}
        onChange={e => setFilter('category', e.target.value)}
        className={selectClass}
      >
        <option value="">카테고리 전체</option>
        {CATEGORIES.map(c => <option key={c} value={c}>{c}</option>)}
      </select>

      <select
        value={filter.priceRange}
        onChange={e => setFilter('priceRange', e.target.value)}
        className={selectClass}
      >
        <option value="">가격대 전체</option>
        {PRICE_RANGES.map(p => <option key={p} value={p}>{p}</option>)}
      </select>

      <select
        value={filter.distance}
        onChange={e => setFilter('distance', e.target.value)}
        className={selectClass}
      >
        <option value="">거리 전체</option>
        {DISTANCES.map(d => <option key={d} value={d}>{d}</option>)}
      </select>

      <button
        onClick={resetFilter}
        className="text-sm text-gray-500 hover:text-gray-700 underline"
      >
        초기화
      </button>
    </div>
  )
}
