// app/store.js
// Redux store configuration

import { configureStore } from '@reduxjs/toolkit'
import productsReducer from '../features/products/model/productsSlice'

export const store = configureStore({
  reducer: {
    products: productsReducer,
  },
})
