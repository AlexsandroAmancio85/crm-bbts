import { NavLink, useNavigate } from 'react-router-dom'
import { LayoutGrid, Users, BarChart2, LogOut, Printer } from 'lucide-react'
import { useAuth } from '../../context/AuthContext'

const links = [
  { to: '/vendedor', icon: <LayoutGrid size={15}/>, label: 'Minha Carteira', end: true },
  { to: '/vendedor/relatorio', icon: <BarChart2 size={15}/>, label: 'Minha Produtividade' },
  { to: '/vendedor/imprimir', icon: <Printer size={15}/>, label: 'Imprimir Carteira' },
]

export default function VendedorSidebar() {
  const { usuario, logout } = useAuth()
  const navigate = useNavigate()

  function sair() { logout(); navigate('/login') }

  return (
    <aside className="sidebar">
      <div className="sidebar-brand">
        <span className="mark">BBTS</span>
        <span className="sub">VENDEDOR</span>
      </div>
      {links.map(l => (
        <NavLink key={l.to} to={l.to} end={l.end}
          className={({ isActive }) => 'sidebar-link' + (isActive ? ' active' : '')}>
          {l.icon}{l.label}
        </NavLink>
      ))}
      <div className="sidebar-footer">
        {usuario && <div style={{ color: '#D6DCC9', marginBottom: 10 }}><strong>{usuario.nome}</strong><br/><span style={{ fontSize: 'var(--fs-xs)', color: '#8FA083' }}>{usuario.perfil}</span></div>}
        <button onClick={sair} className="sidebar-link" style={{ background: 'none', border: 'none', cursor: 'pointer', width: '100%' }}>
          <LogOut size={14}/> Sair
        </button>
      </div>
    </aside>
  )
}
