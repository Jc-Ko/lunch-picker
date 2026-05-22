import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Layout from './components/common/Layout'
import MenuListPage from './pages/menu/MenuListPage'
import MenuDetailPage from './pages/menu/MenuDetailPage'
import PickerPage from './pages/picker/PickerPage'
import AiPickerPage from './pages/aipicker/AiPickerPage'

export default function App() {
  return (
    <BrowserRouter>
      <Layout>
        <Routes>
          <Route path="/" element={<MenuListPage />} />
          <Route path="/menus/:id" element={<MenuDetailPage />} />
          <Route path="/picker" element={<PickerPage />} />
          <Route path="/ai" element={<AiPickerPage />} />
        </Routes>
      </Layout>
    </BrowserRouter>
  )
}
