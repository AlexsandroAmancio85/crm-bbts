import { NavLink, useNavigate } from 'react-router-dom'
import { Sprout, LayoutGrid, LogOut, Users, FileText, Activity } from 'lucide-react'
import { useAuth } from '../../context/AuthContext'

const pipelineLinks = [
  { no: '01', to: '/importacao', label: 'Recebimento & Importação' },
  { no: '02', to: '/qualificacao', label: 'Qualificação Gerencial' },
  { no: '03', to: '/distribuicao', label: 'Distribuição' },
  { no: '04', to: '/atendimento', label: 'Atendimento' },
  { no: '05', to: '/monitoramento', label: 'Monitoramento' },
  { no: '06', to: '/relatorios', label: 'Relatórios' },
]

export default function Sidebar() {
  const { usuario, logout } = useAuth()
  const navigate = useNavigate()
  function sair() { logout(); navigate('/login') }

  return (
    <aside className="sidebar">
      <div className="sidebar-brand">
        <span className="mark">BBTS</span>
        <span className="sub">CRM · AGRO</span>
      </div>

      <NavLink to="/" end className={({ isActive }) => 'sidebar-link' + (isActive ? ' active' : '')}>
        <LayoutGrid size={15}/> Visão geral
      </NavLink>
      <NavLink to="/clientes" className={({ isActive }) => 'sidebar-link' + (isActive ? ' active' : '')}>
        <Sprout size={15}/> Clientes
      </NavLink>

      <div className="sidebar-section-label">Trilha da safra</div>
      <nav className="sidebar-track">
        {pipelineLinks.map(l => (
          <NavLink key={l.to} to={l.to}
            className={({ isActive }) => 'sidebar-link' + (isActive ? ' active' : '')}>
            <span className="stage-no">{l.no}</span>{l.label}
          </NavLink>
        ))}
      </nav>

      <div className="sidebar-section-label">Administração</div>
      <NavLink to="/gerente/usuarios" className={({ isActive }) => 'sidebar-link' + (isActive ? ' active' : '')}>
        <Users size={15}/> Usuários
      </NavLink>
      <NavLink to="/gerente/logs" className={({ isActive }) => 'sidebar-link' + (isActive ? ' active' : '')}>
        <Activity size={15}/> Logs / Auditoria
      </NavLink>

      <div className="sidebar-footer">
        {usuario && (
          <div style={{ color: '#D6DCC9', marginBottom: 10 }}>
            <strong>{usuario.nome}</strong><br/>
            <span style={{ fontSize: 'var(--fs-xs)', color: '#8FA083' }}>{usuario.perfil}</span>
          </div>
        )}
        <button onClick={sair} className="sidebar-link"
          style={{ background: 'none', border: 'none', cursor: 'pointer', width: '100%' }}>
          <LogOut size={14}/> Sair
        </button>
        <div style={{ marginTop: 10 }}>crm-bbts · MVP<br/>br.com.bbts.crm</div>
      </div>
    </aside>
  )
}
