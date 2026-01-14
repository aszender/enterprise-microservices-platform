<script setup>
// src/pages/ProductsPage.vue
// "Page" component: composes UI components and binds them to the store/composable.

import ProductForm from '../components/ProductForm.vue'
import ProductList from '../components/ProductList.vue'
import { useProducts } from '../composables/useProducts'
import { ref } from 'vue'
import { useAuth } from '../composables/useAuth'

const {
  items: products,
  errorMessage,
  loading,
  busy,
  count,
  editingProduct,
  refresh,
  startEdit,
  cancelEdit,
  save,
  remove,
} = useProducts()

const auth = useAuth()
const loginForm = ref({ username: '', password: '' })

async function onLogin() {
  await auth.login(loginForm.value)
  loginForm.value.password = ''
}
</script>

<template>
  <div class="mx-auto w-full max-w-5xl px-4 py-10">
    <header class="mb-6">
      <h1 class="text-3xl font-semibold tracking-tight">Products</h1>
      <p class="mt-1 text-sm text-white/70">Count: {{ count }}</p>
    </header>

    <!-- Auth (JWT) -->
    <section class="mb-6 rounded-xl border border-white/10 bg-white/5 p-4">
      <h2 class="text-sm font-semibold">Authentication</h2>

      <p v-if="auth.errorMessage" class="mt-3 rounded-lg border border-red-400/30 bg-red-500/10 p-3 text-sm text-red-200">
        {{ auth.errorMessage }}
      </p>

      <div v-if="auth.isAuthenticated" class="mt-3 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <p class="text-sm text-white/70">
          Logged in as <span class="font-medium text-white">{{ auth.username || 'user' }}</span>
          <span v-if="auth.role" class="text-white/60"> ({{ auth.role }})</span>
        </p>
        <button
          type="button"
          class="rounded-lg border border-white/15 bg-white/10 px-4 py-2 text-sm font-medium hover:bg-white/15"
          @click="auth.logout"
        >
          Logout
        </button>
      </div>

      <form v-else class="mt-3 grid grid-cols-1 gap-3 sm:grid-cols-3 sm:items-end" @submit.prevent="onLogin">
        <label class="grid gap-1 text-sm">
          <span class="text-white/70">Username</span>
          <input
            v-model="loginForm.username"
            class="w-full rounded-lg border border-white/15 bg-white/10 px-3 py-2 text-sm text-white placeholder:text-white/40"
            placeholder="admin"
            autocomplete="username"
          />
        </label>

        <label class="grid gap-1 text-sm">
          <span class="text-white/70">Password</span>
          <input
            v-model="loginForm.password"
            type="password"
            class="w-full rounded-lg border border-white/15 bg-white/10 px-3 py-2 text-sm text-white placeholder:text-white/40"
            placeholder="admin123"
            autocomplete="current-password"
          />
        </label>

        <button
          type="submit"
          class="rounded-lg border border-white/15 bg-white/10 px-4 py-2 text-sm font-medium hover:bg-white/15 disabled:opacity-50"
          :disabled="auth.busy"
        >
          {{ auth.busy ? 'Signing in…' : 'Login' }}
        </button>
      </form>

      <p class="mt-3 text-xs text-white/50">
        Creating, updating, and deleting products requires a JWT.
      </p>
    </section>

    <!-- Status / error -->
    <p v-if="errorMessage" class="mb-4 rounded-lg border border-red-400/30 bg-red-500/10 p-3 text-sm text-red-200">
      {{ errorMessage }}
    </p>
    <p v-else-if="loading" class="mb-4 text-sm text-white/70">Loading…</p>

    <div class="grid grid-cols-1 gap-6 md:grid-cols-2">
      <!-- CREATE + UPDATE -->
      <ProductForm
        :initial-product="editingProduct"
        :busy="busy"
        @save="save"
        @cancel="cancelEdit"
      />

      <!-- READ + DELETE + choose product to edit -->
      <ProductList
        :products="products"
        :busy="busy"
        @edit="startEdit"
        @delete="remove"
      />
    </div>

    <div class="mt-6 flex justify-end">
      <button
        type="button"
        class="rounded-lg border border-white/15 bg-white/10 px-4 py-2 text-sm font-medium hover:bg-white/15 disabled:opacity-50"
        :disabled="busy"
        @click="refresh"
      >
        Refresh
      </button>
    </div>
  </div>
</template>
