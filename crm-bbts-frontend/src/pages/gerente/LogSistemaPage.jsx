import { useEffect, useState } from 'react'
import Topbar from '../../components/layout/Topbar'
import { logService } from '../../api/logService'
import { vendedorService } from '../../api/vendedorService'

export default function LogSistemaPage() {
  const [logs, setLogs] = useState([])
  const [produtividade, setProdutividade] = useState([])
  const [vendedores, setVendedores] = useState([])
  const [vendedorSel, setVendedorSel] = useState('')
  const [aba, setAba] = useState('sistema')
  const [carregando, setCarregando] = useState(true)

  useEffect(() => {
    Promise.all([
      logService.sistema.recentes(),
      vendedorService.listar(),
    ]).then(([l, v]) => {
      setLogs(l.data)
      setVendedores(v.data)
      setCarregando(false)
    })
  }, [])

  useEffect(() => {
    if (aba !== 'produtividade' || !vendedorSel) return
    logService.produtividade.porVendedor(vendedorSel).then(r => setProdutividade(r.data))
  }, [aba, vendedorSel])

  const TIPO_BADGE = {
    ABERTURA:    'pendente', VENDIDO: 'vendido',
    CONTATADO:   'contatado', INDISPONIVEL: 'indisponivel', AGENDAMENTO: 'pendente',
  }

  return (
    <>
      <Topbar eyebrow="Auditoria" title="Logs do sistema" />
      <div className="app-content">
        {/* Abas */}
        <div style={{ display: 'flex', gap: 0, marginBottom: 20, borderBottom: '2px solid var(--border-hairline)' }}>
          {[['sistema', 'Log do sistema'], ['produtividade', 'Produtividade por vendedor']].map(([k, l]) => (
            <button key={k} onClick={() => setAba(k)} className="btn btn-ghost"
              style={{ borderRadius: 0, borderBottom: aba === k ? '2px solid var(--amber-600)' : '2px solid transparent', marginBottom: -2 }}>
              {l}
            </button>
          ))}
        </div>

        {aba === 'sistema' && (
          <div className="panel">
            <h2>Últimas 100 operações auditadas</h2>
            <p>Toda alteração confirmada por senha fica registrada aqui (item 7.2).</p>
            {carregando ? <p className="muted">Carregando…</p> : (
              <div className="table-wrap">
                <table className="data-table">
                  <thead><tr><th>Data/Hora</th><th>Usuário</th><th>Ação</th><th>Entidade</th><th>ID</th><th>Detalhes</th><th>IP</th></tr></thead>
                  <tbody>
                    {logs.map(l => (
                      <tr key={l.id}>
                        <td><span className="cell-mono">{new Date(l.dataHora).toLocaleString('pt-BR')}</span></td>
                        <td>{l.usuarioNome}</td>
                        <td><span className="badge pendente">{l.acao}</span></td>
                        <td><span className="cell-mono">{l.entidade}</span></td>
                        <td><span className="cell-mono">{l.entidadeId || '—'}</span></td>
                        <td style={{ fontSize: 'var(--fs-xs)', color: 'var(--ink-soft)', maxWidth: 260 }}>{l.detalhes || '—'}</td>
                        <td><span className="cell-mono">{l.ip || '—'}</span></td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        )}

        {aba === 'produtividade' && (
          <div className="panel">
            <div className="panel-head">
              <h2>Produtividade do vendedor</h2>
              <select value={vendedorSel} onChange={e => setVendedorSel(e.target.value)}
                style={{ border: '1px solid var(--border-strong)', borderRadius: 4, padding: '7px 11px', fontSize: 'var(--fs-sm)', minWidth: 200 }}>
                <option value="">Selecione um vendedor…</option>
                {vendedores.map(v => <option key={v.id} value={v.id}>{v.nome}</option>)}
              </select>
            </div>
            {!vendedorSel ? <p className="muted">Selecione um vendedor para ver o log de produtividade.</p> : (
              <div className="table-wrap">
                <table className="data-table">
                  <thead><tr><th>Data/Hora</th><th>Tipo</th><th>Cliente</th><th>Observação</th></tr></thead>
                  <tbody>
                    {produtividade.map(l => (
                      <tr key={l.id}>
                        <td><span className="cell-mono">{new Date(l.dataHora).toLocaleString('pt-BR')}</span></td>
                        <td><span className={`badge ${TIPO_BADGE[l.tipoAtividade] || 'pendente'}`}>{l.tipoAtividade}</span></td>
                        <td>{l.clienteNome}</td>
                        <td style={{ fontSize: 'var(--fs-xs)', color: 'var(--ink-soft)' }}>{l.observacao || '—'}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        )}
      </div>
    </>
  )
}
