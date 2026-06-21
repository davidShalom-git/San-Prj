function SectionCard({ title, subtitle, children }) {
  return (
    <section className="rounded-[28px] border border-white/70 bg-white/85 p-6 shadow-soft">
      <div className="mb-5">
        <h2 className="text-xl font-semibold text-slate-900">{title}</h2>
        {subtitle ? <p className="mt-1 text-sm text-slate-500">{subtitle}</p> : null}
      </div>
      {children}
    </section>
  );
}

export default SectionCard;
