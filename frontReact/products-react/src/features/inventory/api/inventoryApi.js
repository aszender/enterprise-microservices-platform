// features/inventory/api/inventoryApi.js
// API client for Inventory service (port 8082)

import { requestJson } from '../../../shared/api/http'

const DEFAULT_BASE_URL = '/api/inventory'

export function getInventoryBaseUrl() {
  return import.meta.env.VITE_INVENTORY_API_BASE_URL || DEFAULT_BASE_URL
}

export function listStock() {
  return requestJson(`${getInventoryBaseUrl()}/stock`, { method: 'GET' })
}

export function getStock(productId) {
  return requestJson(`${getInventoryBaseUrl()}/stock/${productId}`, { method: 'GET' })
}
