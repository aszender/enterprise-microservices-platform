// app/router.jsx
// React Router configuration

import { createBrowserRouter } from 'react-router-dom'
import Layout from './Layout'
import HomePage from '../pages/HomePage'
import NotFoundPage from '../pages/NotFoundPage'
import ProductsPage from '../features/products/pages/ProductsPage'
import OrdersPage from '../features/orders/pages/OrdersPage'
import InventoryPage from '../features/inventory/pages/InventoryPage'

export const router = createBrowserRouter([
  {
    path: '/',
    element: <Layout />,
    children: [
      { index: true, element: <HomePage /> },
      { path: 'products', element: <ProductsPage /> },
      { path: 'orders', element: <OrdersPage /> },
      { path: 'inventory', element: <InventoryPage /> },
      { path: '*', element: <NotFoundPage /> },
    ],
  },
])
