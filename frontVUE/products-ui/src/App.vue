<template>
  <div class="min-h-screen bg-zinc-950 text-zinc-100">
    <RouterView />
  </div>
</template>

<script setup>
// Root shell: renders the current route.
import { RouterView } from 'vue-router'
</script>