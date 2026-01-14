// src/stores/authStore.js
// Minimal auth store (singleton) for cookie-based JWT login state.

import { computed, ref } from 'vue'
import { login as apiLogin, logout as apiLogout, me as apiMe } from '../api/authApi'

const username = ref('')
const role = ref('')
const loading = ref(false)
const errorMessage = ref('')

const isAuthenticated = computed(() => Boolean(username.value))
const busy = computed(() => loading.value)

async function refreshMe() {
  loading.value = true
  errorMessage.value = ''
  try {
    const data = await apiMe()
    username.value = data?.username || ''
    role.value = data?.role || ''
  } catch (err) {
    // No cookie/session (401) should be silent.
    if (err?.status !== 401) {
      errorMessage.value = err?.message || 'Failed to load session'
    }
    username.value = ''
    role.value = ''
  } finally {
    loading.value = false
  }
}

async function login(credentials) {
  loading.value = true
  errorMessage.value = ''
  try {
    await apiLogin(credentials.username, credentials.password)
    await refreshMe()
  } catch (err) {
    errorMessage.value = err?.message || 'Login failed'
    username.value = ''
    role.value = ''
  } finally {
    loading.value = false
  }
}

async function logout() {
  loading.value = true
  errorMessage.value = ''
  try {
    await apiLogout()
  } catch (err) {
    // Even if server logout fails, clear local state.
    errorMessage.value = err?.message || ''
  } finally {
    username.value = ''
    role.value = ''
    loading.value = false
  }
}

export function useAuthStore() {
  return {
    username,
    role,
    isAuthenticated,
    busy,
    errorMessage,
    refreshMe,
    login,
    logout,
  }
}
