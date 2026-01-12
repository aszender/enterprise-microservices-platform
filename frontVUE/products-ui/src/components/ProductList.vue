<script setup>
// ProductList.vue
// Purpose: Read operation UI — displays a list of products and provides edit/delete actions.

const props = defineProps({
  products: {
    type: Array,
    required: true,
  },

  busy: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits([
  // Emitted when user clicks edit on a product.
  'edit',
  // Emitted when user clicks delete on a product.
  'delete',
])

function formatPrice(price) {
  if (price == null) return ''
  // Keep it simple (learning repo): show 2 decimals.
  return Number(price).toFixed(2)
}
</script>

<template>
  <section class="rounded-2xl border border-white/10 bg-white/5 p-6">
    <h2 class="text-lg font-semibold">Products</h2>

    <p v-if="products.length === 0" class="mt-3 text-sm text-white/70">
      No products yet.
    </p>

    <ul v-else class="mt-4 divide-y divide-white/10">
      <li
        v-for="p in products"
        :key="p.id"
        class="flex items-start justify-between gap-4 py-4"
      >
        <!-- Display fields that match the backend entity: id, name, description, price -->
        <div class="min-w-0 text-left">
          <div class="flex flex-wrap items-baseline gap-2">
            <span class="font-semibold">{{ p.name }}</span>
            <span class="text-xs text-white/50">#{{ p.id }}</span>
          </div>
          <p v-if="p.description" class="mt-1 text-sm text-white/70">
            {{ p.description }}
          </p>
          <p class="mt-1 text-sm font-semibold">$ {{ formatPrice(p.price) }}</p>
        </div>

        <!-- CRUD actions: Update + Delete -->
        <div class="flex shrink-0 gap-2">
          <button
            type="button"
            :disabled="busy"
            class="rounded-lg border border-white/15 bg-white/10 px-3 py-2 text-sm font-medium hover:bg-white/15 disabled:opacity-50"
            @click="emit('edit', p)"
          >
            Edit
          </button>
          <button
            type="button"
            :disabled="busy"
            class="rounded-lg border border-red-400/30 bg-red-500/10 px-3 py-2 text-sm font-medium text-red-200 hover:bg-red-500/15 disabled:opacity-50"
            @click="emit('delete', p)"
          >
            Delete
          </button>
        </div>
      </li>
    </ul>
  </section>
</template>

