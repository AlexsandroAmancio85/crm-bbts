import { useState } from 'react'
import { Search } from 'lucide-react'
import Topbar from '../../components/layout/Topbar'
import DataTable from '../../components/common/DataTable'
import StatusBadge from '../../components/common/StatusBadge'
import { clientes, vendedores } from '../../mock/data'

export default function ClientesPage() {
  const [busca, setBusca] = useState('')

  const filtrados = clientes.filter((c) =>
    [c.nome, c.municipio, c.cultura].join(' ').toLowerCase().includes(busca.toLowerCase())
  )

  const columns = [
    { key: 'nome', header: 'Produtor' },
    { key: 'propriedade', header: 'Propriedade' },
    { key: 'cultura', header: 'Cultura' },
    { key: 'municipio', header: 'Município' },
    { key: 'telefone', header: 'Telefone', render: (r) => <span className="cell-mono">{r.telefone}</span> },
    {
      key: 'vendedorId',
      header: 'Vendedor',
      render: (r) => vendedores.find((v) => v.id === r.vendedorId)?.nome || <span className="muted">não atribuído</span>,
    },
    { key: 'status', header: 'Status', render: (r) => <StatusBadge status={r.status} /> },
  ]

  return (
    <>
      <Topbar eyebrow="Base completa" title="Clientes" />
      <div className="app-content">
        <div className="panel">
          <div className="panel-head">
            <div>
              <h2>Produtores cadastrados</h2>
              <p>Visão consolidada de toda a base, independentemente da etapa do fluxo.</p>
            </div>
          </div>
          <div className="field" style={{ maxWidth: 320, position: 'relative' }}>
            <input
              placeholder="Buscar por nome, cultura ou município…"
              value={busca}
              onChange={(e) => setBusca(e.target.value)}
              style={{ paddingLeft: 34 }}
            />
            <Search size={15} style={{ position: 'absolute', left: 11, top: 11 }} color="var(--ink-faint)" />
          </div>
          <DataTable columns={columns} rows={filtrados} />
        </div>
      </div>
    </>
  )
}
