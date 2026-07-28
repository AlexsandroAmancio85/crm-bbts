import { useState } from 'react'
import { UserCheck } from 'lucide-react'
import Topbar from '../../components/layout/Topbar'
import Trilha from '../../components/common/Trilha'
import { clientes as clientesIniciais, vendedores } from '../../mock/data'
// import { vendedorService } from '../../api/vendedorService'

export default function DistribuicaoPage() {
  const [clientes, setClientes] = useState(clientesIniciais)
  const semVendedor = clientes.filter((c) => c.qualificado && !c.vendedorId)

  function distribuir(clienteId, vendedorId) {
    setClientes((prev) =>
      prev.map((c) => (c.id === clienteId ? { ...c, vendedorId: Number(vendedorId), status: 'pendente' } : c))
    )
    // Integração real: vendedorService.distribuir({ clienteId, vendedorId })
  }

  return (
    <>
      <Topbar eyebrow="Etapa 05" title="Distribuição aos Vendedores" />
      <div className="app-content">
        <Trilha ativo="distribuicao" />

        <div className="kpi-grid">
          {vendedores.map((v) => (
            <div className="kpi" key={v.id}>
              <div className="kpi-label">{v.regiao}</div>
              <div className="kpi-value" style={{ fontSize: 'var(--fs-lg)' }}>{v.nome}</div>
              <div className="kpi-delta">{v.carteiraAtual} clientes na carteira</div>
            </div>
          ))}
        </div>

        <div className="panel">
          <div className="panel-head">
            <div>
              <h2>Produtores qualificados aguardando vendedor</h2>
              <p>Distribua a carteira por região ou carga de trabalho atual.</p>
            </div>
          </div>

          {semVendedor.length === 0 ? (
            <div className="empty-state">
              <h3>Tudo distribuído</h3>
              <p className="muted">Não há produtores qualificados sem vendedor responsável.</p>
            </div>
          ) : (
            semVendedor.map((c) => (
              <div key={c.id} className="flex-between" style={{ padding: '14px 0', borderBottom: '1px solid var(--border-hairline)' }}>
                <div>
                  <strong>{c.nome}</strong>
                  <div className="muted" style={{ fontSize: 'var(--fs-sm)' }}>{c.municipio} · {c.cultura}</div>
                </div>
                <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                  <select
                    className="field"
                    style={{ marginBottom: 0, minWidth: 200 }}
                    defaultValue=""
                    onChange={(e) => e.target.value && distribuir(c.id, e.target.value)}
                  >
                    <option value="" disabled>Selecionar vendedor…</option>
                    {vendedores.map((v) => (
                      <option key={v.id} value={v.id}>{v.nome} — {v.regiao}</option>
                    ))}
                  </select>
                  <UserCheck size={16} color="var(--canopy-500)" />
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </>
  )
}
