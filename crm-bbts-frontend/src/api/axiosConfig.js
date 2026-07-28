import axios from 'axios'

// Em desenvolvimento o Vite faz proxy de /api para http://localhost:8080
// (ver vite.config.js). Em produção, defina VITE_API_BASE_URL no .env.
const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  headers: { 'Content-Type': 'application/json' },
})

// Anexa o JWT (gerado por JwtService no back-end) em toda requisição, se existir.
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('crm-bbts:token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('crm-bbts:token')
    }
    return Promise.reject(error)
  }
)

export default api
