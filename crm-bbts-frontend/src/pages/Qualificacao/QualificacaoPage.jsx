import { useState } from 'react'
import { CheckCircle2, XCircle, Sparkles } from 'lucide-react'
import Topbar from '../../components/layout/Topbar'
import Trilha from '../../components/common/Trilha'
import { clientes as clientesIniciais } from '../../mock/data'
import AnaliseIaCard from '../../components/vendedor/AnaliseIaCard'
// import { qualificacaoService } from '../../api/qualificacaoService'

export default function QualificacaoPage() {
  const [clientes, setClientes] = useState(clientesIniciais)
  const [clienteParaAnalisar, setClienteParaAnalisar] = useState(null)
  
  const pendentes = clientes.filter((c) => !c.qualificado)

  function qualificar(id, aprovado) {
    setClientes((prev) =>
      prev.map((c) => (c.id === id ? { ...c, qualificado: aprovado, status: aprovado ? 'pendente' : 'indisponivel' } : c))
    )
    if (clienteParaAnalisar?.id === id) {
      setClienteParaAnalisar(null)
    }
    // Integração real:
    // aprovado ? qualificacaoService.qualificar(id, {}) : qualificacaoService.rejeitar(id, 'Fora do perfil')
  }

  return (
    <>
      <Topbar eyebrow="Etapa 04" title="Qualificação Gerencial" />
      <div className="app-content">
        <Trilha ativo="qualificacao" />

        <div className="panel">
          <div className="panel-head">
            <div>
              <h2>Produtores aguardando qualificação</h2>
              <p>A gerência analisa cada cliente importado antes da distribuição aos vendedores.</p>
            </div>
          </div>

          {pendentes.length === 0 ? (
            <div className="empty-state">
              <h3>Fila zerada</h3>
              <p className="muted">Todos os produtores importados já foram qualificados.</p>
            </div>
          ) : (
            pendentes.map((c) => (
              <div key={c.id} style={{ borderBottom: '1px solid var(--border-hairline)', padding: '14px 0' }}>
                <div className="flex-between">
                  <div>
                    <strong>{c.nome}</strong>
                    <div className="muted" style={{ fontSize: 'var(--fs-sm)' }}>
                      {c.propriedade} · {c.cultura} · {c.municipio}
                    </div>
                  </div>
                  <div style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
                    {/* Botão para abrir o painel da IA para este cliente específico */}
                    <button 
                      className="btn btn-ghost btn-sm" 
                      style={{ color: '#4F46E5', display: 'flex', alignItems: 'center', gap: 4 }}
                      onClick={() => setClienteParaAnalisar(clienteParaAnalisar?.id === c.id ? null : c)}
                    >
                      <Sparkles size={14} /> 
                      {clienteParaAnalisar?.id === c.id ? 'Fechar IA' : 'Consultar IA'}
                    </button>

                    <button className="btn btn-ghost btn-sm" onClick={() => qualificar(c.id, false)}>
                      <XCircle size={14} /> Rejeitar
                    </button>
                    <button className="btn btn-primary btn-sm" onClick={() => qualificar(c.id, true)}>
                      <CheckCircle2 size={14} /> Qualificar
                    </button>
                  </div>
                </div>

                {/* Renderiza o painel de análise da inteligência artificial logo abaixo do registro selecionado */}
                {clienteParaAnalisar?.id === c.id && (
                  <div style={{ marginTop: '10px', paddingLeft: '10px', borderLeft: '3px solid #4F46E5' }}>
                    <AnaliseIaCard cliente={c} />
                  </div>
                )}
              </div>
            ))
          )}
        </div>
      </div>
    </>
  )
}