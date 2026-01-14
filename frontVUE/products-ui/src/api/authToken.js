// src/api/authToken.js
// Minimal token storage helper for JWT.

const TOKEN_KEY = 'products-ui.authToken'

export function getAuthToken() {
  try {
    return localStorage.getItem(TOKEN_KEY) || ''
  } catch {
    return ''
  }
}

export function setAuthToken(token) {
  try {
    if (!token) {
      localStorage.removeItem(TOKEN_KEY)
    } else {
      localStorage.setItem(TOKEN_KEY, token)
    }
  } catch {
    // ignore
  }
}

export function clearAuthToken() {
  setAuthToken('')
}
