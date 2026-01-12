// src/composables/useProducts.js
// Composable: a convenient "hook" layer over the store.
// In larger apps this is where you'd normalize data, map DTOs, etc.

import { onMounted } from 'vue'
import { useProductsStore } from '../stores/productsStore'

export function useProducts() {
  const store = useProductsStore()

  // Auto-load products when the page/component mounts.
  onMounted(() => {
    store.refresh()
  })

  return store
}
