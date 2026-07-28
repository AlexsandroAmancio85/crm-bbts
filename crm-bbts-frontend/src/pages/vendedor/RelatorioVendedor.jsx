import { useEffect, useState } from 'react'
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid } from 'recharts'
import { Download } from 'lucide-react'
import Topbar from '../../components/layout/Topbar'
import { logService } from '../../api/logService'
import { vendedorService } from '../../api/vendedorService'
import { useAuth } from '../../context/AuthContext'

const TIPO_META = {
  ABERTURA:    { label: 'Fichas abertas', cor: '#3C6140' },
  VENDIDO:     { label: 'Vendas',         cor: '#2F7D45' },
  CONTATADO:   { label: 'Contatados',     cor: '#B9791E' },
  INDISPONIVEL:{ label: 'Indisponíveis',  cor: '#B23A2E' },
  AGENDAMENTO: { label: 'Agendamentos',   cor: '#5C6B7A' },
}

export default function RelatorioVendedor() {
  const { usuario } = useAuth()
  const [logs, setLogs] = useState([])
  const [resumo, setResumo] = useState([])
  const [carregando, setCarregando] = useState(true)

  // Precisamos do vendedorId — no token JWT o front guarda 'perfil' mas não vendedorId.
  // Solução: listar todos os clientes (filtro de vendedor já é aplicado no backend) e pegar id do primeiro.
  // Ou: buscar via GET /api/vendedores e casar pelo username. Aqui usamos o endpoint direto.
  const [vendedorId, setVendedorId] = useState(null)

  useEffect(() => {
    vendedorService.listar().then(r => {
      // O backend já filtra pelo usuário autenticado — no perfil VENDEDOR retorna só o dele
      const v = r.data.find(v => v.nome === usuario?.nome) || r.data[0]
      if (!v) { setCarregando(false); return }
      setVendedorId(v.id)
      Promise.all([
        logService.produtividade.porVendedor(v.id),
        logService.produtividade.resumo(v.id),
      ]).then(([logsR, resumoR]) => {
        setLogs(logsR.data)
        setResumo(resumoR.data)
        setCarregando(false)
      })
    }).catch(() => setCarregando(false))
  }, [usuario])

  const chartData = resumo.map(r => ({
    name: TIPO_META[r.tipoAtividade]?.label || r.tipoAtividade,
    quantidade: r.quantidade,
    fill: TIPO_META[r.tipoAtividade]?.cor || '#888',
  }))

  function exportarCsv() {
    const lines = ['data_hora,tipo,observacao']
    logs.forEach(l => lines.push(`${l.dataHora},${l.tipoAtividade},"${l.observacao || ''}"`) )
    const blob = new Blob([lines.join('\n')], { type: 'text/csv' })
    const a = document.createElement('a'); a.href = URL.createObjectURL(blob)
    a.download = `produtividade-${new Date().toISOString().slice(0,10)}.csv`; a.click()
  }

  return (
    <>
      <Topbar eyebrow="Minha Produtividade" title="Relatório de atividades" />
      <div className="app-content">
        {carregando ? <p className="muted">Carregando…</p> : <>
          <div className="kpi-grid" style={{ marginBottom: 20 }}>
            {resumo.map(r => (
              <div className="kpi" key={r.tipoAtividade}>
                <div className="kpi-label">{TIPO_META[r.tipoAtividade]?.label || r.tipoAtividade}</div>
                <div className="kpi-value">{r.quantidade}</div>
              </div>
            ))}
          </div>

          <div className="panel">
            <div className="panel-head">
              <h2>Atividades por tipo</h2>
              <button className="btn btn-ghost btn-sm" onClick={exportarCsv}><Download size={14}/> Exportar CSV</button>
            </div>
            <ResponsiveContainer width="100%" height={240}>
              <BarChart data={chartData}>
                <CartesianGrid stroke="#DCD3BD" strokeDasharray="3 3" />
                <XAxis dataKey="name" fontSize={12} />
                <YAxis fontSize={12} />
                <Tooltip />
                <Bar dataKey="quantidade" radius={[3,3,0,0]}>
                  {chartData.map((entry, i) => (
                    <rect key={i} fill={entry.fill} />
                  ))}
                </Bar>
              </BarChart>
            </ResponsiveContainer>
          </div>

          <div className="panel">
            <h2>Histórico de atividades</h2>
            <div className="table-wrap">
              <table className="data-table">
                <thead><tr><th>Data/Hora</th><th>Tipo</th><th>Cliente</th><th>Observação</th></tr></thead>
                <tbody>
                  {logs.slice(0, 50).map(l => (
                    <tr key={l.id}>
                      <td><span className="cell-mono">{new Date(l.dataHora).toLocaleString('pt-BR')}</span></td>
                      <td><span className={`badge ${l.tipoAtividade === 'VENDIDO' ? 'vendido' : l.tipoAtividade === 'INDISPONIVEL' ? 'indisponivel' : l.tipoAtividade === 'CONTATADO' ? 'contatado' : 'pendente'}`}>{TIPO_META[l.tipoAtividade]?.label || l.tipoAtividade}</span></td>
                      <td>{l.clienteNome}</td>
                      <td style={{ color: 'var(--ink-soft)', fontSize: 'var(--fs-xs)' }}>{l.observacao || '—'}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </>}
      </div>
    </>
  )
}
