// features/orders/api/ordersApi.js
// API client for Orders service (port 8081)

import { requestJson } from '../../../shared/api/http'

const DEFAULT_BASE_URL = '/api/orders'

export function getOrdersBaseUrl() {
  return import.meta.env.VITE_ORDERS_API_BASE_URL || DEFAULT_BASE_URL
}

export function listOrders() {
  return requestJson(getOrdersBaseUrl(), { method: 'GET' })
}

export function getOrder(id) {
  return requestJson(`${getOrdersBaseUrl()}/${id}`, { method: 'GET' })
}

export function createOrder(order) {
  return requestJson(getOrdersBaseUrl(), {
    method: 'POST',
    body: JSON.stringify(order),
  })
}

export function updateOrderStatus(id, status) {
  const url = `${getOrdersBaseUrl()}/${id}/status?status=${encodeURIComponent(status)}`
  return requestJson(url, { method: 'PATCH' })
}

export function reserveStock(orderId) {
  return requestJson(`${getOrdersBaseUrl()}/${orderId}/reserve`, { method: 'POST' })
}
