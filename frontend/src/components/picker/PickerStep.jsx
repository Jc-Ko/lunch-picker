// TODO: Dev B — 뽑기 조건 입력 스텝 UI 구현
// props: { step, value, onChange }
// step 종류: categoryMode, category, rating, priceRange, distance
export default function PickerStep({ step, value, onChange }) {
  return (
    <div className="border rounded-lg p-4 bg-white shadow-sm">
      {/* TODO: Dev B */}
      <p className="text-gray-400 text-sm">PickerStep — {step}</p>
    </div>
  )
}
