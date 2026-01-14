// features/auth/model/authSlice.js

import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import { login as apiLogin, logout as apiLogout, me as apiMe } from '../api/authApi'

export const fetchMe = createAsyncThunk('auth/me', async (_, { rejectWithValue }) => {
  try {
    return await apiMe()
  } catch (err) {
    // Treat unauthenticated as "no session" without surfacing a global error.
    if (err?.status === 401) return rejectWithValue({ silent: true })
    return rejectWithValue({ message: err?.message || 'Failed to load session' })
  }
})

export const login = createAsyncThunk(
  'auth/login',
  async ({ username, password }, { rejectWithValue }) => {
    try {
      // Login sets an HttpOnly cookie; then we fetch the user.
      await apiLogin(username, password)
      return await apiMe()
    } catch (err) {
      return rejectWithValue(err.message)
    }
  }
)

export const logout = createAsyncThunk('auth/logout', async (_, { rejectWithValue }) => {
  try {
    await apiLogout()
    return null
  } catch (err) {
    return rejectWithValue(err?.message || 'Logout failed')
  }
})

const initialState = {
  username: '',
  role: '',
  loading: false,
  errorMessage: '',
}

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    clearAuthError(state) {
      state.errorMessage = ''
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchMe.pending, (state) => {
        state.loading = true
      })
      .addCase(fetchMe.fulfilled, (state, action) => {
        state.loading = false
        state.username = action.payload?.username || ''
        state.role = action.payload?.role || ''
      })
      .addCase(fetchMe.rejected, (state, action) => {
        state.loading = false
        state.username = ''
        state.role = ''
        if (action.payload?.silent) return
        state.errorMessage = action.payload?.message || 'Failed to load session'
      })
      .addCase(login.pending, (state) => {
        state.loading = true
        state.errorMessage = ''
      })
      .addCase(login.fulfilled, (state, action) => {
        state.loading = false
        state.username = action.payload?.username || ''
        state.role = action.payload?.role || ''
      })
      .addCase(login.rejected, (state, action) => {
        state.loading = false
        state.errorMessage = action.payload || 'Login failed'
      })
      .addCase(logout.pending, (state) => {
        state.loading = true
        state.errorMessage = ''
      })
      .addCase(logout.fulfilled, (state) => {
        state.loading = false
        state.username = ''
        state.role = ''
        state.errorMessage = ''
      })
      .addCase(logout.rejected, (state, action) => {
        // Even if server logout fails, clear client state.
        state.loading = false
        state.username = ''
        state.role = ''
        state.errorMessage = action.payload || ''
      })
  },
})

export const { clearAuthError } = authSlice.actions
export default authSlice.reducer
