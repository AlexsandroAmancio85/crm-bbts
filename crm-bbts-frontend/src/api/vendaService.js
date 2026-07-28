// Espelha br.com.bbts.crm.venda.* — Etapa 6 (resultado "Vendido")
import api from './axiosConfig'

const BASE = '/vendas'

export const vendaService = {
  listar: () => api.get(BASE),
  registrar: (payload) => api.post(BASE, payload),
}
