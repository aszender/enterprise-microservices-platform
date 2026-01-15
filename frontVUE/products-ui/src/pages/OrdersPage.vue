<script setup>
// src/pages/OrdersPage.vue
// Orders page: create orders + view list + reserve stock

import { useOrders } from '../composables/useOrders'
import { useProducts } from '../composables/useProducts'
import { ref, computed } from 'vue'

const {
  items: orders,
  errorMessage,
  loading,
  busy,
  count,
  refresh,
  create,
  reserve,
  updateStatus,
} = useOrders()

const { items: products } = useProducts()

// Form state
const customerName = ref('')
const orderItems = ref([{ productId: '', quantity: 1 }])

function addItem() {
  orderItems.value.push({ productId: '', quantity: 1 })
}

function removeItem(index) {
  orderItems.value.splice(index, 1)
}

const canSubmit = computed(() => {
  return customerName.value.trim() && orderItems.value.some(i => i.productId && i.quantity > 0)
})

async function submitOrder() {
  const validItems = orderItems.value
    .filter(i => i.productId && i.quantity > 0)
    .map(i => {
      const product = products.value.find(p => p.id === Number(i.productId))
      return {
        productId: Number(i.productId),
        quantity: i.quantity,
        unitPrice: product?.price || 0,
      }
    })

  await create({
    customerName: customerName.value,
    items: validItems,
  })

  // Reset form
  customerName.value = ''
  orderItems.value = [{ productId: '', quantity: 1 }]
}

function getStatusColor(status) {
  switch (status) {
    case 'CREATED': return 'bg-blue-500/20 text-blue-200 border-blue-400/30'
    case 'RESERVED': return 'bg-green-500/20 text-green-200 border-green-400/30'
    case 'CANCELLED': return 'bg-red-500/20 text-red-200 border-red-400/30'
    case 'COMPLETED': return 'bg-purple-500/20 text-purple-200 border-purple-400/30'
    default: return 'bg-white/10 text-white/70 border-white/20'
  }
}

function formatPrice(price) {
  if (price == null) return ''
  return Number(price).toFixed(2)
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString()
}
</script>

<template>
  <div class="mx-auto w-full max-w-5xl px-4 py-10">
    <header class="mb-6">
      <h1 class="text-3xl font-semibold tracking-tight">Orders</h1>
      <p class="mt-1 text-sm text-white/70">Count: {{ count }}</p>
    </header>

    <!-- Error -->
    <p v-if="errorMessage" class="mb-4 rounded-lg border border-red-400/30 bg-red-500/10 p-3 text-sm text-red-200">
      {{ errorMessage }}
    </p>
    <p v-else-if="loading" class="mb-4 text-sm text-white/70">Loading…</p>

    <div class="grid grid-cols-1 gap-6 lg:grid-cols-2">
      <!-- CREATE ORDER FORM -->
      <section class="rounded-2xl border border-white/10 bg-white/5 p-6">
        <h2 class="text-lg font-semibold mb-4">Create Order</h2>

        <form @submit.prevent="submitOrder" class="space-y-4">
          <label class="grid gap-1 text-sm">
            <span class="text-white/70">Customer Name</span>
            <input
              v-model="customerName"
              class="w-full rounded-lg border border-white/15 bg-white/10 px-3 py-2 text-sm text-white placeholder:text-white/40"
              placeholder="John Doe"
            />
          </label>

          <div class="space-y-3">
            <div class="flex items-center justify-between">
              <span class="text-sm text-white/70">Order Items</span>
              <button
                type="button"
                class="rounded-lg border border-white/15 bg-white/10 px-3 py-1 text-xs font-medium hover:bg-white/15"
                @click="addItem"
              >
                + Add Item
              </button>
            </div>

            <div
              v-for="(item, idx) in orderItems"
              :key="idx"
              class="flex gap-2 items-end"
            >
              <label class="grid gap-1 text-sm flex-1">
                <span class="text-white/70">Product</span>
                <select
                  v-model="item.productId"
                  class="w-full rounded-lg border border-white/15 bg-white/10 px-3 py-2 text-sm text-white"
                >
                  <option value="">Select product</option>
                  <option v-for="p in products" :key="p.id" :value="p.id">
                    {{ p.name }} (${{ formatPrice(p.price) }})
                  </option>
                </select>
              </label>

              <label class="grid gap-1 text-sm w-24">
                <span class="text-white/70">Qty</span>
                <input
                  v-model.number="item.quantity"
                  type="number"
                  min="1"
                  class="w-full rounded-lg border border-white/15 bg-white/10 px-3 py-2 text-sm text-white"
                />
              </label>

              <button
                v-if="orderItems.length > 1"
                type="button"
                class="rounded-lg border border-red-400/30 bg-red-500/10 px-3 py-2 text-sm text-red-200 hover:bg-red-500/15"
                @click="removeItem(idx)"
              >
                ×
              </button>
            </div>
          </div>

          <button
            type="submit"
            :disabled="!canSubmit || busy"
            class="w-full rounded-lg border border-white/15 bg-white/10 px-4 py-2 text-sm font-medium hover:bg-white/15 disabled:opacity-50"
          >
            {{ busy ? 'Creating…' : 'Create Order' }}
          </button>
        </form>
      </section>

      <!-- ORDERS LIST -->
      <section class="rounded-2xl border border-white/10 bg-white/5 p-6">
        <h2 class="text-lg font-semibold mb-4">Orders List</h2>

        <p v-if="orders.length === 0" class="text-sm text-white/70">
          No orders yet.
        </p>

        <ul v-else class="space-y-4 max-h-[500px] overflow-y-auto">
          <li
            v-for="order in orders"
            :key="order.id"
            class="rounded-xl border border-white/10 bg-white/5 p-4"
          >
            <div class="flex items-start justify-between gap-3">
              <div class="min-w-0">
                <div class="flex flex-wrap items-center gap-2">
                  <span class="font-semibold">Order #{{ order.id }}</span>
                  <span
                    class="rounded-full px-2 py-0.5 text-xs font-medium border"
                    :class="getStatusColor(order.status)"
                  >
                    {{ order.status }}
                  </span>
                </div>
                <p class="mt-1 text-sm text-white/70">{{ order.customerName }}</p>
                <p class="text-xs text-white/50">{{ formatDate(order.createdAt) }}</p>
              </div>
              <p class="text-sm font-semibold shrink-0">${{ formatPrice(order.total) }}</p>
            </div>

            <!-- Items -->
            <div v-if="order.items?.length" class="mt-3 text-xs text-white/60">
              <span v-for="(item, i) in order.items" :key="item.id">
                {{ item.quantity }}x Product#{{ item.productId }}{{ i < order.items.length - 1 ? ', ' : '' }}
              </span>
            </div>

            <!-- Actions -->
            <div class="mt-3 flex flex-wrap gap-2">
              <button
                v-if="order.status === 'CREATED'"
                :disabled="busy"
                class="rounded-lg border border-green-400/30 bg-green-500/10 px-3 py-1.5 text-xs font-medium text-green-200 hover:bg-green-500/15 disabled:opacity-50"
                @click="reserve(order.id)"
              >
                Reserve Stock
              </button>
              <button
                v-if="order.status === 'RESERVED'"
                :disabled="busy"
                class="rounded-lg border border-purple-400/30 bg-purple-500/10 px-3 py-1.5 text-xs font-medium text-purple-200 hover:bg-purple-500/15 disabled:opacity-50"
                @click="updateStatus(order.id, 'COMPLETED')"
              >
                Complete
              </button>
              <button
                v-if="order.status !== 'CANCELLED' && order.status !== 'COMPLETED'"
                :disabled="busy"
                class="rounded-lg border border-red-400/30 bg-red-500/10 px-3 py-1.5 text-xs font-medium text-red-200 hover:bg-red-500/15 disabled:opacity-50"
                @click="updateStatus(order.id, 'CANCELLED')"
              >
                Cancel
              </button>
            </div>
          </li>
        </ul>
      </section>
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
