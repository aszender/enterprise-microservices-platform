// src/router/index.js
// App router (enterprise-ish structure)

import { createRouter, createWebHistory } from 'vue-router'
import ProductsPage from '../pages/ProductsPage.vue'
import OrdersPage from '../pages/OrdersPage.vue'
import InventoryPage from '../pages/InventoryPage.vue'

const routes = [
  {
    path: '/',
    name: 'products',
    component: ProductsPage,
  },
  {
    path: '/orders',
    name: 'orders',
    component: OrdersPage,
  },
  {
    path: '/inventory',
    name: 'inventory',
    component: InventoryPage,
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
