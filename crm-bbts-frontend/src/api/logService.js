import api from './axiosConfig'
export const logService = {
  produtividade: {
    porVendedor: (vid) => api.get(`/logs/produtividade/vendedor/${vid}`),
    resumo: (vid) => api.get(`/logs/produtividade/vendedor/${vid}/resumo`),
    porPeriodo: (vid, ini, fim) => api.get(`/logs/produtividade/vendedor/${vid}/periodo`, { params: { inicio: ini, fim } }),
  },
  sistema: {
    recentes: () => api.get('/logs/sistema'),
  },
}
