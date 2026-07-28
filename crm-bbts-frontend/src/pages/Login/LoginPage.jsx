import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Eye, EyeOff, Sprout } from 'lucide-react'
import { useAuth } from '../../context/AuthContext'

export default function LoginPage() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const [username, setUsername] = useState('')
  const [senha, setSenha] = useState('')
  const [mostrar, setMostrar] = useState(false)
  const [erro, setErro] = useState('')
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e) {
    e.preventDefault()
    if (!username || !senha) { setErro('Preencha usuário e senha.'); return }
    setErro(''); setLoading(true)
    try {
      const data = await login(username, senha)
      // Redireciona por perfil
      navigate(data.perfil === 'VENDEDOR' ? '/vendedor' : '/', { replace: true })
    } catch (err) {
      if (err.response?.status === 401) setErro('Usuário ou senha incorretos.')
      else setErro('Não foi possível conectar ao servidor. Verifique se o back-end está rodando na porta 8080.')
    } finally { setLoading(false) }
  }

  return (
    <div style={{ minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', background: 'var(--bg-field)' }}>
      <div style={{ width: '100%', maxWidth: 400, padding: '0 16px' }}>
        {/* Logo */}
        <div style={{ textAlign: 'center', marginBottom: 28 }}>
          <div style={{ display: 'inline-flex', alignItems: 'center', gap: 10, background: 'var(--canopy-900)', padding: '10px 22px', borderRadius: 10 }}>
            <Sprout size={22} color="var(--amber-200)" />
            <span style={{ fontFamily: 'var(--font-display)', color: 'var(--amber-200)', fontSize: '1.35rem', fontWeight: 700 }}>BBTS · CRM Agro</span>
          </div>
        </div>

        <div className="panel">
          <h2 style={{ marginBottom: 4 }}>Bem-vindo</h2>
          <p style={{ marginBottom: 20 }}>Entre com as credenciais do seu usuário para acessar o sistema.</p>

          <form onSubmit={handleSubmit} autoComplete="on">
            <div className="field">
              <label htmlFor="username">Usuário</label>
              <input id="username" name="username" autoComplete="username"
                value={username} onChange={e => setUsername(e.target.value)} autoFocus />
            </div>
            <div className="field" style={{ position: 'relative' }}>
              <label htmlFor="senha">Senha</label>
              <input id="senha" name="password" autoComplete="current-password"
                type={mostrar ? 'text' : 'password'} value={senha} onChange={e => setSenha(e.target.value)}
                style={{ paddingRight: 40 }} />
              <button type="button" onClick={() => setMostrar(!mostrar)}
                style={{ position: 'absolute', right: 10, top: 30, background: 'none', border: 'none', cursor: 'pointer', color: 'var(--ink-faint)' }}>
                {mostrar ? <EyeOff size={16}/> : <Eye size={16}/>}
              </button>
            </div>

            {erro && (
              <div style={{ background: 'var(--status-indisponivel-bg)', color: 'var(--status-indisponivel)', padding: '9px 14px', borderRadius: 6, fontSize: 'var(--fs-sm)', marginBottom: 12 }}>
                {erro}
              </div>
            )}

            <button type="submit" className="btn btn-primary" disabled={loading}
              style={{ width: '100%', justifyContent: 'center', padding: '11px 0', fontSize: 'var(--fs-md)' }}>
              {loading ? 'Entrando…' : 'Entrar'}
            </button>
          </form>

          <div style={{ marginTop: 20, padding: '14px 16px', background: 'var(--amber-100)', borderRadius: 8, fontSize: 'var(--fs-xs)', color: 'var(--ink-soft)' }}>
            <strong>Usuários de demonstração (seed):</strong><br />
            Gerente: <code>admin</code> / <code>admin123</code><br />
            Vendedor: <code>marcos.teixeira</code> / <code>vendedor123</code>
          </div>
        </div>

        <p style={{ textAlign: 'center', marginTop: 16, fontSize: 'var(--fs-xs)', color: 'var(--ink-faint)' }}>
          BBTS · Sistema CRM Agronegócio · MVP
        </p>
      </div>
    </div>
  )
}
