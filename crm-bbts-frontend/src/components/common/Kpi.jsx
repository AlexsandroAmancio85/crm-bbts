export default function Kpi({ label, value, delta, direction }) {
  return (
    <div className="kpi">
      <div className="kpi-label">{label}</div>
      <div className="kpi-value">{value}</div>
      {delta && <div className={`kpi-delta ${direction || ''}`}>{delta}</div>}
    </div>
  )
}
