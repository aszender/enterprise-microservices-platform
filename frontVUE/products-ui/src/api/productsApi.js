// src/api/productsApi.js
// Tiny API client for your Spring Boot Products endpoints.
// Backend routes (see ProductController):
// - GET    /api/products
// - GET    /api/products/{id}
// - POST   /api/products
// - PUT    /api/products/{id}
// - DELETE /api/products/{id}
// - GET    /api/products/search?keyword=...

// Default: relative URL so Vite dev server can proxy `/api` to Spring Boot.
// (See vite.config.js for the proxy.)
const DEFAULT_BASE_URL = '/api/products'

/**
 * Helper to build a nice error message when fetch() fails.
 */
async function toHttpError(response) {
  let bodyText = ''
  try {
    bodyText = await response.text()
  } catch {
    // ignore
  }

  const message = bodyText
    ? `HTTP ${response.status} ${response.statusText}: ${bodyText}`
    : `HTTP ${response.status} ${response.statusText}`

  const err = new Error(message)
  err.status = response.status
  throw err
}

async function requestJson(url, options) {
  const res = await fetch(url, {
    headers: {
      'Content-Type': 'application/json',
      ...(options?.headers ?? {}),
    },
    ...options,
  })

  if (!res.ok) {
    await toHttpError(res)
  }

  // DELETE returns 204 No Content.
  if (res.status === 204) return null

  return res.json()
}

export function getProductsBaseUrl() {
  // Allow override via Vite env: VITE_PRODUCTS_API_BASE_URL
  // Example: VITE_PRODUCTS_API_BASE_URL=http://localhost:8080/api/products
  return import.meta.env.VITE_PRODUCTS_API_BASE_URL || DEFAULT_BASE_URL
}

export async function listProducts() {
  return requestJson(getProductsBaseUrl(), { method: 'GET' })
}

export async function getProduct(id) {
  return requestJson(`${getProductsBaseUrl()}/${id}`, { method: 'GET' })
}

export async function createProduct(product) {
  return requestJson(getProductsBaseUrl(), {
    method: 'POST',
    body: JSON.stringify(product),
  })
}

export async function updateProduct(id, product) {
  return requestJson(`${getProductsBaseUrl()}/${id}`, {
    method: 'PUT',
    body: JSON.stringify(product),
  })
}

export async function deleteProduct(id) {
  return requestJson(`${getProductsBaseUrl()}/${id}`, { method: 'DELETE' })
}

export async function searchProducts(keyword) {
  const url = new URL(`${getProductsBaseUrl()}/search`)
  url.searchParams.set('keyword', keyword)
  return requestJson(url.toString(), { method: 'GET' })
}
