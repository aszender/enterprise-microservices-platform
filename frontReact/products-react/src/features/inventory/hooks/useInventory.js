// features/inventory/hooks/useInventory.js
// Hook layer over Redux store for Inventory feature

import { useCallback, useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { fetchInventory } from '../model/inventorySlice'

export function useInventory() {
  const dispatch = useDispatch()
  const { items, loading, errorMessage } = useSelector((state) => state.inventory)

  const refresh = useCallback(() => {
    dispatch(fetchInventory())
  }, [dispatch])

  // Auto-load on mount
  useEffect(() => {
    refresh()
  }, [refresh])

  return {
    items,
    loading,
    errorMessage,
    count: items.length,
    busy: loading,
    refresh,
  }
}
