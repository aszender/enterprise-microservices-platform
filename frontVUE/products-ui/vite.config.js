import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  // Dev-only: proxy API calls to Spring Boot services
  server: {
    proxy: {
      // Products service (8080)
      '/api/products': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
      '/api/auth': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
      // Orders service (8081)
      '/api/orders': {
        target: 'http://localhost:8081',
        changeOrigin: true,
      },
      // Inventory service (8082)
      '/api/inventory': {
        target: 'http://localhost:8082',
        changeOrigin: true,
      },
    },
  },
})
