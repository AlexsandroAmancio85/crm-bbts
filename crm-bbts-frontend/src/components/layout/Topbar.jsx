export default function Topbar({ eyebrow, title }) {
  return (
    <header className="topbar">
      <div className="topbar-title">
        {eyebrow && <span className="eyebrow">{eyebrow}</span>}
        <h1 className="mt-0">{title}</h1>
      </div>
      <div className="topbar-user">
        <div className="avatar">G</div>
        <div>
          <strong>Gerente Comercial</strong>
        </div>
      </div>
    </header>
  )
}
