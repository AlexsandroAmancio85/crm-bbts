import { statusMeta } from '../../mock/data'

export default function StatusBadge({ status }) {
  const meta = statusMeta[status] || statusMeta.pendente
  return <span className={`badge ${meta.className}`}>{meta.label}</span>
}
