import api from './axiosConfig'
export const cpfService = {
  validar: (cpf) => api.get(`/integracao/cpf/${cpf.replace(/\D/g,'')}`),
  consultarMargem: (cpf) => api.get(`/integracao/dataprev/margem/${cpf.replace(/\D/g,'')}`),
}
