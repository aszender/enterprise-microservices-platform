// features/auth/api/authApi.js
// JWT auth endpoints (Spring Boot):
// - POST /api/auth/login
// - POST /api/auth/register
// - GET  /api/auth/me

import { requestJson } from '../../../shared/api/http'

const BASE_URL = '/api/auth'

export function login(username, password) {
  return requestJson(`${BASE_URL}/login`, {
    method: 'POST',
    body: JSON.stringify({ username, password }),
  })
}

export function register(username, password) {
  return requestJson(`${BASE_URL}/register`, {
    method: 'POST',
    body: JSON.stringify({ username, password }),
  })
}

export function me() {
  return requestJson(`${BASE_URL}/me`, { method: 'GET' })
}

export function logout() {
  return requestJson(`${BASE_URL}/logout`, { method: 'POST' })
}
