// src/api/ordersApi.js
// API client for Orders service (port 8081)
// Backend routes: OrderController @ /api/orders

const DEFAULT_BASE_URL = '/api/orders'

function getOrdersBaseUrl() {
  return import.meta.env.VITE_ORDERS_API_BASE_URL || DEFAULT_BASE_URL
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

export async function listOrders() {
  return requestJson(getOrdersBaseUrl(), { method: 'GET' })
}

export async function getOrder(id) {
  return requestJson(`${getOrdersBaseUrl()}/${id}`, { method: 'GET' })
}

export async function createOrder(order) {
  // order: { customerName: string, items: [{ productId, quantity, unitPrice }] }
  return requestJson(getOrdersBaseUrl(), {
    method: 'POST',
    body: JSON.stringify(order),
  })
}

export async function updateOrderStatus(id, status) {
  const url = `${getOrdersBaseUrl()}/${id}/status?status=${encodeURIComponent(status)}`
  return requestJson(url, { method: 'PATCH' })
}

export async function reserveStock(orderId) {
  return requestJson(`${getOrdersBaseUrl()}/${orderId}/reserve`, { method: 'POST' })
}
