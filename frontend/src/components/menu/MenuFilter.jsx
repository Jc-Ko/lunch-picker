import { useCommonCodes } from '../../hooks/useCommonCodes'
import useMenuStore from '../../store/useMenuStore'

const selectClass =
  'border border-gray-300 rounded-md px-3 py-2 text-sm bg-white focus:outline-none focus:ring-2 focus:ring-orange-400'

export default function MenuFilter() {
  const { filter, setFilter, resetFilter } = useMenuStore()
  const { data: categories = [] } = useCommonCodes('category')
  const { data: priceRanges = [] } = useCommonCodes('price_range')
  const { data: distances = [] } = useCommonCodes('distance')

  return (
    <div className="flex gap-3 mb-6 items-center flex-wrap">
      <select
        value={filter.category}
        onChange={e => setFilter('category', e.target.value)}
        className={selectClass}
      >
        <option value="">카테고리 전체</option>
        {categories.map(c => <option key={c.code} value={c.code}>{c.label}</option>)}
      </select>

      <select
        value={filter.priceRange}
        onChange={e => setFilter('priceRange', e.target.value)}
        className={selectClass}
      >
        <option value="">가격대 전체</option>
        {priceRanges.map(p => <option key={p.code} value={p.code}>{p.label}</option>)}
      </select>

      <select
        value={filter.distance}
        onChange={e => setFilter('distance', e.target.value)}
        className={selectClass}
      >
        <option value="">거리 전체</option>
        {distances.map(d => <option key={d.code} value={d.code}>{d.label}</option>)}
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
