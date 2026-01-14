// features/auth/hooks/useAuth.js

import { useCallback, useEffect, useRef, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { fetchMe, login as loginThunk, logout as logoutThunk } from '../model/authSlice'

export function useAuth() {
  const dispatch = useDispatch()
  const { username, role, loading, errorMessage } = useSelector((s) => s.auth)

  const didInit = useRef(false)
  useEffect(() => {
    if (didInit.current) return
    didInit.current = true
    dispatch(fetchMe())
  }, [dispatch])

  const [form, setForm] = useState({ username: '', password: '' })

  const onChange = useCallback((field, value) => {
    setForm((prev) => ({ ...prev, [field]: value }))
  }, [])

  const login = useCallback(async () => {
    await dispatch(loginThunk(form))
  }, [dispatch, form])

  const logout = useCallback(() => {
    dispatch(logoutThunk())
  }, [dispatch])

  return {
    username,
    role,
    isAuthenticated: Boolean(username),
    loading,
    errorMessage,
    form,
    onChange,
    login,
    logout,
  }
}
