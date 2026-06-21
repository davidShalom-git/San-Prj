import FormInput from "./FormInput";
import TextAreaInput from "./TextAreaInput";

function ArraySection({ title, items, fields, onAdd, onRemove, onChange }) {
  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h3 className="text-lg font-semibold text-slate-900">{title}</h3>
        <button
          type="button"
          onClick={onAdd}
          className="rounded-full bg-brand-500 px-4 py-2 text-sm font-medium text-white transition hover:bg-brand-600"
        >
          Add Item
        </button>
      </div>

      {items.length === 0 ? (
        <div className="rounded-2xl border border-dashed border-slate-300 bg-slate-50 px-4 py-6 text-sm text-slate-500">
          No items added yet.
        </div>
      ) : null}

      {items.map((item, index) => (
        <div key={index} className="rounded-3xl border border-slate-200 bg-slate-50/70 p-4">
          <div className="mb-4 flex items-center justify-between">
            <span className="text-sm font-medium text-slate-700">{title} #{index + 1}</span>
            <button
              type="button"
              onClick={() => onRemove(index)}
              className="text-sm font-medium text-red-500 hover:text-red-600"
            >
              Remove
            </button>
          </div>
          <div className="grid gap-4 md:grid-cols-2">
            {fields.map((field) =>
              field.type === "textarea" ? (
                <div className="md:col-span-2" key={field.name}>
                  <TextAreaInput
                    label={field.label}
                    name={field.name}
                    value={item[field.name] ?? ""}
                    onChange={(event) => onChange(index, field.name, event.target.value)}
                    placeholder={field.placeholder}
                  />
                </div>
              ) : (
                <FormInput
                  key={field.name}
                  label={field.label}
                  name={field.name}
                  value={item[field.name] ?? ""}
                  onChange={(event) => onChange(index, field.name, event.target.value)}
                  placeholder={field.placeholder}
                />
              )
            )}
          </div>
        </div>
      ))}
    </div>
  );
}

export default ArraySection;
