// src/composables/useOrders.js
// Composable hook for orders

import { onMounted } from 'vue'
import { useOrdersStore } from '../stores/ordersStore'

export function useOrders() {
  const store = useOrdersStore()

  onMounted(() => {
    store.refresh()
  })

  return store
}
