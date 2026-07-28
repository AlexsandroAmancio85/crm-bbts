import React, { useState } from 'react';
import { analisarLeadComIa } from '../../services/api'; // Certifique-se de que tem exatamente dois "../"

export default function AnaliseIaCard({ cliente }) {
  const [loading, setLoading] = useState(false);
  const [resultado, setResultado] = useState(null);
  const [erro, setErro] = useState(null);

  const handleAnalise = async () => {
    setLoading(true);
    setErro(null);
    try {
      // Mapeia as propriedades do seu objeto 'cliente' para o que o seu DTO do Java espera
      const dados = await analisarLeadComIa({
        nome: cliente.nome || cliente.razaoSocial,
        empresa: cliente.empresa || cliente.nomeFantasia || 'Não informada',
        faturamentoEstimado: cliente.faturamentoEstimado || 0,
        quantidadeFuncionarios: cliente.quantidadeFuncionarios || 0,
        segmento: cliente.segmento || 'Não informado',
        historicoContato: cliente.historicoContato || cliente.observacoes || 'Sem histórico registrado.'
      });
      setResultado(dados);
    } catch (err) {
      setErro('Falha ao obter análise da IA. Verifique se o backend está rodando.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const getBadgeColor = (prioridade) => {
    switch (prioridade?.toUpperCase()) {
      case 'ALTA': return { bg: '#FEE2E2', text: '#991B1B', border: '#FCA5A5' };
      case 'MEDIA': return { bg: '#FEF3C7', text: '#92400E', border: '#FCD34D' };
      case 'BAIXA': return { bg: '#D1FAE5', text: '#065F46', border: '#6EE7B7' };
      default: return { bg: '#F3F4F6', text: '#374151', border: '#D1D5DB' };
    }
  };

  const cores = getBadgeColor(resultado?.prioridade);

  return (
    <div style={{ padding: '24px', backgroundColor: '#fff', borderRadius: '12px', boxShadow: '0 4px 6px -1px rgba(0,0,0,0.1)', border: '1px solid #E5E7EB', marginTop: '20px' }}>
      {/* 🛠️ LINHA CORRIGIDA AQUI: Removido o justifyContent duplicado */}
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '16px' }}>
        <h3 style={{ margin: 0, fontSize: '18px', fontWeight: 'bold', color: '#1F2937' }}>
          ✨ Análise Inteligente (Gemini)
        </h3>
        
        {!resultado && (
          <button
            onClick={handleAnalise}
            disabled={loading}
            style={{ padding: '8px 16px', backgroundColor: loading ? '#93C5FD' : '#2563EB', color: '#fff', border: 'none', borderRadius: '8px', cursor: loading ? 'not-allowed' : 'pointer', fontWeight: '500' }}
          >
            {loading ? 'Processando...' : 'Analisar Perfil'}
          </button>
        )}
      </div>

      {loading && (
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '16px 0', color: '#6B7280' }}>
          <p style={{ margin: 0 }}>O Gemini está cruzando os dados e histórico comercial...</p>
        </div>
      )}

      {erro && (
        <div style={{ padding: '12px', backgroundColor: '#FEE2E2', color: '#B91C1C', borderRadius: '8px', fontSize: '14px', marginBottom: '16px' }}>
          {erro}
        </div>
      )}

      {resultado && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <span style={{ fontSize: '14px', fontWeight: '600', color: '#4B5563' }}>Prioridade:</span>
            <span style={{ padding: '4px 10px', borderRadius: '9999px', fontSize: '12px', fontWeight: 'bold', backgroundColor: cores.bg, color: cores.text, border: `1px solid ${cores.border}` }}>
              {resultado.prioridade}
            </span>
          </div>

          <div>
            <h4 style={{ margin: '0 0 4px 0', fontSize: '14px', fontWeight: '600', color: '#374151' }}>Justificativa Comercial:</h4>
            <p style={{ margin: 0, fontSize: '14px', color: '#4B5563', backgroundColor: '#F9FAFB', padding: '12px', borderRadius: '8px', border: '1px solid #F3F4F6', lineHeight: '1.5' }}>
              {resultado.justificativa}
            </p>
          </div>

          <div>
            <h4 style={{ margin: '0 0 4px 0', fontSize: '14px', fontWeight: '600', color: '#374151' }}>Próximo Passo Recomendado:</h4>
            <p style={{ margin: 0, fontSize: '14px', color: '#1E3A8A', backgroundColor: '#EFF6FF', padding: '12px', borderRadius: '8px', border: '1px solid #DBEAFE', fontWeight: '500' }}>
              💡 {resultado.proximoPassoRecomendado}
            </p>
          </div>

          <button
            onClick={() => setResultado(null)}
            style={{ background: 'none', border: 'none', color: '#9CA3AF', textDecoration: 'underline', cursor: 'pointer', fontSize: '12px', alignSelf: 'flex-start', padding: 0 }}
          >
            Refazer análise
          </button>
        </div>
      )}
    </div>
  );
}