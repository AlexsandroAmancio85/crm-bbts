// Espelha br.com.bbts.crm.agenda.* — RetornoAgendado
import api from './axiosConfig'

const BASE = '/agenda'

export const agendaService = {
  listar: (vendedorId) => api.get(BASE, { params: { vendedorId } }),
  agendar: (payload) => api.post(BASE, payload),
  concluir: (id) => api.patch(`${BASE}/${id}/concluir`),
}
