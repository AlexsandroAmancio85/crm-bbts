// Tabela genérica reutilizada nas telas de Clientes, Importação, etc.
export default function DataTable({ columns, rows, rowKey = 'id', emptyMessage = 'Nenhum registro encontrado.' }) {
  if (!rows?.length) {
    return (
      <div className="empty-state">
        <h3>Nada por aqui ainda</h3>
        <p className="muted">{emptyMessage}</p>
      </div>
    )
  }
  return (
    <div className="table-wrap">
      <table className="data-table">
        <thead>
          <tr>
            {columns.map((col) => (
              <th key={col.key}>{col.header}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {rows.map((row) => (
            <tr key={row[rowKey]}>
              {columns.map((col) => (
                <td key={col.key}>{col.render ? col.render(row) : row[col.key]}</td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
