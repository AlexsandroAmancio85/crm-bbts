import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Search, ChevronRight, Clock, CheckCircle2, XCircle, PhoneCall } from 'lucide-react'
import Topbar from '../../components/layout/Topbar'
import { clienteService } from '../../api/clienteService'
import { useAuth } from '../../context/AuthContext'

const STATUS_CONFIG = {
  PENDENTE:      { label: 'Pendente',     className: 'pendente',     icon: <Clock size={12}/> },
  CONTATADO:     { label: 'Contatado',    className: 'contatado',    icon: <PhoneCall size={12}/> },
  VENDIDO:       { label: 'Vendido',      className: 'vendido',      icon: <CheckCircle2 size={12}/> },
  INDISPONIVEL:  { label: 'Indisponível', className: 'indisponivel', icon: <XCircle size={12}/> },
}

export default function CarteiraDashboard() {
  const { usuario } = useAuth()
  const navigate = useNavigate()
  const [clientes, setClientes] = useState([])
  const [busca, setBusca] = useState('')
  const [filtroStatus, setFiltroStatus] = useState('')
  const [carregando, setCarregando] = useState(true)

  useEffect(() => {
    clienteService.listar({}).then(r => {
      setClientes(r.data)
      setCarregando(false)
    }).catch(() => setCarregando(false))
  }, [])

  const filtrados = clientes.filter(c => {
    const textoOk = !busca || [c.nome, c.municipio, c.cultura, c.cpf].join(' ').toLowerCase().includes(busca.toLowerCase())
    const statusOk = !filtroStatus || c.status === filtroStatus
    return textoOk && statusOk
  })

  const contagens = Object.fromEntries(
    Object.keys(STATUS_CONFIG).map(s => [s, clientes.filter(c => c.status === s).length])
  )

  return (
    <>
      <Topbar eyebrow="Minha Carteira" title={`Olá, ${usuario?.nome?.split(' ')[0]} 👋`} />
      <div className="app-content">
        <div className="kpi-grid" style={{ marginBottom: 20 }}>
          {Object.entries(STATUS_CONFIG).map(([status, meta]) => (
            <button key={status} onClick={() => setFiltroStatus(filtroStatus === status ? '' : status)}
              className="kpi" style={{ cursor: 'pointer', border: filtroStatus === status ? '2px solid var(--amber-600)' : undefined, textAlign: 'left' }}>
              <div className="kpi-label">{meta.label}</div>
              <div className="kpi-value">{contagens[status] || 0}</div>
            </button>
          ))}
        </div>

        <div className="panel">
          <div className="panel-head">
            <div>
              <h2>Carteira de clientes</h2>
              <p>Clique em um cliente para abrir a ficha e registrar o atendimento.</p>
            </div>
            <div style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
              <div style={{ position: 'relative' }}>
                <input placeholder="Buscar…" value={busca} onChange={e => setBusca(e.target.value)}
                  style={{ paddingLeft: 32, width: 200 }} />
                <Search size={14} style={{ position: 'absolute', left: 10, top: 11 }} color="var(--ink-faint)" />
              </div>
            </div>
          </div>

          {carregando ? <p className="muted">Carregando…</p> : filtrados.length === 0 ? (
            <div className="empty-state"><h3>Nenhum cliente encontrado</h3><p className="muted">Tente ajustar o filtro ou busca.</p></div>
          ) : (
            <div className="table-wrap">
              <table className="data-table">
                <thead><tr>
                  <th>Produtor</th><th>CPF</th><th>Município</th><th>Cultura</th><th>Telefone</th><th>Status</th><th></th>
                </tr></thead>
                <tbody>
                  {filtrados.map(c => {
                    const meta = STATUS_CONFIG[c.status] || STATUS_CONFIG.PENDENTE
                    return (
                      <tr key={c.id} onClick={() => navigate(`/vendedor/cliente/${c.id}`)} style={{ cursor: 'pointer' }}>
                        <td><strong>{c.nome}</strong></td>
                        <td><span className="cell-mono">{c.cpf || '—'}</span></td>
                        <td>{c.municipio || '—'}</td>
                        <td>{c.cultura || '—'}</td>
                        <td><span className="cell-mono">{c.telefone || '—'}</span></td>
                        <td><span className={`badge ${meta.className}`}>{meta.icon} {meta.label}</span></td>
                        <td><ChevronRight size={16} color="var(--ink-faint)" /></td>
                      </tr>
                    )
                  })}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </>
  )
}
