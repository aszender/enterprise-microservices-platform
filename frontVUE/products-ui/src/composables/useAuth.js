// src/composables/useAuth.js

import { onMounted } from 'vue'
import { useAuthStore } from '../stores/authStore'

export function useAuth() {
  const store = useAuthStore()

  onMounted(() => {
    store.refreshMe()
  })

  return store
}
