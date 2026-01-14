// shared/api/http.js
// Tiny fetch wrapper (kept dependency-free on purpose).

export async function requestJson(url, options) {
  const res = await fetch(url, {
    // Cookie-based auth (HttpOnly JWT) requires credentials.
    credentials: options?.credentials ?? 'include',
    headers: {
      'Content-Type': 'application/json',
      ...(options?.headers ?? {}),
    },
    ...options,
  })

  if (!res.ok) {
    let bodyText = ''
    try {
      bodyText = await res.text()
    } catch {
      // ignore
    }

    const message = bodyText
      ? `HTTP ${res.status} ${res.statusText}: ${bodyText}`
      : `HTTP ${res.status} ${res.statusText}`

    const err = new Error(message)
    err.status = res.status
    throw err
  }

  // DELETE often returns 204 No Content.
  if (res.status === 204) return null

  return res.json()
}
