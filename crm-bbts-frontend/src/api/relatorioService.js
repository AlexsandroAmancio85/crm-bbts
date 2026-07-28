// Espelha br.com.bbts.crm.relatorio.* — Etapa 8: Relatórios e Indicadores
import api from './axiosConfig'

const BASE = '/relatorios'

export const relatorioService = {
  indicadoresMensais: (params) => api.get(`${BASE}/indicadores-mensais`, { params }),
  exportarCsv: (params) => api.get(`${BASE}/exportar`, { params, responseType: 'blob' }),
}
