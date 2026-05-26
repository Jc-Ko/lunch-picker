export default function StarRating({ value, onChange, readonly = false }) {
  return (
    <div className="flex gap-0.5">
      {[1, 2, 3, 4, 5].map(n => (
        <button
          key={n}
          type="button"
          onClick={() => !readonly && onChange?.(n)}
          className={`text-lg leading-none ${n <= value ? 'text-yellow-400' : 'text-gray-300'} ${readonly ? 'cursor-default' : 'hover:text-yellow-300'}`}
        >
          ★
        </button>
      ))}
    </div>
  )
}
