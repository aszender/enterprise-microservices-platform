// src/stores/inventoryStore.js
// Singleton store for inventory state

import { computed, ref } from 'vue'
import { listStock } from '../api/inventoryApi'

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
    items.value = await listStock()
  } catch (err) {
    errorMessage.value = err?.message || 'Failed to load inventory'
  } finally {
    loading.value = false
  }
}

export function useInventoryStore() {
  return {
    items,
    loading,
    errorMessage,
    count,
    busy,
    refresh,
  }
}
