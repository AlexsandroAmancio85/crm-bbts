import { useEffect, useState, useRef } from 'react'
import { Printer, ArrowLeft } from 'lucide-react'
import { useNavigate } from 'react-router-dom'
import { clienteService } from '../../api/clienteService'
import { useAuth } from '../../context/AuthContext'

const STATUS_LABEL = { PENDENTE: 'Pendente', CONTATADO: 'Contatado', VENDIDO: 'Vendido', INDISPONIVEL: 'Indisponível' }

export default function PrintCarteira() {
  const { usuario } = useAuth()
  const navigate = useNavigate()
  const [clientes, setClientes] = useState([])
  const [carregando, setCarregando] = useState(true)
  const printRef = useRef()

  useEffect(() => {
    clienteService.listar({}).then(r => {
      setClientes(r.data)
      setCarregando(false)
    }).catch(() => setCarregando(false))
  }, [])

  function imprimir() { window.print() }

  return (
    <>
      {/* Controles — ocultos na impressão */}
      <div className="app-content" style={{ paddingBottom: 0 }}>
        <div className="flex-between" style={{ marginBottom: 16 }}>
          <button className="btn btn-ghost btn-sm" onClick={() => navigate('/vendedor')}>
            <ArrowLeft size={14} /> Voltar
          </button>
          <button className="btn btn-primary" onClick={imprimir}>
            <Printer size={14} /> Imprimir / Salvar PDF
          </button>
        </div>
      </div>

      {/* Área imprimível */}
      <div ref={printRef} style={{ padding: '24px 36px', fontFamily: 'var(--font-body)' }} className="print-area">
        <style>{`
          @media print {
            .no-print { display: none !important; }
            .print-area { padding: 0; }
            body { background: white; }
            .sidebar, .topbar, .app-content > div:first-child { display: none !important; }
          }
        `}</style>

        <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 24, alignItems: 'flex-end' }}>
          <div>
            <div style={{ fontFamily: 'var(--font-display)', fontSize: 22, fontWeight: 700 }}>BBTS · CRM Agronegócio</div>
            <div style={{ fontSize: 13, color: 'var(--ink-soft)' }}>Carteira de {usuario?.nome}</div>
          </div>
          <div style={{ textAlign: 'right', fontSize: 12, color: 'var(--ink-faint)' }}>
            Emitido em {new Date().toLocaleString('pt-BR')}<br />
            Total: {clientes.length} clientes
          </div>
        </div>

        {carregando ? <p>Carregando…</p> : (
          <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 12 }}>
            <thead>
              <tr style={{ borderBottom: '2px solid #1E2A18' }}>
                {['#', 'Produtor', 'CPF', 'Município', 'Cultura', 'Telefone', 'Status', 'Convênio', 'Prazo', 'Valor', 'Observação'].map(h => (
                  <th key={h} style={{ textAlign: 'left', padding: '6px 8px', fontSize: 10, textTransform: 'uppercase', letterSpacing: '.04em', color: '#4B5A40' }}>{h}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {clientes.map((c, i) => (
                <tr key={c.id} style={{ borderBottom: '1px solid #DCD3BD', background: i % 2 === 0 ? '#fff' : '#F9F5EC' }}>
                  <td style={{ padding: '7px 8px', color: '#8B9580', fontFamily: 'monospace' }}>{String(i + 1).padStart(3, '0')}</td>
                  <td style={{ padding: '7px 8px', fontWeight: 600 }}>{c.nome}</td>
                  <td style={{ padding: '7px 8px', fontFamily: 'monospace', fontSize: 11 }}>{c.cpf || '—'}</td>
                  <td style={{ padding: '7px 8px' }}>{c.municipio || '—'}</td>
                  <td style={{ padding: '7px 8px' }}>{c.cultura || '—'}</td>
                  <td style={{ padding: '7px 8px', fontFamily: 'monospace', fontSize: 11 }}>{c.telefone || '—'}</td>
                  <td style={{ padding: '7px 8px' }}>
                    <span style={{
                      padding: '2px 8px', borderRadius: 99, fontFamily: 'monospace', fontSize: 10, fontWeight: 600,
                      background: c.status === 'VENDIDO' ? '#E1F1E3' : c.status === 'INDISPONIVEL' ? '#FBE6E2' : c.status === 'CONTATADO' ? '#FBEED4' : '#E7EBEE',
                      color: c.status === 'VENDIDO' ? '#2F7D45' : c.status === 'INDISPONIVEL' ? '#B23A2E' : c.status === 'CONTATADO' ? '#B9791E' : '#5C6B7A',
                    }}>{STATUS_LABEL[c.status] || c.status}</span>
                  </td>
                  <td style={{ padding: '7px 8px', fontSize: 11 }}>{c.convenio || '—'}</td>
                  <td style={{ padding: '7px 8px', fontFamily: 'monospace', fontSize: 11 }}>{c.prazo ? `${c.prazo}m` : '—'}</td>
                  <td style={{ padding: '7px 8px', fontFamily: 'monospace', fontSize: 11 }}>
                    {c.valorVendido ? `R$ ${parseFloat(c.valorVendido).toLocaleString('pt-BR', { minimumFractionDigits: 2 })}` : '—'}
                  </td>
                  <td style={{ padding: '7px 8px', fontSize: 10, color: '#4B5A40', maxWidth: 180 }}>{c.observacao || '—'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}

        <div style={{ marginTop: 32, borderTop: '1px solid #DCD3BD', paddingTop: 12, display: 'flex', justifyContent: 'space-between', fontSize: 11, color: '#8B9580' }}>
          <span>BBTS · CRM Agronegócio — Documento gerado automaticamente pelo sistema.</span>
          <span>Atualizado diariamente. Dados sujeitos a alteração.</span>
        </div>
      </div>
    </>
  )
}
