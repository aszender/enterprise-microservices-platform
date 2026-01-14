// app/store.js
// Redux store configuration

import { configureStore } from '@reduxjs/toolkit'
import productsReducer from '../features/products/model/productsSlice'
import authReducer from '../features/auth/model/authSlice'

export const store = configureStore({
  reducer: {
    auth: authReducer,
    products: productsReducer,
  },
})
