import { Link, useLocation } from 'react-router-dom'

const navLinks = [
  { to: '/', label: '메뉴 목록' },
  { to: '/picker', label: '오늘의 메뉴 뽑기' },
  { to: '/ai', label: 'AI 추천' },
]

export default function Layout({ children }) {
  const { pathname } = useLocation()

  const isActive = (to) => {
    if (to === '/') return pathname === '/'
    return pathname.startsWith(to)
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white border-b border-gray-200 shadow-sm">
        <div className="max-w-5xl mx-auto px-4 py-3 flex items-center justify-between">
          <Link to="/" className="text-xl font-bold text-orange-500">
            오늘 뭐 먹지? 🍱
          </Link>
          <nav className="flex gap-2">
            {navLinks.map(({ to, label }) => (
              <Link
                key={to}
                to={to}
                className={`px-4 py-2 rounded-md text-sm font-medium transition-colors ${
                  isActive(to)
                    ? 'bg-orange-500 text-white'
                    : 'text-gray-600 hover:bg-gray-100'
                }`}
              >
                {label}
              </Link>
            ))}
          </nav>
        </div>
      </header>

      <main className="max-w-5xl mx-auto px-4 py-8">
        {children}
      </main>
    </div>
  )
}
