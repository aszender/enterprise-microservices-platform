// src/composables/useInventory.js
// Composable hook for inventory

import { onMounted } from 'vue'
import { useInventoryStore } from '../stores/inventoryStore'

export function useInventory() {
  const store = useInventoryStore()

  onMounted(() => {
    store.refresh()
  })

  return store
}
