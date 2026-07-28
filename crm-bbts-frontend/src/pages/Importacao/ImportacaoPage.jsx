import { useRef, useState } from 'react'
import { UploadCloud, FileSpreadsheet, CheckCircle2, AlertTriangle } from 'lucide-react'
import Topbar from '../../components/layout/Topbar'
import Trilha from '../../components/common/Trilha'
import DataTable from '../../components/common/DataTable'
import { importacoes as importacoesIniciais } from '../../mock/data'
// import { importacaoService } from '../../api/importacaoService'

export default function ImportacaoPage() {
  const [arquivos, setArquivos] = useState(importacoesIniciais)
  const [arrastando, setArrastando] = useState(false)
  const inputRef = useRef(null)

  function handleArquivoSelecionado(files) {
    if (!files?.length) return
    const novo = {
      id: arquivos.length + 1,
      arquivo: files[0].name,
      dataImportacao: new Date().toISOString(),
      linhasLidas: 0,
      linhasValidas: 0,
      linhasComErro: 0,
      status: 'AGUARDANDO_VALIDACAO',
    }
    setArquivos([novo, ...arquivos])

    // Integração real:
    // importacaoService.upload(files[0]).then((res) => { ... })
  }

  const columns = [
    { key: 'arquivo', header: 'Arquivo', render: (r) => (
      <span style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
        <FileSpreadsheet size={15} color="var(--canopy-500)" /> {r.arquivo}
      </span>
    )},
    { key: 'dataImportacao', header: 'Recebido em', render: (r) => new Date(r.dataImportacao).toLocaleString('pt-BR') },
    { key: 'linhasLidas', header: 'Linhas lidas', render: (r) => <span className="cell-mono">{r.linhasLidas}</span> },
    { key: 'linhasValidas', header: 'Válidas', render: (r) => <span className="cell-mono">{r.linhasValidas}</span> },
    { key: 'linhasComErro', header: 'Com erro', render: (r) => <span className="cell-mono">{r.linhasComErro}</span> },
    { key: 'status', header: 'Status', render: (r) => (
      r.status === 'VALIDADO'
        ? <span className="badge vendido"><CheckCircle2 size={12} /> Validado</span>
        : <span className="badge contatado"><AlertTriangle size={12} /> Aguardando validação</span>
    )},
  ]

  return (
    <>
      <Topbar eyebrow="Etapas 01–03" title="Recebimento, Importação e Validação da Base" />
      <div className="app-content">
        <Trilha ativo="recebimento" />

        <div className="panel">
          <div className="panel-head">
            <div>
              <h2>1 · Recebimento da base</h2>
              <p>A BBTS recebe a planilha XLS com os dados dos produtores rurais.</p>
            </div>
          </div>
          <div
            className={'dropzone' + (arrastando ? ' is-over' : '')}
            onDragOver={(e) => { e.preventDefault(); setArrastando(true) }}
            onDragLeave={() => setArrastando(false)}
            onDrop={(e) => {
              e.preventDefault()
              setArrastando(false)
              handleArquivoSelecionado(e.dataTransfer.files)
            }}
            onClick={() => inputRef.current?.click()}
            role="button"
            tabIndex={0}
          >
            <UploadCloud size={28} color="var(--amber-600)" />
            <h3 style={{ marginTop: 10 }}>Arraste a planilha .xls ou .xlsx aqui</h3>
            <p className="hint">ou clique para selecionar — processado por ExcelReaderService no back-end</p>
            <input
              ref={inputRef}
              type="file"
              accept=".xls,.xlsx"
              style={{ display: 'none' }}
              onChange={(e) => handleArquivoSelecionado(e.target.files)}
            />
          </div>
        </div>

        <div className="panel">
          <div className="panel-head">
            <div>
              <h2>2–3 · Importação, leitura e validação</h2>
              <p>Cada arquivo recebido é lido e validado linha a linha; pendências ficam visíveis em LogImportacao.</p>
            </div>
          </div>
          <DataTable columns={columns} rows={arquivos} />
        </div>
      </div>
    </>
  )
}
