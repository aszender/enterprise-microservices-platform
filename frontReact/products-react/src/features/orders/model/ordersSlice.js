// features/orders/model/ordersSlice.js
// Redux slice for Orders state

import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'
import {
  listOrders,
  createOrder as apiCreate,
  reserveStock as apiReserve,
  updateOrderStatus as apiUpdateStatus,
} from '../api/ordersApi'

// Async thunks
export const fetchOrders = createAsyncThunk('orders/fetchAll', async (_, { rejectWithValue }) => {
  try {
    return await listOrders()
  } catch (err) {
    return rejectWithValue(err.message)
  }
})

export const addOrder = createAsyncThunk('orders/add', async (order, { rejectWithValue }) => {
  try {
    return await apiCreate(order)
  } catch (err) {
    return rejectWithValue(err.message)
  }
})

export const reserveStock = createAsyncThunk('orders/reserve', async (orderId, { rejectWithValue }) => {
  try {
    return await apiReserve(orderId)
  } catch (err) {
    return rejectWithValue(err.message)
  }
})

export const updateStatus = createAsyncThunk('orders/updateStatus', async ({ id, status }, { rejectWithValue }) => {
  try {
    return await apiUpdateStatus(id, status)
  } catch (err) {
    return rejectWithValue(err.message)
  }
})

const initialState = {
  items: [],
  loading: false,
  errorMessage: '',
}

const ordersSlice = createSlice({
  name: 'orders',
  initialState,
  reducers: {
    clearError(state) {
      state.errorMessage = ''
    },
  },
  extraReducers: (builder) => {
    builder
      // Fetch
      .addCase(fetchOrders.pending, (state) => {
        state.loading = true
        state.errorMessage = ''
      })
      .addCase(fetchOrders.fulfilled, (state, action) => {
        state.loading = false
        state.items = action.payload
      })
      .addCase(fetchOrders.rejected, (state, action) => {
        state.loading = false
        state.errorMessage = action.payload || 'Failed to load orders'
      })
      // Add
      .addCase(addOrder.pending, (state) => {
        state.loading = true
      })
      .addCase(addOrder.fulfilled, (state, action) => {
        state.loading = false
        state.items.push(action.payload)
      })
      .addCase(addOrder.rejected, (state, action) => {
        state.loading = false
        state.errorMessage = action.payload || 'Failed to create order'
      })
      // Reserve
      .addCase(reserveStock.pending, (state) => {
        state.loading = true
      })
      .addCase(reserveStock.fulfilled, (state, action) => {
        state.loading = false
        const idx = state.items.findIndex((o) => o.id === action.payload.id)
        if (idx !== -1) state.items[idx] = action.payload
      })
      .addCase(reserveStock.rejected, (state, action) => {
        state.loading = false
        state.errorMessage = action.payload || 'Failed to reserve stock'
      })
      // Update status
      .addCase(updateStatus.pending, (state) => {
        state.loading = true
      })
      .addCase(updateStatus.fulfilled, (state, action) => {
        state.loading = false
        const idx = state.items.findIndex((o) => o.id === action.payload.id)
        if (idx !== -1) state.items[idx] = action.payload
      })
      .addCase(updateStatus.rejected, (state, action) => {
        state.loading = false
        state.errorMessage = action.payload || 'Failed to update status'
      })
  },
})

export const { clearError } = ordersSlice.actions
export default ordersSlice.reducer
