import Topbar from '../../components/layout/Topbar'
import Trilha from '../../components/common/Trilha'
import DataTable from '../../components/common/DataTable'
import StatusBadge from '../../components/common/StatusBadge'
import { clientes, vendedores } from '../../mock/data'

export default function MonitoramentoPage() {
  const linhas = vendedores.map((v) => {
    const carteira = clientes.filter((c) => c.vendedorId === v.id)
    return {
      ...v,
      total: carteira.length,
      vendido: carteira.filter((c) => c.status === 'vendido').length,
      contatado: carteira.filter((c) => c.status === 'contatado').length,
      indisponivel: carteira.filter((c) => c.status === 'indisponivel').length,
    }
  })

  const columns = [
    { key: 'nome', header: 'Vendedor' },
    { key: 'regiao', header: 'Região' },
    { key: 'total', header: 'Carteira', render: (r) => <span className="cell-mono">{r.total}</span> },
    { key: 'vendido', header: '', render: (r) => <span style={{ display: 'flex', gap: 6, alignItems: 'center' }}><StatusBadge status="vendido" /> {r.vendido}</span> },
    { key: 'contatado', header: '', render: (r) => <span style={{ display: 'flex', gap: 6, alignItems: 'center' }}><StatusBadge status="contatado" /> {r.contatado}</span> },
    { key: 'indisponivel', header: '', render: (r) => <span style={{ display: 'flex', gap: 6, alignItems: 'center' }}><StatusBadge status="indisponivel" /> {r.indisponivel}</span> },
  ]

  return (
    <>
      <Topbar eyebrow="Etapa 07" title="Monitoramento Gerencial" />
      <div className="app-content">
        <Trilha ativo="monitoramento" />

        <div className="panel">
          <div className="panel-head">
            <div>
              <h2>Desempenho por vendedor</h2>
              <p>Acompanhamento em tempo real dos desfechos de atendimento por carteira.</p>
            </div>
          </div>
          <DataTable columns={columns} rows={linhas} rowKey="id" />
        </div>
      </div>
    </>
  )
}
