<script setup>
// ProductForm.vue
// Purpose: A reusable form used for BOTH create and update.
// - If `initialProduct` is null -> create mode
// - If `initialProduct` is set  -> edit mode

import { computed, ref, watch } from 'vue'

const props = defineProps({
  // When editing, we pass an existing product object with id/name/description/price.
  // When creating, we pass null.
  initialProduct: {
    type: Object,
    default: null,
  },

  // Used to disable the form while API calls are in flight.
  busy: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits([
  // Emitted when user submits; payload matches backend Product fields.
  'save',
  // Emitted when user cancels editing.
  'cancel',
])

// Local form state (we intentionally copy fields instead of mutating props).
const name = ref('')
const description = ref('')
const price = ref('')

// Keep form in sync whenever the selected product changes.
watch(
  () => props.initialProduct,
  (p) => {
    name.value = p?.name ?? ''
    description.value = p?.description ?? ''
    // Store as string for input convenience; convert to number on submit.
    price.value = p?.price != null ? String(p.price) : ''
  },
  { immediate: true }
)

const isEditMode = computed(() => Boolean(props.initialProduct?.id))

function submit() {
  // Very small client-side validation for learning/demo.
  const trimmedName = name.value.trim()
  const numericPrice = Number(price.value)

  if (!trimmedName) {
    alert('Name is required')
    return
  }
  if (Number.isNaN(numericPrice)) {
    alert('Price must be a number')
    return
  }

  // Emit exactly what the backend expects.
  // For update, the id is handled by the parent (PUT /{id}).
  emit('save', {
    name: trimmedName,
    description: description.value.trim(),
    price: numericPrice,
  })
}
</script>

<template>
  <section class="rounded-2xl border border-white/10 bg-white/5 p-6">
    <!-- Title changes based on create vs update -->
    <h2 class="text-lg font-semibold">
      {{ isEditMode ? 'Edit product' : 'Create product' }}
    </h2>

    <!-- Form fields -->
    <form class="mt-4 space-y-4" @submit.prevent="submit">
      <label class="block text-left">
        <span class="text-sm font-medium text-white/80">Name</span>
        <input
          v-model="name"
          :disabled="busy"
          placeholder="Product name"
          class="mt-1 w-full rounded-lg border border-white/10 bg-black/20 px-3 py-2 text-sm outline-none ring-0 placeholder:text-white/40 focus:border-white/25 disabled:opacity-60"
        />
      </label>

      <label class="block text-left">
        <span class="text-sm font-medium text-white/80">Description</span>
        <textarea
          v-model="description"
          :disabled="busy"
          placeholder="Optional description"
          rows="3"
          class="mt-1 w-full resize-none rounded-lg border border-white/10 bg-black/20 px-3 py-2 text-sm outline-none placeholder:text-white/40 focus:border-white/25 disabled:opacity-60"
        />
      </label>

      <label class="block text-left">
        <span class="text-sm font-medium text-white/80">Price</span>
        <input
          v-model="price"
          :disabled="busy"
          inputmode="decimal"
          placeholder="e.g. 19.99"
          class="mt-1 w-full rounded-lg border border-white/10 bg-black/20 px-3 py-2 text-sm outline-none placeholder:text-white/40 focus:border-white/25 disabled:opacity-60"
        />
      </label>

      <!-- Actions -->
      <div class="flex gap-3">
        <button
          type="submit"
          :disabled="busy"
          class="rounded-lg bg-white px-4 py-2 text-sm font-semibold text-zinc-900 hover:bg-white/90 disabled:opacity-50"
        >
          {{ isEditMode ? 'Update' : 'Create' }}
        </button>

        <button
          type="button"
          :disabled="busy"
          class="rounded-lg border border-white/15 bg-white/10 px-4 py-2 text-sm font-medium hover:bg-white/15 disabled:opacity-50"
          @click="emit('cancel')"
        >
          Cancel
        </button>
      </div>
    </form>
  </section>
</template>

