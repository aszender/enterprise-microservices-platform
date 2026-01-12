<script setup>
// NameCard.vue
// Purpose: Demonstrate "props down, events up" using Composition API macros.
//
// PROPS DOWN: parent passes data into the child
const props = defineProps({
  name: { type: String, required: true },
  disabled: { type: Boolean, default: false },
})

// EVENTS UP: child notifies parent via an emitted event
const emit = defineEmits(['clear'])

function requestClear() {
  emit('clear')
}
</script>

<template>
  <div
    style="
      margin: 1rem 0;
      padding: 1rem;
      border: 1px solid rgba(255, 255, 255, 0.2);
      border-radius: 8px;
    "
  >
    <p>
      <strong>Child component:</strong>
      I received name via <code>props</code> → "{{ props.name }}"
    </p>

    <button type="button" :disabled="props.disabled" @click="requestClear">
      Clear name (emit event up)
    </button>

    <p style="opacity: 0.8; margin-top: 0.5rem">
      Note: child does NOT change parent state directly; it emits an event.
    </p>
  </div>
</template>
