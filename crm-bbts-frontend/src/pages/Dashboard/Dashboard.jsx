import { LineChart, Line, XAxis, YAxis, Tooltip, CartesianGrid, ResponsiveContainer, Legend } from 'recharts'
import Topbar from '../../components/layout/Topbar'
import Trilha from '../../components/common/Trilha'
import Kpi from '../../components/common/Kpi'
import { clientes, indicadoresMensais } from '../../mock/data'

export default function Dashboard() {
  const contagens = {
    recebimento: 320,
    importacao: 298,
    validacao: 298,
    qualificacao: clientes.filter((c) => c.qualificado).length,
    distribuicao: clientes.filter((c) => c.vendedorId).length,
    atendimento: clientes.filter((c) => c.status !== 'pendente').length,
    monitoramento: clientes.filter((c) => c.status !== 'pendente').length,
    relatorios: indicadoresMensais.reduce((acc, m) => acc + m.vendidos, 0),
  }

  const vendidos = clientes.filter((c) => c.status === 'vendido').length
  const taxaConversao = ((vendidos / clientes.length) * 100).toFixed(0)

  return (
    <>
      <Topbar eyebrow="Visão geral" title="Trilha da Safra" />
      <div className="app-content">
        <p>
          Panorama consolidado das oito etapas do fluxo comercial, da chegada da base de produtores
          até os relatórios finais — atualizado em tempo real assim que o back-end crm-bbts estiver no ar.
        </p>

        <Trilha contagens={contagens} />

        <div className="kpi-grid">
          <Kpi label="Produtores na base" value={clientes.length} />
          <Kpi label="Qualificados" value={contagens.qualificacao} />
          <Kpi label="Vendidos no mês" value={vendidos} delta="+4 vs. mês anterior" direction="up" />
          <Kpi label="Taxa de conversão" value={`${taxaConversao}%`} />
        </div>

        <div className="panel">
          <div className="panel-head">
            <div>
              <h2>Evolução mensal por status</h2>
              <p className="muted">Vendido · Contatado · Indisponível, últimos 6 meses.</p>
            </div>
          </div>
          <ResponsiveContainer width="100%" height={280}>
            <LineChart data={indicadoresMensais}>
              <CartesianGrid stroke="#DCD3BD" strokeDasharray="3 3" />
              <XAxis dataKey="mes" stroke="#8B9580" fontSize={12} />
              <YAxis stroke="#8B9580" fontSize={12} />
              <Tooltip />
              <Legend />
              <Line type="monotone" dataKey="vendidos" stroke="#2F7D45" strokeWidth={2} name="Vendido" />
              <Line type="monotone" dataKey="contatados" stroke="#B9791E" strokeWidth={2} name="Contatado" />
              <Line type="monotone" dataKey="indisponiveis" stroke="#B23A2E" strokeWidth={2} name="Indisponível" />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>
    </>
  )
}
