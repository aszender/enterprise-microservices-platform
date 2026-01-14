// features/products/model/productsStore.js
// Minimal "store-like" state for the Products feature.
// Intentionally simple (no Redux/Zustand) — you can evolve it later.

import { useCallback, useEffect, useMemo, useState } from 'react'
import { listProducts } from '../api/productsApi'

export function useProductsStore() {
  const [items, setItems] = useState([])
  const [loading, setLoading] = useState(false)
  const [errorMessage, setErrorMessage] = useState('')

  const refresh = useCallback(async () => {
    setLoading(true)
    setErrorMessage('')
    try {
      const data = await listProducts()
      setItems(Array.isArray(data) ? data : [])
    } catch (err) {
      setErrorMessage(err?.message || 'Failed to load products')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    refresh()
  }, [refresh])

  const count = useMemo(() => items.length, [items])
  const busy = loading

  return {
    // state
    items,
    loading,
    errorMessage,
    // derived
    count,
    busy,
    // actions
    refresh,
  }
}
