import { useState } from 'react'
import usePickerStore from '../../store/usePickerStore'
import { usePick } from '../../hooks/usePicker'
import PickerStep from '../../components/picker/PickerStep'
import PickerResultCard from '../../components/picker/PickerResultCard'

const RATINGS = [
  { label: '4.5 이상', value: 4.5 },
  { label: '4.0 이상', value: 4.0 },
  { label: '3.5 이상', value: 3.5 },
  { label: '상관없음', value: null },
]
const PRICE_RANGES = ['1만원이하', '1~2만원', '2만원이상', '상관없음']
const DISTANCES = ['도보5분', '도보10분', '배달가능', '상관없음']

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

  const query = usePick(pickState.params, pickState.count)

  const isCategoryAll =
    !condition.category ||
    condition.category === '전체' ||
    condition.category === '상관없음'

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
            {['한식', '양식', '중식', '전체'].map(cat => (
              <OptionButton
                key={cat}
                label={cat}
                selected={cat === '전체' ? isCategoryAll : condition.category === cat}
                onClick={() => setCondition('category', cat)}
              />
            ))}
          </div>
        ) : (
          <div className="flex items-center gap-4 flex-wrap">
            {[
              { key: 'koreanWeight', label: '한식' },
              { key: 'westernWeight', label: '양식' },
              { key: 'chineseWeight', label: '중식' },
            ].map(({ key, label }) => (
              <label key={key} className="flex items-center gap-2 text-sm text-gray-700">
                <span>{label}</span>
                <input
                  type="number"
                  min={0}
                  value={condition[key]}
                  onChange={e => setCondition(key, Number(e.target.value))}
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
          {PRICE_RANGES.map(p => (
            <OptionButton
              key={p}
              label={p}
              selected={condition.priceRange === p}
              onClick={() => setCondition('priceRange', p)}
            />
          ))}
        </div>
      </PickerStep>

      {/* Step 4: 거리 */}
      <PickerStep title="Step 4. 거리">
        <div className="flex gap-2 flex-wrap">
          {DISTANCES.map(d => (
            <OptionButton
              key={d}
              label={d}
              selected={condition.distance === d}
              onClick={() => setCondition('distance', d)}
            />
          ))}
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
