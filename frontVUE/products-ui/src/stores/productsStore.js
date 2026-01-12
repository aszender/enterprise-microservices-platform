// src/stores/productsStore.js
// A tiny store (singleton module) to centralize products state + CRUD actions.
// This is "store-like" without adding extra dependencies (Pinia/Redux).

import { computed, ref } from 'vue'
import {
  createProduct,
  deleteProduct,
  listProducts,
  updateProduct,
} from '../api/productsApi'

// STATE (shared singleton)
const items = ref([])
const loading = ref(false)
const errorMessage = ref('')
const editingProduct = ref(null)

// DERIVED STATE
const count = computed(() => items.value.length)
const busy = computed(() => loading.value)

// ACTIONS
async function refresh() {
  loading.value = true
  errorMessage.value = ''
  try {
    items.value = await listProducts()
  } catch (err) {
    errorMessage.value = err?.message || 'Failed to load products'
  } finally {
    loading.value = false
  }
}

function startEdit(product) {
  editingProduct.value = product ? { ...product } : null
}

function cancelEdit() {
  editingProduct.value = null
}

async function save(productPayload) {
  // CREATE vs UPDATE is decided by whether the store has an editingProduct.
  loading.value = true
  errorMessage.value = ''
  try {
    if (editingProduct.value?.id) {
      await updateProduct(editingProduct.value.id, productPayload)
      editingProduct.value = null
    } else {
      await createProduct(productPayload)
    }

    await refresh()
  } catch (err) {
    errorMessage.value = err?.message || 'Save failed'
  } finally {
    loading.value = false
  }
}

async function remove(product) {
  const ok = confirm(`Delete "${product.name}"?`)
  if (!ok) return

  loading.value = true
  errorMessage.value = ''
  try {
    await deleteProduct(product.id)

    if (editingProduct.value?.id === product.id) {
      editingProduct.value = null
    }

    await refresh()
  } catch (err) {
    errorMessage.value = err?.message || 'Delete failed'
  } finally {
    loading.value = false
  }
}

// Store accessor
export function useProductsStore() {
  return {
    // state
    items,
    loading,
    errorMessage,
    editingProduct,
    // derived
    count,
    busy,
    // actions
    refresh,
    startEdit,
    cancelEdit,
    save,
    remove,
  }
}
