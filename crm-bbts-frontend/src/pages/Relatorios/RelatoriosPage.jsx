import { BarChart, Bar, XAxis, YAxis, Tooltip, CartesianGrid, ResponsiveContainer, Legend } from 'recharts'
import { Download } from 'lucide-react'
import Topbar from '../../components/layout/Topbar'
import Trilha from '../../components/common/Trilha'
import { indicadoresMensais } from '../../mock/data'
// import { relatorioService } from '../../api/relatorioService'

export default function RelatoriosPage() {
  function exportar() {
    // Integração real:
    // relatorioService.exportarCsv({}).then((res) => { ...salvar blob... })
    alert('Exportação simulada — ligar ao endpoint GET /relatorios/exportar no back-end.')
  }

  return (
    <>
      <Topbar eyebrow="Etapa 08" title="Relatórios e Indicadores" />
      <div className="app-content">
        <Trilha ativo="relatorios" />

        <div className="panel">
          <div className="panel-head">
            <div>
              <h2>Indicadores mensais consolidados</h2>
              <p>Volume de desfechos por status ao longo do semestre.</p>
            </div>
            <button className="btn btn-amber" onClick={exportar}>
              <Download size={15} /> Exportar CSV
            </button>
          </div>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={indicadoresMensais}>
              <CartesianGrid stroke="#DCD3BD" strokeDasharray="3 3" />
              <XAxis dataKey="mes" stroke="#8B9580" fontSize={12} />
              <YAxis stroke="#8B9580" fontSize={12} />
              <Tooltip />
              <Legend />
              <Bar dataKey="vendidos" fill="#2F7D45" name="Vendido" radius={[3, 3, 0, 0]} />
              <Bar dataKey="contatados" fill="#B9791E" name="Contatado" radius={[3, 3, 0, 0]} />
              <Bar dataKey="indisponiveis" fill="#B23A2E" name="Indisponível" radius={[3, 3, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>
    </>
  )
}
