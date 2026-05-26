import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useMenu, useCreateMenu, useUpdateMenu } from '../../hooks/useMenus'

const CATEGORIES = ['한식', '양식', '중식']
const PRICE_RANGES = ['1만원이하', '1~2만원', '2만원이상']
const DISTANCES = ['도보5분', '도보10분', '배달가능']

const EMPTY_FORM = {
  name: '',
  restaurantName: '',
  category: '',
  priceRange: '',
  distance: '',
  imageUrl: '',
}

const inputClass =
  'w-full border border-gray-300 rounded-md px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-orange-400'

export default function MenuFormPage() {
  const { id } = useParams()
  const navigate = useNavigate()
  const isEdit = !!id

  const { data: existing, isLoading } = useMenu(id)
  const createMenu = useCreateMenu()
  const updateMenu = useUpdateMenu()

  const [form, setForm] = useState(EMPTY_FORM)
  const [validationError, setValidationError] = useState('')

  useEffect(() => {
    if (existing) {
      setForm({
        name: existing.name ?? '',
        restaurantName: existing.restaurantName ?? '',
        category: existing.category ?? '',
        priceRange: existing.priceRange ?? '',
        distance: existing.distance ?? '',
        imageUrl: existing.imageUrl ?? '',
      })
    }
  }, [existing])

  function handleChange(e) {
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }))
  }

  function handleSubmit(e) {
    e.preventDefault()
    if (!form.name || !form.restaurantName || !form.category || !form.priceRange || !form.distance) {
      setValidationError('필수 항목을 모두 입력해 주세요.')
      return
    }
    setValidationError('')

    const body = {
      name: form.name,
      restaurantName: form.restaurantName,
      category: form.category,
      priceRange: form.priceRange,
      distance: form.distance,
      imageUrl: form.imageUrl || null,
    }

    if (isEdit) {
      updateMenu.mutate({ id, body }, { onSuccess: () => navigate(`/menus/${id}`) })
    } else {
      createMenu.mutate(body, { onSuccess: () => navigate('/') })
    }
  }

  if (isEdit && isLoading) {
    return <p className="text-gray-400 text-center py-16">로딩 중...</p>
  }

  const isPending = createMenu.isPending || updateMenu.isPending
  const apiError =
    createMenu.error?.response?.data?.message ||
    updateMenu.error?.response?.data?.message ||
    (createMenu.isError || updateMenu.isError ? '오류가 발생했습니다.' : '')

  return (
    <div className="max-w-lg">
      <h1 className="text-2xl font-bold text-gray-800 mb-6">
        {isEdit ? '메뉴 수정' : '메뉴 등록'}
      </h1>

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">메뉴 이름 *</label>
          <input
            name="name"
            value={form.name}
            onChange={handleChange}
            className={inputClass}
            placeholder="예: 김치찌개"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">가게 이름 *</label>
          <input
            name="restaurantName"
            value={form.restaurantName}
            onChange={handleChange}
            className={inputClass}
            placeholder="예: 한솥뚝배기"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">카테고리 *</label>
          <select name="category" value={form.category} onChange={handleChange} className={inputClass + ' bg-white'}>
            <option value="">선택</option>
            {CATEGORIES.map(c => <option key={c} value={c}>{c}</option>)}
          </select>
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">가격대 *</label>
          <select name="priceRange" value={form.priceRange} onChange={handleChange} className={inputClass + ' bg-white'}>
            <option value="">선택</option>
            {PRICE_RANGES.map(p => <option key={p} value={p}>{p}</option>)}
          </select>
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">거리 *</label>
          <select name="distance" value={form.distance} onChange={handleChange} className={inputClass + ' bg-white'}>
            <option value="">선택</option>
            {DISTANCES.map(d => <option key={d} value={d}>{d}</option>)}
          </select>
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">이미지 URL</label>
          <input
            name="imageUrl"
            value={form.imageUrl}
            onChange={handleChange}
            className={inputClass}
            placeholder="https://example.com/image.jpg"
          />
        </div>

        {validationError && <p className="text-red-500 text-sm">{validationError}</p>}
        {apiError && <p className="text-red-500 text-sm">{apiError}</p>}

        <div className="flex gap-3 pt-2">
          <button
            type="submit"
            disabled={isPending}
            className="bg-orange-500 hover:bg-orange-600 disabled:bg-orange-300 text-white text-sm font-medium px-6 py-2 rounded-md transition-colors"
          >
            {isPending ? '저장 중...' : isEdit ? '수정' : '등록'}
          </button>
          <button
            type="button"
            onClick={() => navigate(-1)}
            className="border border-gray-300 text-gray-600 hover:bg-gray-50 text-sm font-medium px-6 py-2 rounded-md transition-colors"
          >
            취소
          </button>
        </div>
      </form>
    </div>
  )
}
