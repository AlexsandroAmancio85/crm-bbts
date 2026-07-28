import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api'
});

// Interceptor automático para injetar o token que o seu AuthContext gera
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token'); // Ajuste aqui caso seu AuthContext salve com outro nome (ex: 'token_crm')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const analisarLeadComIa = async (leadData) => {
  const response = await api.post('/qualificacao-ia/analisar', leadData);
  return response.data;
};