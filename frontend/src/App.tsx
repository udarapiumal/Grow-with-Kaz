import { useState } from 'react'
import heroImg from './assets/hero.png'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import RequireAdmin from './RequireAdmin';
import AdminLogin from './AdminLogin';
import AdminProducts from './AdminProducts';
import './App.css'
import NavBar from './Navbar'
import { Route, Routes, useLocation } from 'react-router-dom'
import Home from './Home'
import Contact from './Contact'
import Footer from './Footer'
import ProductDetail from './ProductDetail';
import Products from './Products';

function App() {
  const [count, setCount] = useState(0)
  const location = useLocation();
  const isAdminRoute = location.pathname.startsWith('/admin');
  return (
    <>
      {!isAdminRoute && <NavBar />}
      <main>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/contact" element={<Contact />} />
          <Route path="/products" element={<Products />} />
          <Route path="/products/:slug" element={<ProductDetail />} />

          <Route path="/admin/login" element={<AdminLogin />} />
          <Route element={<RequireAdmin />}>
            <Route path="/admin/products" element={<AdminProducts />} />
          </Route>

          <Route path="*" element={<Home />} />
        </Routes>
      </main>
      {!isAdminRoute && <Footer />}
    </>
  )
}

export default App
