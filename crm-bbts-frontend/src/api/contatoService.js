// Espelha br.com.bbts.crm.contato.* — registro de contatos do cliente
import api from './axiosConfig'

const BASE = '/contatos'

export const contatoService = {
  listarPorCliente: (clienteId) => api.get(`${BASE}/cliente/${clienteId}`),
  registrar: (payload) => api.post(BASE, payload),
}
