// src/api/inventoryApi.js
// API client for Inventory service (port 8082)
// Backend routes: InventoryController @ /api/inventory

const DEFAULT_BASE_URL = '/api/inventory'

function getInventoryBaseUrl() {
  return import.meta.env.VITE_INVENTORY_API_BASE_URL || DEFAULT_BASE_URL
}

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
    credentials: options?.credentials ?? 'include',
    headers: {
      'Content-Type': 'application/json',
      ...(options?.headers ?? {}),
    },
    ...options,
  })

  if (!res.ok) {
    await toHttpError(res)
  }

  if (res.status === 204) return null

  return res.json()
}

export async function listStock() {
  return requestJson(`${getInventoryBaseUrl()}/stock`, { method: 'GET' })
}

export async function getStock(productId) {
  return requestJson(`${getInventoryBaseUrl()}/stock/${productId}`, { method: 'GET' })
}
