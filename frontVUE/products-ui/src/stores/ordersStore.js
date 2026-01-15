// src/stores/ordersStore.js
// Singleton store for orders state + CRUD actions

import { computed, ref } from 'vue'
import {
  createOrder,
  listOrders,
  reserveStock,
  updateOrderStatus,
} from '../api/ordersApi'

// STATE
const items = ref([])
const loading = ref(false)
const errorMessage = ref('')

// DERIVED STATE
const count = computed(() => items.value.length)
const busy = computed(() => loading.value)

// ACTIONS
async function refresh() {
  loading.value = true
  errorMessage.value = ''
  try {
    items.value = await listOrders()
  } catch (err) {
    errorMessage.value = err?.message || 'Failed to load orders'
  } finally {
    loading.value = false
  }
}

async function create(orderPayload) {
  loading.value = true
  errorMessage.value = ''
  try {
    await createOrder(orderPayload)
    await refresh()
  } catch (err) {
    errorMessage.value = err?.message || 'Create order failed'
  } finally {
    loading.value = false
  }
}

async function reserve(orderId) {
  loading.value = true
  errorMessage.value = ''
  try {
    await reserveStock(orderId)
    await refresh()
  } catch (err) {
    errorMessage.value = err?.message || 'Reserve stock failed'
  } finally {
    loading.value = false
  }
}

async function updateStatus(orderId, status) {
  loading.value = true
  errorMessage.value = ''
  try {
    await updateOrderStatus(orderId, status)
    await refresh()
  } catch (err) {
    errorMessage.value = err?.message || 'Update status failed'
  } finally {
    loading.value = false
  }
}

export function useOrdersStore() {
  return {
    items,
    loading,
    errorMessage,
    count,
    busy,
    refresh,
    create,
    reserve,
    updateStatus,
  }
}
