function StatusBanner({ type = "info", message }) {
  if (!message) return null;

  const styles = {
    success: "border-emerald-200 bg-emerald-50 text-emerald-700",
    error: "border-red-200 bg-red-50 text-red-700",
    info: "border-brand-200 bg-brand-50 text-brand-900",
  };

  return (
    <div className={`rounded-2xl border px-4 py-3 text-sm ${styles[type]}`}>
      {message}
    </div>
  );
}

export default StatusBanner;
