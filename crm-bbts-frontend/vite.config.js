import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// Proxy /api calls to the Spring Boot backend (crm-bbts) during local dev.
// Adjust the target if the back-end runs on a different port/profile.
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
