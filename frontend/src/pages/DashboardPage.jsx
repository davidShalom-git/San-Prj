import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import SectionCard from "../components/SectionCard";
import LoadingSpinner from "../components/LoadingSpinner";
import StatusBanner from "../components/StatusBanner";
import { deleteUser, getUsers } from "../services/userService";

function DashboardPage() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [status, setStatus] = useState({ type: "info", message: "" });

  const loadUsers = async () => {
    try {
      setLoading(true);
      const data = await getUsers();
      setUsers(data);
    } catch (error) {
      setStatus({
        type: "error",
        message: error.response?.data?.message || "Failed to load resumes.",
      });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadUsers();
  }, []);

  const handleDelete = async (id) => {
    const confirmDelete = window.confirm("Delete this resume permanently?");
    if (!confirmDelete) return;

    try {
      await deleteUser(id);
      setStatus({ type: "success", message: "Resume deleted successfully." });
      await loadUsers();
    } catch (error) {
      setStatus({
        type: "error",
        message: error.response?.data?.message || "Failed to delete resume.",
      });
    }
  };

  return (
    <div className="space-y-6">
      <div className="grid gap-6 rounded-[32px] bg-slate-900 px-8 py-10 text-white shadow-soft lg:grid-cols-[1.4fr_0.9fr]">
        <div>
          <p className="mb-3 inline-flex rounded-full bg-white/10 px-3 py-1 text-xs uppercase tracking-[0.25em] text-slate-200">
            College Mini Project
          </p>
          <h1 className="text-4xl font-semibold leading-tight">
            Build, edit, preview, and download professional resumes in one place.
          </h1>
          <p className="mt-4 max-w-2xl text-sm text-slate-300">
            This dashboard keeps your saved resumes organized while the builder form handles every
            major section step by step.
          </p>
          <Link
            to="/resume/new"
            className="mt-6 inline-flex rounded-full bg-accent px-5 py-3 text-sm font-semibold text-white transition hover:opacity-90"
          >
            Create New Resume
          </Link>
        </div>
        <div className="grid gap-4 sm:grid-cols-3 lg:grid-cols-1">
          {[
            { label: "Frontend", value: "React + Tailwind" },
            { label: "Backend", value: "Spring Boot REST API" },
            { label: "Export", value: "Professional PDF" },
          ].map((item) => (
            <div key={item.label} className="rounded-3xl bg-white/10 p-5">
              <p className="text-xs uppercase tracking-[0.25em] text-slate-300">{item.label}</p>
              <p className="mt-3 text-lg font-semibold">{item.value}</p>
            </div>
          ))}
        </div>
      </div>

      <StatusBanner type={status.type} message={status.message} />

      <SectionCard title="Saved Resumes" subtitle="Manage all created resumes from this dashboard.">
        {loading ? (
          <LoadingSpinner label="Loading saved resumes..." />
        ) : users.length === 0 ? (
          <div className="rounded-2xl border border-dashed border-slate-300 bg-slate-50 px-4 py-6 text-sm text-slate-500">
            No resumes created yet. Start by creating your first resume.
          </div>
        ) : (
          <div className="grid gap-4">
            {users.map((user) => (
              <div
                key={user.id}
                className="flex flex-col justify-between gap-4 rounded-3xl border border-slate-200 bg-white p-5 md:flex-row md:items-center"
              >
                <div>
                  <h3 className="text-lg font-semibold text-slate-900">{user.fullName}</h3>
                  <p className="text-sm text-slate-500">{user.email}</p>
                  <p className="mt-1 text-sm text-slate-600">{user.phoneNumber}</p>
                </div>
                <div className="flex flex-wrap gap-2">
                  <Link
                    to={`/resume/preview/${user.id}`}
                    className="rounded-full bg-brand-100 px-4 py-2 text-sm font-medium text-brand-900"
                  >
                    View
                  </Link>
                  <Link
                    to={`/resume/edit/${user.id}`}
                    className="rounded-full bg-slate-100 px-4 py-2 text-sm font-medium text-slate-700"
                  >
                    Edit
                  </Link>
                  <Link
                    to={`/resume/pdf/${user.id}`}
                    className="rounded-full bg-emerald-100 px-4 py-2 text-sm font-medium text-emerald-700"
                  >
                    PDF
                  </Link>
                  <button
                    type="button"
                    onClick={() => handleDelete(user.id)}
                    className="rounded-full bg-red-100 px-4 py-2 text-sm font-medium text-red-600"
                  >
                    Delete
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </SectionCard>
    </div>
  );
}

export default DashboardPage;
