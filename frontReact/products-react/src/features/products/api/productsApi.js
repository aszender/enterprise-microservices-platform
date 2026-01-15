// features/products/api/productsApi.js
// Mirrors the Vue app's API client shape, but in React.
// Backend: /api/products (see products-service ProductController)

import { requestJson } from '../../../shared/api/http'

const DEFAULT_BASE_URL = '/api/products'

export function getProductsBaseUrl() {
  // Allow override via Vite env.
  // Example: VITE_PRODUCTS_API_BASE_URL=http://localhost:8080/api/products
  return import.meta.env.VITE_PRODUCTS_API_BASE_URL || DEFAULT_BASE_URL
}

export function listProducts() {
  return requestJson(getProductsBaseUrl(), { method: 'GET' })
}

export function getProduct(id) {
  return requestJson(`${getProductsBaseUrl()}/${id}`, { method: 'GET' })
}

export function createProduct(product) {
  return requestJson(getProductsBaseUrl(), {
    method: 'POST',
    body: JSON.stringify(product),
  })
}

export function updateProduct(id, product) {
  return requestJson(`${getProductsBaseUrl()}/${id}`, {
    method: 'PUT',
    body: JSON.stringify(product),
  })
}

export function deleteProduct(id) {
  return requestJson(`${getProductsBaseUrl()}/${id}`, { method: 'DELETE' })
}

export function searchProducts(keyword) {
  const url = new URL(`${getProductsBaseUrl()}/search`)
  url.searchParams.set('keyword', keyword)
  return requestJson(url.toString(), { method: 'GET' })
}
