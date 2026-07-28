import { useEffect, useState } from 'react'
import { UserPlus, Shield, UserX, Eye, EyeOff } from 'lucide-react'
import Topbar from '../../components/layout/Topbar'
import { usuarioService } from '../../api/usuarioService'
import { vendedorService } from '../../api/vendedorService'

const PERFIL_BADGE = {
  GERENTE: { label: 'Gerente', className: 'contatado' },
  VENDEDOR: { label: 'Vendedor', className: 'pendente' },
  ADMIN:    { label: 'Admin',   className: 'vendido' },
}

function ModalNovoUsuario({ vendedores, onSalvar, onCancelar }) {
  const [form, setForm] = useState({ nome: '', username: '', senha: '', perfil: 'VENDEDOR', vendedorId: '' })
  const [mostrarSenha, setMostrarSenha] = useState(false)
  const [erro, setErro] = useState('')
  const f = (k, v) => setForm(prev => ({ ...prev, [k]: v }))

  async function salvar() {
    setErro('')
    if (!form.nome || !form.username || !form.senha) { setErro('Preencha todos os campos obrigatórios.'); return }
    try {
      await onSalvar({ ...form, vendedorId: form.vendedorId ? parseInt(form.vendedorId) : null })
    } catch (e) {
      setErro(e.response?.data?.message || 'Erro ao criar usuário.')
    }
  }

  return (
    <div style={{ position: 'fixed', inset: 0, background: 'rgba(22,36,26,0.55)', zIndex: 100, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
      <div className="panel" style={{ width: 440, margin: 0 }}>
        <h2>Novo usuário</h2>
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 8 }}>
          <div className="field" style={{ gridColumn: '1 / -1' }}>
            <label>Nome completo *</label>
            <input value={form.nome} onChange={e => f('nome', e.target.value)} />
          </div>
          <div className="field">
            <label>Username *</label>
            <input value={form.username} onChange={e => f('username', e.target.value)} />
          </div>
          <div className="field">
            <label>Perfil *</label>
            <select value={form.perfil} onChange={e => f('perfil', e.target.value)}>
              <option value="VENDEDOR">Vendedor</option>
              <option value="GERENTE">Gerente</option>
              <option value="ADMIN">Admin</option>
            </select>
          </div>
          <div className="field" style={{ gridColumn: '1 / -1', position: 'relative' }}>
            <label>Senha *</label>
            <input type={mostrarSenha ? 'text' : 'password'} value={form.senha} onChange={e => f('senha', e.target.value)} />
            <button type="button" onClick={() => setMostrarSenha(!mostrarSenha)}
              style={{ position: 'absolute', right: 10, top: 30, background: 'none', border: 'none', cursor: 'pointer' }}>
              {mostrarSenha ? <EyeOff size={15} /> : <Eye size={15} />}
            </button>
          </div>
          {form.perfil === 'VENDEDOR' && (
            <div className="field" style={{ gridColumn: '1 / -1' }}>
              <label>Vincular ao vendedor</label>
              <select value={form.vendedorId} onChange={e => f('vendedorId', e.target.value)}>
                <option value="">Não vincular</option>
                {vendedores.map(v => <option key={v.id} value={v.id}>{v.nome} — {v.regiao}</option>)}
              </select>
            </div>
          )}
        </div>
        {erro && <p style={{ color: 'var(--status-indisponivel)', fontSize: 'var(--fs-sm)' }}>{erro}</p>}
        <div style={{ display: 'flex', gap: 8, marginTop: 14 }}>
          <button className="btn btn-ghost" onClick={onCancelar} style={{ flex: 1 }}>Cancelar</button>
          <button className="btn btn-primary" onClick={salvar} style={{ flex: 1 }}>Criar usuário</button>
        </div>
      </div>
    </div>
  )
}

export default function UsuariosPage() {
  const [usuarios, setUsuarios] = useState([])
  const [vendedores, setVendedores] = useState([])
  const [modalAberto, setModalAberto] = useState(false)
  const [carregando, setCarregando] = useState(true)

  function carregar() {
    Promise.all([usuarioService.listar(), vendedorService.listar()]).then(([u, v]) => {
      setUsuarios(u.data)
      setVendedores(v.data)
      setCarregando(false)
    })
  }

  useEffect(() => { carregar() }, [])

  async function criarUsuario(dados) {
    await usuarioService.criar(dados)
    setModalAberto(false)
    carregar()
  }

  async function desativar(id) {
    if (!window.confirm('Desativar este usuário?')) return
    await usuarioService.desativar(id)
    carregar()
  }

  return (
    <>
      <Topbar eyebrow="Controles de acesso" title="Gestão de usuários" />
      <div className="app-content">
        <div className="panel">
          <div className="panel-head">
            <div>
              <h2>Usuários do sistema</h2>
              <p>Gerencie contas, perfis e vínculos com vendedores. Apenas gerentes podem criar usuários (item 7.1).</p>
            </div>
            <button className="btn btn-primary" onClick={() => setModalAberto(true)}>
              <UserPlus size={15} /> Novo usuário
            </button>
          </div>

          {carregando ? <p className="muted">Carregando…</p> : (
            <div className="table-wrap">
              <table className="data-table">
                <thead>
                  <tr>
                    <th>Nome</th><th>Username</th><th>Perfil</th><th>Vendedor vinculado</th><th>Status</th><th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {usuarios.map(u => {
                    const meta = PERFIL_BADGE[u.perfil] || { label: u.perfil, className: 'pendente' }
                    return (
                      <tr key={u.id}>
                        <td><strong>{u.nome}</strong></td>
                        <td><span className="cell-mono">{u.username}</span></td>
                        <td><span className={`badge ${meta.className}`}><Shield size={10}/> {meta.label}</span></td>
                        <td>{u.vendedorNome || <span className="muted">—</span>}</td>
                        <td>
                          {u.ativo
                            ? <span className="badge vendido">Ativo</span>
                            : <span className="badge indisponivel">Inativo</span>}
                        </td>
                        <td>
                          {u.ativo && (
                            <button className="btn btn-ghost btn-sm" onClick={() => desativar(u.id)}>
                              <UserX size={13}/> Desativar
                            </button>
                          )}
                        </td>
                      </tr>
                    )
                  })}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>

      {modalAberto && (
        <ModalNovoUsuario
          vendedores={vendedores}
          onSalvar={criarUsuario}
          onCancelar={() => setModalAberto(false)}
        />
      )}
    </>
  )
}
