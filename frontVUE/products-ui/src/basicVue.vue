<!--
  basicVue.vue
  Purpose: Your ORIGINAL App.vue example preserved for learning.
  It demonstrates core Vue 3 Composition API concepts:
  - ref() for reactive state
  - v-model for two-way binding
  - v-if vs v-show
  - @click events
  - onMounted() lifecycle hook for fetching data
  - computed() for derived values
  - watch() for reacting to changes
-->

<template>
  <!-- Simple heading -->
  <h1>Products</h1>

  <!-- Reactive state displayed in template -->
  <p>Count: {{ count }}</p>

  <!-- Dynamic binding: :src binds to imageURL -->
  <img :src="imageURL" />

  <!-- Two-way binding: v-model updates `name` as you type -->
  <input v-model="name" />
  <p>Your name is: {{ name }}</p>

  <!--
    PROPS DOWN, EVENTS UP (Component communication)

    Parent -> Child: pass data as props
    Child -> Parent: emit events (parent decides what to do)
  -->
  <NameCard :name="name" :disabled="count >= 5" @clear="clearName" />

  <!-- v-show: element stays in the DOM; only CSS display toggles -->
  <p v-show="visible">Now you see me (v-show)</p>

  <!-- v-if: element is created/removed from the DOM -->
  <p v-if="visible">Now you see me (v-if)</p>

  <!-- Event handler: toggles visibility -->
  <button @click="toggle">toggle</button>

  <!-- Event handler: increments a ref -->
  <button @click="increment">Increment</button>

  <!-- Render a list from an array: v-for + :key -->
  <ul>
    <li v-for="product in products" :key="product.id">
      {{ product.name }}
    </li>
  </ul>

  <!-- More reactive values -->
  <p>Price: {{ price }}</p>

  <!-- computed(): derived value recalculated when dependencies change -->
  <p>Total: {{ total }}</p>
</template>

<script setup>
// Import Composition API helpers
import { ref, computed, watch, onMounted } from 'vue'
import NameCard from './components/NameCard.vue'

// ref(): reactive primitive
const count = ref(0)

// ref(): reactive string used with v-model
const name = ref('')

// Parent handler for the child's emitted event
function clearName() {
  name.value = ''
}

// Method used by @click
function increment() {
  count.value++
}

// ref(): reactive value used by :src
const imageURL = ref('src/assets/vue.svg')

// ref(): boolean used by v-if / v-show
const visible = ref(true)

function toggle() {
  visible.value = !visible.value
}

// ref(): array of products fetched from a public API (demo)
const products = ref([])

// onMounted(): runs once when the component mounts
onMounted(async () => {
  // Demo fetch (this is NOT your Spring backend; it's just for learning)
  const res = await fetch('https://fakestoreapi.com/products')
  products.value = await res.json()
})

// Another ref used by computed
const price = ref(100)

// computed(): recalculates when price.value changes
const total = computed(() => price.value * 1.2)

// watch(): runs when `name` changes (like a side-effect hook)
watch(name, (newName, oldName) => {
  console.log(`Name changed from ${oldName} to ${newName}`)
})
</script>

<!--
Notes:
- v-if   → DOM creation / destruction
- v-show → CSS visibility toggle
-->
