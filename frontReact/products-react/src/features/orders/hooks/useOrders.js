// features/orders/hooks/useOrders.js
// Hook layer over Redux store for Orders feature

import { useCallback, useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import {
  fetchOrders,
  addOrder,
  reserveStock,
  updateStatus,
} from '../model/ordersSlice'

export function useOrders() {
  const dispatch = useDispatch()
  const { items, loading, errorMessage } = useSelector((state) => state.orders)

  const refresh = useCallback(() => {
    dispatch(fetchOrders())
  }, [dispatch])

  // Auto-load on mount
  useEffect(() => {
    refresh()
  }, [refresh])

  const create = useCallback(
    async (orderPayload) => {
      await dispatch(addOrder(orderPayload))
    },
    [dispatch]
  )

  const reserve = useCallback(
    async (orderId) => {
      await dispatch(reserveStock(orderId))
    },
    [dispatch]
  )

  const changeStatus = useCallback(
    async (orderId, status) => {
      await dispatch(updateStatus({ id: orderId, status }))
    },
    [dispatch]
  )

  return {
    items,
    loading,
    errorMessage,
    count: items.length,
    busy: loading,
    refresh,
    create,
    reserve,
    updateStatus: changeStatus,
  }
}
