import { Routes, Route, Navigate } from 'react-router-dom'
import { useAuth } from './context/AuthContext'

// Layouts
import Sidebar from './components/layout/Sidebar'
import VendedorSidebar from './components/vendedor/VendedorSidebar'

// Shared
import LoginPage from './pages/Login/LoginPage'

// Gerente (views existentes)
import Dashboard from './pages/Dashboard/Dashboard'
import ImportacaoPage from './pages/Importacao/ImportacaoPage'
import QualificacaoPage from './pages/Qualificacao/QualificacaoPage'
import DistribuicaoPage from './pages/Distribuicao/DistribuicaoPage'
import AtendimentoPage from './pages/Atendimento/AtendimentoPage'
import MonitoramentoPage from './pages/Monitoramento/MonitoramentoPage'
import RelatoriosPage from './pages/Relatorios/RelatoriosPage'
import ClientesPage from './pages/Clientes/ClientesPage'
import UsuariosPage from './pages/gerente/UsuariosPage'
import LogSistemaPage from './pages/gerente/LogSistemaPage'

// Vendedor
import CarteiraDashboard from './pages/vendedor/CarteiraDashboard'
import ClienteDetalhe from './pages/vendedor/ClienteDetalhe'
import RelatorioVendedor from './pages/vendedor/RelatorioVendedor'
import PrintCarteira from './pages/vendedor/PrintCarteira'

// ===== Layouts =====
function GerenteLayout({ children }) {
  return (
    <div className="app-shell">
      <Sidebar />
      <div className="app-main">{children}</div>
    </div>
  )
}

function VendedorLayout({ children }) {
  return (
    <div className="app-shell">
      <VendedorSidebar />
      <div className="app-main">{children}</div>
    </div>
  )
}

// Guard que redireciona para /login se não autenticado
function RequireAuth({ children }) {
  const { usuario } = useAuth()
  if (!usuario) return <Navigate to="/login" replace />
  return children
}

// Guard que força VENDEDOR para a visão própria
function RequireGerente({ children }) {
  const { usuario } = useAuth()
  if (!usuario) return <Navigate to="/login" replace />
  if (usuario.perfil === 'VENDEDOR') return <Navigate to="/vendedor" replace />
  return children
}

export default function App() {
  return (
    <Routes>
      {/* Login — redireciona por perfil após autenticação */}
      <Route path="/login" element={<LoginPage />} />

      {/* ====== VISÃO DO VENDEDOR ====== */}
      <Route path="/vendedor" element={
        <RequireAuth>
          <VendedorLayout>
            <CarteiraDashboard />
          </VendedorLayout>
        </RequireAuth>
      } />
      <Route path="/vendedor/cliente/:id" element={
        <RequireAuth>
          <VendedorLayout>
            <ClienteDetalhe />
          </VendedorLayout>
        </RequireAuth>
      } />
      <Route path="/vendedor/relatorio" element={
        <RequireAuth>
          <VendedorLayout>
            <RelatorioVendedor />
          </VendedorLayout>
        </RequireAuth>
      } />
      <Route path="/vendedor/imprimir" element={
        <RequireAuth>
          <VendedorLayout>
            <PrintCarteira />
          </VendedorLayout>
        </RequireAuth>
      } />

      {/* ====== VISÃO DO GERENTE ====== */}
      <Route path="/" element={<RequireGerente><GerenteLayout><Dashboard /></GerenteLayout></RequireGerente>} />
      <Route path="/clientes" element={<RequireGerente><GerenteLayout><ClientesPage /></GerenteLayout></RequireGerente>} />
      <Route path="/importacao" element={<RequireGerente><GerenteLayout><ImportacaoPage /></GerenteLayout></RequireGerente>} />
      <Route path="/qualificacao" element={<RequireGerente><GerenteLayout><QualificacaoPage /></GerenteLayout></RequireGerente>} />
      <Route path="/distribuicao" element={<RequireGerente><GerenteLayout><DistribuicaoPage /></GerenteLayout></RequireGerente>} />
      <Route path="/atendimento" element={<RequireGerente><GerenteLayout><AtendimentoPage /></GerenteLayout></RequireGerente>} />
      <Route path="/monitoramento" element={<RequireGerente><GerenteLayout><MonitoramentoPage /></GerenteLayout></RequireGerente>} />
      <Route path="/relatorios" element={<RequireGerente><GerenteLayout><RelatoriosPage /></GerenteLayout></RequireGerente>} />
      <Route path="/gerente/usuarios" element={<RequireGerente><GerenteLayout><UsuariosPage /></GerenteLayout></RequireGerente>} />
      <Route path="/gerente/logs" element={<RequireGerente><GerenteLayout><LogSistemaPage /></GerenteLayout></RequireGerente>} />

      {/* Catch-all */}
      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  )
}
