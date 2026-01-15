// app/store.js
// Redux store configuration

import { configureStore } from '@reduxjs/toolkit'
import productsReducer from '../features/products/model/productsSlice'
import authReducer from '../features/auth/model/authSlice'
import ordersReducer from '../features/orders/model/ordersSlice'
import inventoryReducer from '../features/inventory/model/inventorySlice'

export const store = configureStore({
  reducer: {
    auth: authReducer,
    products: productsReducer,
    orders: ordersReducer,
    inventory: inventoryReducer,
  },
})
