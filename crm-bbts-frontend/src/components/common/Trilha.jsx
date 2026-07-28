import { estagiosTrilha } from '../../mock/data'

// Elemento de assinatura: a trilha horizontal espelha exatamente o fluxograma
// fornecido (Recebimento -> Importação -> ... -> Relatórios), com contagem
// de registros em cada etapa — como sulcos de uma lavoura, em sequência.
export default function Trilha({ contagens = {}, ativo }) {
  return (
    <div className="trilha">
      {estagiosTrilha.map((e) => {
        const isAtivo = ativo === e.chave
        const valor = contagens[e.chave]
        return (
          <div
            key={e.chave}
            className={
              'trilha-stage' +
              (isAtivo ? ' is-active' : '') +
              (!isAtivo && valor !== undefined ? ' is-done' : '')
            }
          >
            <span className="no">{String(e.no).padStart(2, '0')}</span>
            <div className="label">{e.label}</div>
            {valor !== undefined && <div className="count">{valor}</div>}
          </div>
        )
      })}
    </div>
  )
}
