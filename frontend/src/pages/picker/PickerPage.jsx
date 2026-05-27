import { useState, useEffect } from 'react'
import usePickerStore from '../../store/usePickerStore'
import { usePick } from '../../hooks/usePicker'
import { useCommonCodes } from '../../hooks/useCommonCodes'
import PickerStep from '../../components/picker/PickerStep'
import PickerResultCard from '../../components/picker/PickerResultCard'

const RATINGS = [
  { label: '4.5 이상', value: 4.5 },
  { label: '4.0 이상', value: 4.0 },
  { label: '3.5 이상', value: 3.5 },
  { label: '상관없음', value: null },
]

function OptionButton({ label, selected, onClick }) {
  return (
    <button
      type="button"
      onClick={onClick}
      className={`px-3 py-1.5 rounded-md text-sm border transition-colors ${
        selected
          ? 'bg-orange-500 text-white border-orange-500'
          : 'bg-white text-gray-600 border-gray-300 hover:border-orange-400'
      }`}
    >
      {label}
    </button>
  )
}

export default function PickerPage() {
  const { condition, setCondition } = usePickerStore()
  const [pickState, setPickState] = useState({ params: null, count: 0 })

  const { data: categories = [] } = useCommonCodes('category')
  const { data: priceRanges = [] } = useCommonCodes('price_range')
  const { data: distances = [] } = useCommonCodes('distance')

  useEffect(() => {
    if (categories.length > 0 && Object.keys(condition.weights).length === 0) {
      setCondition('weights', Object.fromEntries(categories.map(c => [c.code, 33])))
    }
  }, [categories])

  const query = usePick(pickState.params, pickState.count)

  function handlePick() {
    setPickState(prev => ({ params: { ...condition }, count: prev.count + 1 }))
  }

  return (
    <div className="max-w-xl space-y-4">
      <h1 className="text-2xl font-bold text-gray-800">오늘의 메뉴 뽑기 🎲</h1>

      {/* Step 1: 카테고리 */}
      <PickerStep title="Step 1. 카테고리">
        <div className="flex gap-2 mb-3">
          <OptionButton
            label="단순 선택"
            selected={condition.categoryMode === 'simple'}
            onClick={() => setCondition('categoryMode', 'simple')}
          />
          <OptionButton
            label="비중 선택"
            selected={condition.categoryMode === 'weighted'}
            onClick={() => setCondition('categoryMode', 'weighted')}
          />
        </div>

        {condition.categoryMode === 'simple' ? (
          <div className="flex gap-2 flex-wrap">
            {categories.map(c => (
              <OptionButton
                key={c.code}
                label={c.label}
                selected={condition.category === c.code}
                onClick={() => setCondition('category', c.code)}
              />
            ))}
            <OptionButton
              label="전체"
              selected={!condition.category}
              onClick={() => setCondition('category', null)}
            />
          </div>
        ) : (
          <div className="flex items-center gap-4 flex-wrap">
            {categories.map(c => (
              <label key={c.code} className="flex items-center gap-2 text-sm text-gray-700">
                <span>{c.label}</span>
                <input
                  type="number"
                  min={0}
                  value={condition.weights[c.code] ?? 0}
                  onChange={e =>
                    setCondition('weights', { ...condition.weights, [c.code]: Number(e.target.value) })
                  }
                  className="w-16 border border-gray-300 rounded-md px-2 py-1 text-sm text-center focus:outline-none focus:ring-2 focus:ring-orange-400"
                />
              </label>
            ))}
          </div>
        )}
      </PickerStep>

      {/* Step 2: 별점 */}
      <PickerStep title="Step 2. 평균 별점">
        <div className="flex gap-2 flex-wrap">
          {RATINGS.map(({ label, value }) => (
            <OptionButton
              key={label}
              label={label}
              selected={condition.minRating === value}
              onClick={() => setCondition('minRating', value)}
            />
          ))}
        </div>
      </PickerStep>

      {/* Step 3: 가격대 */}
      <PickerStep title="Step 3. 가격대">
        <div className="flex gap-2 flex-wrap">
          {priceRanges.map(p => (
            <OptionButton
              key={p.code}
              label={p.label}
              selected={condition.priceRange === p.code}
              onClick={() => setCondition('priceRange', p.code)}
            />
          ))}
          <OptionButton
            label="상관없음"
            selected={!condition.priceRange}
            onClick={() => setCondition('priceRange', null)}
          />
        </div>
      </PickerStep>

      {/* Step 4: 거리 */}
      <PickerStep title="Step 4. 거리">
        <div className="flex gap-2 flex-wrap">
          {distances.map(d => (
            <OptionButton
              key={d.code}
              label={d.label}
              selected={condition.distance === d.code}
              onClick={() => setCondition('distance', d.code)}
            />
          ))}
          <OptionButton
            label="상관없음"
            selected={!condition.distance}
            onClick={() => setCondition('distance', null)}
          />
        </div>
      </PickerStep>

      {/* 뽑기 버튼 */}
      <button
        type="button"
        onClick={handlePick}
        disabled={query.isFetching}
        className="w-full py-3 bg-orange-500 hover:bg-orange-600 disabled:bg-orange-300 text-white font-bold rounded-lg transition-colors"
      >
        {query.isFetching ? '뽑는 중...' : '뽑기!'}
      </button>

      {/* 결과 */}
      {pickState.count > 0 && !query.isFetching && (
        <div>
          {query.isError ? (
            <p className="text-center text-gray-500 py-8">
              {query.error?.response?.data?.message || '오류가 발생했습니다.'}
            </p>
          ) : query.data ? (
            <PickerResultCard menu={query.data} onRepick={handlePick} />
          ) : null}
        </div>
      )}
    </div>
  )
}
