import api from './axiosConfig'
const BASE = '/vendedores'
export const vendedorService = {
  listar: () => api.get(BASE),
  carteira: (id) => api.get(`${BASE}/${id}/carteira`),
  distribuir: (payload) => api.post(`${BASE}/distribuicao`, payload),
}
