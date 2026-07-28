// Espelha br.com.bbts.crm.usuario.controller.AuthController
import api from './axiosConfig'

const TOKEN_KEY = 'crm-bbts:token'
const USER_KEY = 'crm-bbts:user'

export const authService = {
  login: async (username, senha) => {
    const { data } = await api.post('/auth/login', { username, senha })
    localStorage.setItem(TOKEN_KEY, data.token)
    localStorage.setItem(USER_KEY, JSON.stringify({ nome: data.nome, username: data.username, perfil: data.perfil }))
    return data
  },
  logout: () => {
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(USER_KEY)
  },
  usuarioAtual: () => {
    const raw = localStorage.getItem(USER_KEY)
    return raw ? JSON.parse(raw) : null
  },
  autenticado: () => Boolean(localStorage.getItem(TOKEN_KEY)),
}
