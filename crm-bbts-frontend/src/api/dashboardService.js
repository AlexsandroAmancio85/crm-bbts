// Espelha br.com.bbts.crm.dashboard.* — visão consolidada da trilha (etapas 1-8)
import api from './axiosConfig'

const BASE = '/dashboard'

export const dashboardService = {
  resumoTrilha: () => api.get(`${BASE}/trilha`),
  kpis: () => api.get(`${BASE}/kpis`),
}
