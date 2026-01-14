// features/products/hooks/useProducts.js
// Hook layer over Redux store for Products feature

import { useCallback, useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import {
  fetchProducts,
  addProduct,
  editProduct,
  removeProduct,
  startEdit,
  cancelEdit,
} from '../model/productsSlice'

export function useProducts() {
  const dispatch = useDispatch()
  const { items, loading, errorMessage, editingProduct } = useSelector((state) => state.products)

  const refresh = useCallback(() => {
    dispatch(fetchProducts())
  }, [dispatch])

  // Auto-load on mount
  useEffect(() => {
    refresh()
  }, [refresh])

  const save = useCallback(
    async (productPayload) => {
      if (editingProduct?.id) {
        await dispatch(editProduct({ id: editingProduct.id, product: productPayload }))
      } else {
        await dispatch(addProduct(productPayload))
      }
    },
    [dispatch, editingProduct]
  )

  const remove = useCallback(
    async (product) => {
      if (!window.confirm(`Delete "${product.name}"?`)) return
      await dispatch(removeProduct(product.id))
    },
    [dispatch]
  )

  const handleStartEdit = useCallback(
    (product) => {
      dispatch(startEdit(product))
    },
    [dispatch]
  )

  const handleCancelEdit = useCallback(() => {
    dispatch(cancelEdit())
  }, [dispatch])

  return {
    // State
    items,
    loading,
    errorMessage,
    editingProduct,
    // Derived
    count: items.length,
    busy: loading,
    // Actions
    refresh,
    save,
    remove,
    startEdit: handleStartEdit,
    cancelEdit: handleCancelEdit,
  }
}
