<template>
  <h1>Products</h1>
  <p>Count: {{ count }}</p>
  <img :src="imageURL" />
  <input v-model="name" />
  <p>Your name is: {{ name }}</p>
  <p v-show="visible">Now you see me</p>
  <p v-if="visible">Now you see me</p>
  <button @click="toggle">toggle</button>
  <button @click="increment">
    Increment
  </button>
    <ul>
    <li v-for="product in products" :key="product.id">
      {{ product.name }}
    </li>
  </ul>

</template>

<script setup>
import { ref } from 'vue'
const count = ref(0)
const name = ref("")
function increment() {
  count.value++
}
const imageURL = ref("src/assets/vue.svg")
const visible = ref(true)
function toggle() {
  visible.value = !visible.value
}

const products = ref([
  { id: 1, name: 'Keyboard' },
  { id: 2, name: 'Mouse' },
  { id: 3, name: 'Monitor' }
])


</script>


<!-- v-if   → DOM creation / destruction
     v-show → CSS visibility
 -->