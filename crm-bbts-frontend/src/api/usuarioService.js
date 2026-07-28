import api from './axiosConfig'
const BASE = '/usuarios'
export const usuarioService = {
  listar: () => api.get(BASE),
  criar: (dto) => api.post(BASE, dto),
  desativar: (id) => api.patch(`${BASE}/${id}/desativar`),
  verificarSenha: (senha) => api.post(`${BASE}/verificar-senha`, { senha }),
  alterarSenha: (dto) => api.post(`${BASE}/alterar-senha`, dto),
}
