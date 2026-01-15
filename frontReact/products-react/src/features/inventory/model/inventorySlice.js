// features/inventory/model/inventorySlice.js
// Redux slice for Inventory state

import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'
import { listStock } from '../api/inventoryApi'

// Async thunks
export const fetchInventory = createAsyncThunk('inventory/fetchAll', async (_, { rejectWithValue }) => {
  try {
    return await listStock()
  } catch (err) {
    return rejectWithValue(err.message)
  }
})

const initialState = {
  items: [],
  loading: false,
  errorMessage: '',
}

const inventorySlice = createSlice({
  name: 'inventory',
  initialState,
  reducers: {
    clearError(state) {
      state.errorMessage = ''
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchInventory.pending, (state) => {
        state.loading = true
        state.errorMessage = ''
      })
      .addCase(fetchInventory.fulfilled, (state, action) => {
        state.loading = false
        state.items = action.payload
      })
      .addCase(fetchInventory.rejected, (state, action) => {
        state.loading = false
        state.errorMessage = action.payload || 'Failed to load inventory'
      })
  },
})

export const { clearError } = inventorySlice.actions
export default inventorySlice.reducer
