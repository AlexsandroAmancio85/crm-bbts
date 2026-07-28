import api from './axiosConfig'
const BASE = '/clientes'
export const clienteService = {
  listar: (filtro = {}) => api.get(BASE, { params: filtro }),
  obter: (id) => api.get(`${BASE}/${id}`),
  criar: (dto) => api.post(BASE, dto),
  atualizar: (id, dto) => api.put(`${BASE}/${id}`, dto),
  remover: (id) => api.delete(`${BASE}/${id}`),
  atualizarStatus: (id, status) => api.patch(`${BASE}/${id}/status`, { status }),
  /** Fluxo do vendedor — itens 4.1, 4.2, 4.3: requer senha confirmada no front antes de chamar */
  atualizarAtendimento: (id, payload) => api.patch(`${BASE}/${id}/atendimento`, payload),
}
