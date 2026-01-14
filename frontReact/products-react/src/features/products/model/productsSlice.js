// features/products/model/productsSlice.js
// Redux slice for Products CRUD state (mirrors Vue productsStore)

import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'
import {
  listProducts,
  createProduct as apiCreate,
  updateProduct as apiUpdate,
  deleteProduct as apiDelete,
} from '../api/productsApi'

// Async thunks
export const fetchProducts = createAsyncThunk('products/fetchAll', async (_, { rejectWithValue }) => {
  try {
    return await listProducts()
  } catch (err) {
    return rejectWithValue(err.message)
  }
})

export const addProduct = createAsyncThunk('products/add', async (product, { rejectWithValue }) => {
  try {
    return await apiCreate(product)
  } catch (err) {
    return rejectWithValue(err.message)
  }
})

export const editProduct = createAsyncThunk('products/edit', async ({ id, product }, { rejectWithValue }) => {
  try {
    return await apiUpdate(id, product)
  } catch (err) {
    return rejectWithValue(err.message)
  }
})

export const removeProduct = createAsyncThunk('products/remove', async (id, { rejectWithValue }) => {
  try {
    await apiDelete(id)
    return id
  } catch (err) {
    return rejectWithValue(err.message)
  }
})

const initialState = {
  items: [],
  loading: false,
  errorMessage: '',
  editingProduct: null,
}

const productsSlice = createSlice({
  name: 'products',
  initialState,
  reducers: {
    startEdit(state, action) {
      state.editingProduct = action.payload ? { ...action.payload } : null
    },
    cancelEdit(state) {
      state.editingProduct = null
    },
    clearError(state) {
      state.errorMessage = ''
    },
  },
  extraReducers: (builder) => {
    builder
      // Fetch
      .addCase(fetchProducts.pending, (state) => {
        state.loading = true
        state.errorMessage = ''
      })
      .addCase(fetchProducts.fulfilled, (state, action) => {
        state.loading = false
        state.items = action.payload
      })
      .addCase(fetchProducts.rejected, (state, action) => {
        state.loading = false
        state.errorMessage = action.payload || 'Failed to load products'
      })
      // Add
      .addCase(addProduct.pending, (state) => {
        state.loading = true
      })
      .addCase(addProduct.fulfilled, (state, action) => {
        state.loading = false
        state.items.push(action.payload)
      })
      .addCase(addProduct.rejected, (state, action) => {
        state.loading = false
        state.errorMessage = action.payload || 'Failed to create product'
      })
      // Edit
      .addCase(editProduct.pending, (state) => {
        state.loading = true
      })
      .addCase(editProduct.fulfilled, (state, action) => {
        state.loading = false
        state.editingProduct = null
        const idx = state.items.findIndex((p) => p.id === action.payload.id)
        if (idx !== -1) {
          state.items[idx] = action.payload
        }
      })
      .addCase(editProduct.rejected, (state, action) => {
        state.loading = false
        state.errorMessage = action.payload || 'Failed to update product'
      })
      // Remove
      .addCase(removeProduct.pending, (state) => {
        state.loading = true
      })
      .addCase(removeProduct.fulfilled, (state, action) => {
        state.loading = false
        state.items = state.items.filter((p) => p.id !== action.payload)
        if (state.editingProduct?.id === action.payload) {
          state.editingProduct = null
        }
      })
      .addCase(removeProduct.rejected, (state, action) => {
        state.loading = false
        state.errorMessage = action.payload || 'Failed to delete product'
      })
  },
})

export const { startEdit, cancelEdit, clearError } = productsSlice.actions
export default productsSlice.reducer
