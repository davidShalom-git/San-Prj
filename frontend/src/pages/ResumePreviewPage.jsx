import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import LoadingSpinner from "../components/LoadingSpinner";
import StatusBanner from "../components/StatusBanner";
import { getResumeByUserId } from "../services/resumeService";

function ResumePreviewPage() {
  const { userId } = useParams();
  const [resume, setResume] = useState(null);
  const [loading, setLoading] = useState(true);
  const [status, setStatus] = useState({ type: "info", message: "" });

  useEffect(() => {
    const loadResume = async () => {
      try {
        setLoading(true);
        const data = await getResumeByUserId(userId);
        setResume(data);
      } catch (error) {
        setStatus({
          type: "error",
          message: error.response?.data?.message || "Failed to load resume preview.",
        });
      } finally {
        setLoading(false);
      }
    };

    loadResume();
  }, [userId]);

  if (loading) {
    return <LoadingSpinner label="Loading resume preview..." />;
  }

  if (!resume) {
    return <StatusBanner type={status.type} message={status.message || "Resume not found."} />;
  }

  const { user, educations, skills, projects, experiences, certifications } = resume;
  const selectedTemplate = user.resumeTemplate || "modern";
  const isClassic = selectedTemplate === "classic";
  const isCompact = selectedTemplate === "compact";
  const previewShell = isClassic
    ? "border border-slate-300 bg-white p-8 shadow-sm"
    : isCompact
      ? "border border-slate-200 bg-white p-5 shadow-soft"
      : "bg-white/85 p-6 shadow-soft";
  const previewRadius = isClassic ? "rounded-lg" : "rounded-[28px]";
  const itemRadius = isClassic ? "rounded-md" : "rounded-2xl";
  const sectionGap = isCompact ? "space-y-4" : "space-y-6";

  return (
    <div className="space-y-6">
      <div className="flex flex-wrap items-center justify-between gap-3">
        <div>
          <p className="text-sm uppercase tracking-[0.25em] text-brand-500">Resume Preview</p>
          <h1 className="mt-2 text-3xl font-semibold text-slate-900">{user.fullName}</h1>
        </div>
        <div className="flex flex-wrap gap-2">
          <Link
            to={`/resume/edit/${user.id}`}
            className="rounded-full bg-slate-200 px-5 py-3 text-sm font-medium text-slate-700"
          >
            Edit Resume
          </Link>
          <a
            href={`http://localhost:8080/api/resume/${user.id}/pdf`}
            download={`resume-${user.id}.pdf`}
            className="rounded-full bg-brand-500 px-5 py-3 text-sm font-medium text-white"
          >
            Download PDF
          </a>
        </div>
      </div>

      <div className={`${previewShell} ${previewRadius}`}>
        <div className={sectionGap}>
          <div className={isClassic ? "border-b border-slate-300 pb-4 text-center" : "border-b border-slate-200 pb-4"}>
            <h2 className={isCompact ? "text-2xl font-semibold text-slate-950" : "text-4xl font-semibold text-slate-950"}>
              {user.fullName}
            </h2>
            <p className="mt-2 text-sm text-slate-600">
              {user.email} | {user.phoneNumber} | {user.address}
            </p>
            <p className="mt-1 text-sm text-slate-500">
              LinkedIn: {user.linkedInUrl || "N/A"} | GitHub: {user.githubUrl || "N/A"}
            </p>
          </div>

          {user.professionalSummary ? (
            <div>
              <h3 className="text-sm font-bold uppercase tracking-[0.18em] text-slate-500">Summary</h3>
              <p className="mt-2 text-sm leading-6 text-slate-700">{user.professionalSummary}</p>
            </div>
          ) : null}

          <div>
            <h3 className="text-sm font-bold uppercase tracking-[0.18em] text-slate-500">Skills</h3>
            <div className="mt-3 flex flex-wrap gap-2">
              {skills.map((skill) => (
                <span
                  key={skill.id}
                  className={isClassic ? "text-sm text-slate-700" : "rounded-full bg-brand-100 px-3 py-1.5 text-sm font-medium text-brand-900"}
                >
                  {skill.skillName}
                </span>
              ))}
            </div>
          </div>

          <div>
            <h3 className="text-sm font-bold uppercase tracking-[0.18em] text-slate-500">Experience</h3>
            <div className="mt-3 grid gap-3">
              {experiences.map((item) => (
                <div key={item.id} className={`${itemRadius} border border-slate-200 p-4`}>
                  <h4 className="font-semibold text-slate-900">
                    {item.role} - {item.companyName}
                  </h4>
                  <p className="mt-1 text-sm text-slate-500">{item.duration}</p>
                  <p className="mt-2 text-sm text-slate-600">{item.description}</p>
                </div>
              ))}
            </div>
          </div>

          <div>
            <h3 className="text-sm font-bold uppercase tracking-[0.18em] text-slate-500">Projects</h3>
            <div className="mt-3 grid gap-3">
              {projects.map((item) => (
                <div key={item.id} className={`${itemRadius} border border-slate-200 p-4`}>
                  <h4 className="font-semibold text-slate-900">{item.projectTitle}</h4>
                  <p className="mt-2 text-sm text-slate-600">{item.description}</p>
                  <p className="mt-2 text-sm font-medium text-brand-600">
                    Technologies: {item.technologiesUsed}
                  </p>
                </div>
              ))}
            </div>
          </div>

          <div>
            <h3 className="text-sm font-bold uppercase tracking-[0.18em] text-slate-500">Education</h3>
            <div className="mt-3 grid gap-3">
              {educations.map((item) => (
                <div key={item.id} className={`${itemRadius} border border-slate-200 p-4`}>
                  <h4 className="font-semibold text-slate-900">{item.degree}</h4>
                  <p className="text-sm text-slate-600">{item.collegeName}</p>
                  <p className="mt-1 text-sm text-slate-500">
                    {item.year} | {item.cgpaOrPercentage}
                  </p>
                </div>
              ))}
            </div>
          </div>

          <div>
            <h3 className="text-sm font-bold uppercase tracking-[0.18em] text-slate-500">Certifications</h3>
            <div className="mt-3 grid gap-3 md:grid-cols-2">
              {certifications.map((item) => (
                <div key={item.id} className={`${itemRadius} border border-slate-200 p-4`}>
                  <h4 className="font-semibold text-slate-900">{item.certificationName}</h4>
                  <p className="text-sm text-slate-600">{item.organization}</p>
                  <p className="mt-1 text-sm text-slate-500">{item.year}</p>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ResumePreviewPage;
