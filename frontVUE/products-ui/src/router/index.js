// src/router/index.js
// App router (enterprise-ish structure)

import { createRouter, createWebHistory } from 'vue-router'
import ProductsPage from '../pages/ProductsPage.vue'

const routes = [
  {
    path: '/',
    name: 'products',
    component: ProductsPage,
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
