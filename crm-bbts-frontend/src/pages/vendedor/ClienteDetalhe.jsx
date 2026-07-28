import AnaliseIaCard from '../../components/vendedor/AnaliseIaCard';
import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { ArrowLeft, CheckCircle2, XCircle, PhoneCall, BarChart2, RefreshCcw } from 'lucide-react'
import Topbar from '../../components/layout/Topbar'
import { clienteService } from '../../api/clienteService'
import { cpfService } from '../../api/cpfService'
import { usuarioService } from '../../api/usuarioService'
import { agendaService } from '../../api/agendaService'

// ===== Modal de confirmação com senha =====
function ModalConfirmacao({ titulo, onConfirmar, onCancelar, children }) {
  const [senha, setSenha] = useState('')
  const [erro, setErro] = useState('')
  const [loading, setLoading] = useState(false)

  async function handleConfirmar() {
    setErro('')
    setLoading(true)
    try {
      const { data } = await usuarioService.verificarSenha(senha)
      if (!data.valido) { setErro('Senha incorreta. Tente novamente.'); setLoading(false); return }
      await onConfirmar()
    } catch {
      setErro('Erro ao verificar senha.')
    } finally { setLoading(false) }
  }

  return (
    <div style={{ position: 'fixed', inset: 0, background: 'rgba(22,36,26,0.55)', zIndex: 100, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
      <div className="panel" style={{ width: 420, margin: 0 }}>
        <h2 style={{ marginBottom: 4 }}>{titulo}</h2>
        {children}
        <div className="field" style={{ marginTop: 16 }}>
          <label>Confirme com sua senha de acesso</label>
          <input type="password" value={senha} onChange={e => setSenha(e.target.value)} autoFocus
            onKeyDown={e => e.key === 'Enter' && handleConfirmar()} />
        </div>
        {erro && <p style={{ color: 'var(--status-indisponivel)', fontSize: 'var(--fs-sm)' }}>{erro}</p>}
        <div style={{ display: 'flex', gap: 8, marginTop: 14 }}>
          <button className="btn btn-ghost" onClick={onCancelar} style={{ flex: 1 }}>Cancelar</button>
          <button className="btn btn-primary" onClick={handleConfirmar} disabled={loading || !senha} style={{ flex: 1 }}>
            {loading ? 'Verificando…' : 'Confirmar'}
          </button>
        </div>
      </div>
    </div>
  )
}

export default function ClienteDetalhe() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [cliente, setCliente] = useState(null)
  const [margem, setMargem] = useState(null)
  const [modal, setModal] = useState(null) // 'VENDIDO' | 'INDISPONIVEL' | 'CONTATADO'
  const [form, setForm] = useState({ convenio: '', prazo: '', valorVendido: '', observacao: '', dataAgendada: '' })
  const [sucesso, setSucesso] = useState('')

  useEffect(() => {
    // Ao abrir a ficha → o backend já loga ABERTURA automaticamente via GET /api/clientes/{id}
    clienteService.obter(id).then(r => {
      setCliente(r.data)
      // Consulta margem Dataprev se CPF disponível
      if (r.data.cpf) {
        cpfService.consultarMargem(r.data.cpf).then(m => setMargem(m.data)).catch(() => {})
      }
    })
  }, [id])

  const f = (field, val) => setForm(prev => ({ ...prev, [field]: val }))

  async function confirmarAtendimento() {
    const payload = {
      status: modal,
      observacao: form.observacao,
      convenio: modal === 'VENDIDO' ? form.convenio : undefined,
      prazo: modal === 'VENDIDO' ? parseInt(form.prazo) || null : undefined,
      valorVendido: modal === 'VENDIDO' ? parseFloat(form.valorVendido) || null : undefined,
    }
    await clienteService.atualizarAtendimento(id, payload)

    if (modal === 'INDISPONIVEL' && form.dataAgendada) {
      await agendaService.agendar({ clienteId: parseInt(id), vendedorId: cliente.vendedorId, dataAgendada: form.dataAgendada, observacao: form.observacao })
    }

    setSucesso(`Status atualizado para ${modal} com sucesso!`)
    setModal(null)
    clienteService.obter(id).then(r => setCliente(r.data))
  }

  if (!cliente) return <div className="app-content"><p className="muted">Carregando ficha…</p></div>

  const fmt = (v) => v ? `R$ ${parseFloat(v).toLocaleString('pt-BR', { minimumFractionDigits: 2 })}` : '—'

  return (
    <>
      <Topbar eyebrow="Atendimento" title={cliente.nome} />
      <div className="app-content">
        <button className="btn btn-ghost btn-sm" onClick={() => navigate('/vendedor')} style={{ marginBottom: 16 }}>
          <ArrowLeft size={14} /> Voltar à carteira
        </button>

        {sucesso && <div style={{ background: 'var(--status-vendido-bg)', color: 'var(--status-vendido)', padding: '10px 16px', borderRadius: 6, marginBottom: 14 }}>{sucesso}</div>}

        {/* Ações principais */}
        <div style={{ display: 'flex', gap: 10, marginBottom: 20, flexWrap: 'wrap' }}>
          <button className="btn btn-primary" onClick={() => setModal('CONTATADO')}>
            <PhoneCall size={15}/> Contatado
          </button>
          <button className="btn btn-amber" onClick={() => setModal('VENDIDO')}>
            <CheckCircle2 size={15}/> Registrar Venda
          </button>
          <button className="btn btn-ghost" onClick={() => setModal('INDISPONIVEL')}>
            <XCircle size={15}/> Indisponível / Agendar
          </button>
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 18 }}>
          {/* Dados do produtor */}
          <div className="panel">
            <h2>Dados do produtor</h2>
            <table style={{ width: '100%', fontSize: 'var(--fs-sm)', borderCollapse: 'collapse' }}>
              {[
                ['CPF', cliente.cpf || '—'],
                ['Propriedade', cliente.propriedade || '—'],
                ['Cultura', cliente.cultura || '—'],
                ['Município', cliente.municipio || '—'],
                ['Telefone', cliente.telefone || '—'],
                ['E-mail', cliente.email || '—'],
              ].map(([k, v]) => (
                <tr key={k}>
                  <td style={{ color: 'var(--ink-faint)', padding: '7px 0', width: '40%' }}>{k}</td>
                  <td style={{ fontFamily: 'var(--font-mono)', fontSize: 'var(--fs-xs)' }}>{v}</td>
                </tr>
              ))}
            </table>
          </div>

          {/* Margem Dataprev */}
          <div className="panel">
            <h2>Margem consignado {margem?.mock && <span className="badge pendente" style={{ fontSize: 10 }}>simulado</span>}</h2>
            {!cliente.cpf ? (
              <p className="muted">CPF não cadastrado — preencha para consultar a margem.</p>
            ) : !margem ? (
              <p className="muted">Consultando Dataprev…</p>
            ) : margem.mensagemErro ? (
              <p style={{ color: 'var(--status-indisponivel)' }}>{margem.mensagemErro}</p>
            ) : (
              <div className="kpi-grid" style={{ gridTemplateColumns: '1fr 1fr' }}>
                <div className="kpi"><div className="kpi-label">Benefício</div><div className="kpi-value" style={{ fontSize: 'var(--fs-md)' }}>{fmt(margem.valorBeneficio)}</div></div>
                <div className="kpi"><div className="kpi-label">Margem disponível</div><div className="kpi-value" style={{ fontSize: 'var(--fs-md)', color: 'var(--status-vendido)' }}>{fmt(margem.margemDisponivel)}</div></div>
                <div className="kpi"><div className="kpi-label">Margem utilizada</div><div className="kpi-value" style={{ fontSize: 'var(--fs-md)' }}>{fmt(margem.margemUtilizada)}</div></div>
                <div className="kpi"><div className="kpi-label">Espécie</div><div style={{ fontFamily: 'var(--font-mono)', fontSize: 'var(--fs-xs)', marginTop: 6 }}>{margem.especieBeneficio || '—'}</div></div>
              </div>
            )}
          </div>

          {/* Dados do atendimento atual */}
          <div className="panel" style={{ gridColumn: '1 / -1' }}>
            <h2>Resultado do atendimento</h2>
            <div className="kpi-grid">
              <div className="kpi"><div className="kpi-label">Status atual</div>
                <div style={{ marginTop: 6 }}><span className={`badge ${cliente.status?.toLowerCase()}`}>{cliente.status}</span></div></div>
              <div className="kpi"><div className="kpi-label">Convênio</div><div className="kpi-value" style={{ fontSize: 'var(--fs-md)' }}>{cliente.convenio || '—'}</div></div>
              <div className="kpi"><div className="kpi-label">Prazo</div><div className="kpi-value" style={{ fontSize: 'var(--fs-md)' }}>{cliente.prazo ? `${cliente.prazo} meses` : '—'}</div></div>
              <div className="kpi"><div className="kpi-label">Valor vendido</div><div className="kpi-value" style={{ fontSize: 'var(--fs-md)', color: 'var(--status-vendido)' }}>{fmt(cliente.valorVendido)}</div></div>
            </div>
            {cliente.observacao && (
              <div style={{ marginTop: 12, padding: '10px 14px', background: 'var(--amber-100)', borderRadius: 6, fontSize: 'var(--fs-sm)', color: 'var(--ink-deep)' }}>
                <strong>Observação:</strong> {cliente.observacao}
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Modal CONTATADO */}
      {modal === 'CONTATADO' && (
        <ModalConfirmacao titulo="Registrar contato" onCancelar={() => setModal(null)} onConfirmar={confirmarAtendimento}>
          <p>O cliente foi contatado mas não fechou negócio. Adicione uma observação.</p>
          <div className="field">
            <label>Observação</label>
            <textarea rows={3} value={form.observacao} onChange={e => f('observacao', e.target.value)} />
          </div>
        </ModalConfirmacao>
      )}

      {/* Modal INDISPONIVEL */}
      {modal === 'INDISPONIVEL' && (
        <ModalConfirmacao titulo="Cliente indisponível" onCancelar={() => setModal(null)} onConfirmar={confirmarAtendimento}>
          <p>Cliente indisponível no momento. Agende um retorno e preencha a observação.</p>
          <div className="field">
            <label>Data do próximo contato</label>
            <input type="datetime-local" value={form.dataAgendada} onChange={e => f('dataAgendada', e.target.value)} />
          </div>
          <div className="field">
            <label>Observação</label>
            <textarea rows={2} value={form.observacao} onChange={e => f('observacao', e.target.value)} />
          </div>
        </ModalConfirmacao>
      )}

      {/* Modal VENDIDO */}
      {modal === 'VENDIDO' && (
        <ModalConfirmacao titulo="Registrar venda" onCancelar={() => setModal(null)} onConfirmar={confirmarAtendimento}>
          <p>Preencha os dados da venda realizada.</p>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 8 }}>
            <div className="field">
              <label>Convênio</label>
              <input value={form.convenio} onChange={e => f('convenio', e.target.value)} placeholder="Ex: INSS, Siape…" />
            </div>
            <div className="field">
              <label>Prazo (meses)</label>
              <input type="number" min="1" max="96" value={form.prazo} onChange={e => f('prazo', e.target.value)} />
            </div>
          </div>
          <div className="field">
            <label>Valor vendido (R$)</label>
            <input type="number" step="0.01" value={form.valorVendido} onChange={e => f('valorVendido', e.target.value)} />
          </div>
          <div className="field">
            <label>Observação</label>
            <textarea rows={2} value={form.observacao} onChange={e => f('observacao', e.target.value)} />
          </div>
        </ModalConfirmacao>
      )}
    </>
  )
}
