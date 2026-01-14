// src/api/authApi.js
// JWT auth endpoints (Spring Boot):
// - POST /api/auth/login
// - POST /api/auth/register
// - GET  /api/auth/me

async function toHttpError(response) {
  let bodyText = ''
  try {
    bodyText = await response.text()
  } catch {
    // ignore
  }

  const message = bodyText
    ? `HTTP ${response.status} ${response.statusText}: ${bodyText}`
    : `HTTP ${response.status} ${response.statusText}`

  const err = new Error(message)
  err.status = response.status
  throw err
}

async function requestJson(url, options) {
  const res = await fetch(url, {
    credentials: options?.credentials ?? 'include',
    headers: {
      'Content-Type': 'application/json',
      ...(options?.headers ?? {}),
    },
    ...options,
  })

  if (!res.ok) {
    await toHttpError(res)
  }

  if (res.status === 204) return null
  return res.json()
}

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
