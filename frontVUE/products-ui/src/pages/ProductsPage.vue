<script setup>
// src/pages/ProductsPage.vue
// "Page" component: composes UI components and binds them to the store/composable.

import ProductForm from '../components/ProductForm.vue'
import ProductList from '../components/ProductList.vue'
import { useProducts } from '../composables/useProducts'

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
</script>

<template>
  <div class="mx-auto w-full max-w-5xl px-4 py-10">
    <header class="mb-6">
      <h1 class="text-3xl font-semibold tracking-tight">Products</h1>
      <p class="mt-1 text-sm text-white/70">Count: {{ count }}</p>
    </header>

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
