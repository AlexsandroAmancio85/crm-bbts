// Espelha br.com.bbts.crm.qualificacao.* — Etapa 4: Qualificação Gerencial
import api from './axiosConfig'

const BASE = '/qualificacoes'

export const qualificacaoService = {
  listarPendentes: () => api.get(`${BASE}/pendentes`),
  qualificar: (clienteId, payload) => api.post(`${BASE}/${clienteId}`, payload),
  rejeitar: (clienteId, motivo) => api.post(`${BASE}/${clienteId}/rejeitar`, { motivo }),
}
