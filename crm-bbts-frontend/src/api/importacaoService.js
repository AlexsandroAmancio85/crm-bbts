// Espelha br.com.bbts.crm.importacao.controller.ImportacaoController
// Etapas 1-3 do fluxo: Recebimento da Base -> Importação e Leitura -> Validação da Base
import api from './axiosConfig'

const BASE = '/importacoes'

export const importacaoService = {
  listar: () => api.get(BASE),
  upload: (arquivo, onUploadProgress) => {
    const formData = new FormData()
    formData.append('arquivo', arquivo)
    return api.post(`${BASE}/upload`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
      onUploadProgress,
    })
  },
  validar: (importacaoId) => api.post(`${BASE}/${importacaoId}/validar`),
  obterLog: (importacaoId) => api.get(`${BASE}/${importacaoId}/log`),
}
