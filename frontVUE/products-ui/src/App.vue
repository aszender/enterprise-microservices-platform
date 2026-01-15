<template>
  <div class="min-h-screen bg-zinc-950 text-zinc-100">
    <!-- Navigation -->
    <nav class="sticky top-0 z-50 border-b border-white/10 bg-zinc-950/80 backdrop-blur-xl">
      <div class="mx-auto flex max-w-5xl items-center justify-between px-4 py-4">
        <RouterLink to="/" class="text-lg font-bold tracking-tight">
          Products Vue
        </RouterLink>
        <div class="flex items-center gap-1">
          <RouterLink
            to="/"
            class="rounded-lg px-4 py-2 text-sm font-medium transition-all"
            :class="$route.path === '/' ? 'bg-white/10 text-white' : 'text-white/70 hover:bg-white/5 hover:text-white'"
          >
            Products
          </RouterLink>
          <RouterLink
            to="/orders"
            class="rounded-lg px-4 py-2 text-sm font-medium transition-all"
            :class="$route.path === '/orders' ? 'bg-white/10 text-white' : 'text-white/70 hover:bg-white/5 hover:text-white'"
          >
            Orders
          </RouterLink>
          <RouterLink
            to="/inventory"
            class="rounded-lg px-4 py-2 text-sm font-medium transition-all"
            :class="$route.path === '/inventory' ? 'bg-white/10 text-white' : 'text-white/70 hover:bg-white/5 hover:text-white'"
          >
            Inventory
          </RouterLink>
        </div>
      </div>
    </nav>
    <RouterView />
  </div>
</template>

<script setup>
// Root shell: renders the current route with navigation.
import { RouterView, RouterLink } from 'vue-router'
</script>