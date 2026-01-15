<script setup>
// src/pages/InventoryPage.vue
// Inventory page: view stock levels

import { useInventory } from '../composables/useInventory'

const {
  items: stockItems,
  errorMessage,
  loading,
  busy,
  count,
  refresh,
} = useInventory()

function getStockColor(available) {
  if (available <= 5) return 'text-red-300'
  if (available <= 20) return 'text-yellow-300'
  return 'text-green-300'
}
</script>

<template>
  <div class="mx-auto w-full max-w-5xl px-4 py-10">
    <header class="mb-6">
      <h1 class="text-3xl font-semibold tracking-tight">Inventory</h1>
      <p class="mt-1 text-sm text-white/70">Stock Items: {{ count }}</p>
    </header>

    <!-- Error -->
    <p v-if="errorMessage" class="mb-4 rounded-lg border border-red-400/30 bg-red-500/10 p-3 text-sm text-red-200">
      {{ errorMessage }}
    </p>
    <p v-else-if="loading" class="mb-4 text-sm text-white/70">Loading…</p>

    <!-- STOCK LIST -->
    <section class="rounded-2xl border border-white/10 bg-white/5 p-6">
      <h2 class="text-lg font-semibold mb-4">Stock Levels</h2>

      <p v-if="stockItems.length === 0" class="text-sm text-white/70">
        No stock items yet. Create products and they'll appear here.
      </p>

      <div v-else class="overflow-x-auto">
        <table class="w-full text-left text-sm">
          <thead>
            <tr class="border-b border-white/10">
              <th class="py-3 px-4 font-medium text-white/70">Product ID</th>
              <th class="py-3 px-4 font-medium text-white/70">Available</th>
              <th class="py-3 px-4 font-medium text-white/70">Reserved</th>
              <th class="py-3 px-4 font-medium text-white/70">Status</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="item in stockItems"
              :key="item.id"
              class="border-b border-white/5 hover:bg-white/5"
            >
              <td class="py-3 px-4">
                <span class="font-semibold">#{{ item.productId }}</span>
              </td>
              <td class="py-3 px-4">
                <span class="font-mono" :class="getStockColor(item.available)">
                  {{ item.available }}
                </span>
              </td>
              <td class="py-3 px-4">
                <span class="font-mono text-white/70">{{ item.reserved }}</span>
              </td>
              <td class="py-3 px-4">
                <span
                  v-if="item.available <= 5"
                  class="rounded-full border border-red-400/30 bg-red-500/20 px-2 py-0.5 text-xs font-medium text-red-200"
                >
                  Low Stock
                </span>
                <span
                  v-else-if="item.available <= 20"
                  class="rounded-full border border-yellow-400/30 bg-yellow-500/20 px-2 py-0.5 text-xs font-medium text-yellow-200"
                >
                  Medium
                </span>
                <span
                  v-else
                  class="rounded-full border border-green-400/30 bg-green-500/20 px-2 py-0.5 text-xs font-medium text-green-200"
                >
                  In Stock
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>

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
