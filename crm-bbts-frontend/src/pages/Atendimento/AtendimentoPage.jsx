import { useState } from 'react'
import Topbar from '../../components/layout/Topbar'
import Trilha from '../../components/common/Trilha'
import { clientes as clientesIniciais, vendedores } from '../../mock/data'
// import { clienteService } from '../../api/clienteService'

const colunas = [
  { status: 'contatado', titulo: 'Contatado', className: 'contatado' },
  { status: 'vendido', titulo: 'Vendido', className: 'vendido' },
  { status: 'indisponivel', titulo: 'Indisponível', className: 'indisponivel' },
]

export default function AtendimentoPage() {
  const [clientes, setClientes] = useState(clientesIniciais.filter((c) => c.vendedorId))

  function mudarStatus(id, status) {
    setClientes((prev) => prev.map((c) => (c.id === id ? { ...c, status } : c)))
    // Integração real: clienteService.atualizarStatus(id, status)
  }

  function nomeVendedor(id) {
    return vendedores.find((v) => v.id === id)?.nome || '—'
  }

  return (
    <>
      <Topbar eyebrow="Etapa 06" title="Atendimento do Vendedor" />
      <div className="app-content">
        <Trilha ativo="atendimento" />

        <p>
          Cada vendedor registra o resultado do contato com o produtor. Use os botões do card para
          mover o cliente entre os três desfechos do fluxo.
        </p>

        <div className="kanban">
          {colunas.map((col) => {
            const itens = clientes.filter((c) => c.status === col.status)
            return (
              <div className="kanban-col" key={col.status}>
                <div className="kanban-col-head">
                  <span className={`badge ${col.className}`}>{col.titulo}</span>
                  <span className="cell-mono">{itens.length}</span>
                </div>
                {itens.length === 0 && <p className="muted" style={{ fontSize: 'var(--fs-sm)' }}>Sem registros.</p>}
                {itens.map((c) => (
                  <div className="kanban-card" key={c.id}>
                    <div className="name">{c.nome}</div>
                    <div className="meta">{nomeVendedor(c.vendedorId)} · {c.municipio}</div>
                    <div style={{ display: 'flex', gap: 6, marginTop: 8, flexWrap: 'wrap' }}>
                      {colunas
                        .filter((o) => o.status !== c.status)
                        .map((o) => (
                          <button
                            key={o.status}
                            className="btn btn-ghost btn-sm"
                            onClick={() => mudarStatus(c.id, o.status)}
                          >
                            → {o.titulo}
                          </button>
                        ))}
                    </div>
                  </div>
                ))}
              </div>
            )
          })}
        </div>
      </div>
    </>
  )
}
