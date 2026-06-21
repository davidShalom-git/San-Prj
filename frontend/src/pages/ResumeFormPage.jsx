import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import ArraySection from "../components/ArraySection";
import FormInput from "../components/FormInput";
import LoadingSpinner from "../components/LoadingSpinner";
import SectionCard from "../components/SectionCard";
import StatusBanner from "../components/StatusBanner";
import TextAreaInput from "../components/TextAreaInput";
import { getResumeFormData, saveResume, tailorResumeWithAi } from "../services/resumeService";

const resumeTemplates = [
  {
    id: "modern",
    name: "Modern",
    description: "Balanced layout with clear cards and strong section spacing.",
  },
  {
    id: "classic",
    name: "Classic",
    description: "Traditional resume structure for formal applications.",
  },
  {
    id: "compact",
    name: "Compact",
    description: "Dense layout for candidates with more experience.",
  },
];

const createEmptyEducation = () => ({
  degree: "",
  collegeName: "",
  year: "",
  cgpaOrPercentage: "",
});

const createEmptySkill = () => ({
  skillName: "",
});

const createEmptyProject = () => ({
  projectTitle: "",
  description: "",
  technologiesUsed: "",
});

const createEmptyExperience = () => ({
  companyName: "",
  role: "",
  duration: "",
  description: "",
});

const createEmptyCertification = () => ({
  certificationName: "",
  organization: "",
  year: "",
});

const initialFormState = {
  fullName: "",
  email: "",
  phoneNumber: "",
  address: "",
  linkedInUrl: "",
  githubUrl: "",
  professionalSummary: "",
  resumeTemplate: "modern",
  educations: [createEmptyEducation()],
  skills: [createEmptySkill()],
  projects: [createEmptyProject()],
  experiences: [createEmptyExperience()],
  certifications: [createEmptyCertification()],
};

function ResumeFormPage() {
  const { userId } = useParams();
  const navigate = useNavigate();
  const [formData, setFormData] = useState(initialFormState);
  const [loading, setLoading] = useState(Boolean(userId));
  const [saving, setSaving] = useState(false);
  const [status, setStatus] = useState({ type: "info", message: "" });
  const [errors, setErrors] = useState({});
  const [aiPrompt, setAiPrompt] = useState("");
  const [aiGenerating, setAiGenerating] = useState(false);

  useEffect(() => {
    if (!userId) return;

    const loadResume = async () => {
      try {
        setLoading(true);
        const data = await getResumeFormData(userId);
        setFormData({
          fullName: data.fullName || "",
          email: data.email || "",
          phoneNumber: data.phoneNumber || "",
          address: data.address || "",
          linkedInUrl: data.linkedInUrl || "",
          githubUrl: data.githubUrl || "",
          professionalSummary: data.professionalSummary || "",
          resumeTemplate: data.resumeTemplate || "modern",
          educations: data.educations.length ? data.educations : [createEmptyEducation()],
          skills: data.skills.length ? data.skills : [createEmptySkill()],
          projects: data.projects.length ? data.projects : [createEmptyProject()],
          experiences: data.experiences.length ? data.experiences : [createEmptyExperience()],
          certifications: data.certifications.length
            ? data.certifications
            : [createEmptyCertification()],
        });
      } catch (error) {
        setStatus({
          type: "error",
          message: error.response?.data?.message || "Failed to load resume data.",
        });
      } finally {
        setLoading(false);
      }
    };

    loadResume();
  }, [userId]);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((previous) => ({ ...previous, [name]: value }));
  };

  const handleArrayChange = (sectionName, index, fieldName, value) => {
    setFormData((previous) => ({
      ...previous,
      [sectionName]: previous[sectionName].map((item, itemIndex) =>
        itemIndex === index ? { ...item, [fieldName]: value } : item
      ),
    }));
  };

  const addArrayItem = (sectionName, factory) => {
    setFormData((previous) => ({
      ...previous,
      [sectionName]: [...previous[sectionName], factory()],
    }));
  };

  const removeArrayItem = (sectionName, index) => {
    setFormData((previous) => {
      const nextItems = previous[sectionName].filter((_, itemIndex) => itemIndex !== index);
      return {
        ...previous,
        [sectionName]: nextItems.length ? nextItems : [initialFormState[sectionName][0]],
      };
    });
  };

  const applyAiSuggestion = async () => {
    const prompt = aiPrompt.trim();
    if (!prompt) {
      setStatus({ type: "error", message: "Tell AI the role, experience, or target job first." });
      return;
    }

    try {
      setAiGenerating(true);
      setStatus({ type: "info", message: "Generating resume suggestions with Gemini 2.5 Pro..." });
      const aiResume = await tailorResumeWithAi(prompt, formData);
      setFormData({
        ...initialFormState,
        ...aiResume,
        educations: aiResume.educations?.length ? aiResume.educations : [createEmptyEducation()],
        skills: aiResume.skills?.length ? aiResume.skills : [createEmptySkill()],
        projects: aiResume.projects?.length ? aiResume.projects : [createEmptyProject()],
        experiences: aiResume.experiences?.length ? aiResume.experiences : [createEmptyExperience()],
        certifications: aiResume.certifications?.length
          ? aiResume.certifications
          : [createEmptyCertification()],
        resumeTemplate: aiResume.resumeTemplate || formData.resumeTemplate || "modern",
      });
      setStatus({
        type: "success",
        message: "Gemini suggestions added. Review and edit them before saving.",
      });
    } catch (error) {
      setStatus({
        type: "error",
        message: error.response?.data?.message || "Failed to generate resume suggestions.",
      });
    } finally {
      setAiGenerating(false);
    }
  };

  const validate = () => {
    const nextErrors = {};
    if (!formData.fullName.trim()) nextErrors.fullName = "Full name is required.";
    if (!formData.email.trim()) nextErrors.email = "Email is required.";
    if (!formData.phoneNumber.trim()) nextErrors.phoneNumber = "Phone number is required.";
    if (!formData.address.trim()) nextErrors.address = "Address is required.";
    setErrors(nextErrors);
    return Object.keys(nextErrors).length === 0;
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (!validate()) {
      setStatus({ type: "error", message: "Please fill all required personal details." });
      return;
    }

    try {
      setSaving(true);
      setStatus({ type: "info", message: "" });
      const savedResume = await saveResume(formData, userId);
      setStatus({ type: "success", message: "Resume saved successfully." });
      navigate(`/resume/preview/${savedResume.user.id}`);
    } catch (error) {
      setStatus({
        type: "error",
        message: error.response?.data?.message || "Failed to save resume.",
      });
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return <LoadingSpinner label="Loading resume form..." />;
  }

  return (
    <form className="space-y-6" onSubmit={handleSubmit}>
      <div className="rounded-[32px] bg-white/85 p-8 shadow-soft">
        <p className="text-sm uppercase tracking-[0.25em] text-brand-500">
          {userId ? "Edit Resume" : "Create Resume"}
        </p>
        <h1 className="mt-2 text-3xl font-semibold text-slate-900">
          {userId ? "Update your resume details" : "Build your resume section by section"}
        </h1>
        <p className="mt-3 max-w-2xl text-sm text-slate-500">
          Use the optional AI helper for a quick draft, or skip it and fill the resume manually.
          You can preview and download the PDF after saving.
        </p>
      </div>

      <StatusBanner type={status.type} message={status.message} />

      <SectionCard
        title="AI Resume Helper (Optional)"
        subtitle="Skip this if you want to write everything manually. Use it only when you want Gemini to draft editable resume content."
      >
        <div className="space-y-4">
          <TextAreaInput
            label="Role Prompt"
            name="aiPrompt"
            value={aiPrompt}
            onChange={(event) => setAiPrompt(event.target.value)}
            placeholder="Example: I have 5 years in sales and want a resume for an account manager role."
          />
          <div className="flex flex-wrap items-center gap-3">
            <button
              type="button"
              onClick={applyAiSuggestion}
              disabled={aiGenerating}
              className="rounded-full bg-slate-900 px-5 py-3 text-sm font-semibold text-white transition hover:bg-slate-700 disabled:cursor-not-allowed disabled:opacity-70"
            >
              {aiGenerating ? "Generating..." : "Generate with Gemini 2.5 Pro"}
            </button>
            <span className="text-sm text-slate-500">
              Optional: generated content can be edited before saving.
            </span>
          </div>
        </div>
      </SectionCard>

      <SectionCard title="Personal Details" subtitle="Basic contact information for the resume.">
        <div className="grid gap-4 md:grid-cols-2">
          <FormInput
            label="Full Name"
            name="fullName"
            value={formData.fullName}
            onChange={handleChange}
            placeholder="Enter your full name"
            error={errors.fullName}
          />
          <FormInput
            label="Email"
            name="email"
            type="email"
            value={formData.email}
            onChange={handleChange}
            placeholder="Enter your email"
            error={errors.email}
          />
          <FormInput
            label="Phone Number"
            name="phoneNumber"
            value={formData.phoneNumber}
            onChange={handleChange}
            placeholder="Enter your phone number"
            error={errors.phoneNumber}
          />
          <FormInput
            label="Address"
            name="address"
            value={formData.address}
            onChange={handleChange}
            placeholder="Enter your address"
            error={errors.address}
          />
          <FormInput
            label="LinkedIn URL"
            name="linkedInUrl"
            value={formData.linkedInUrl}
            onChange={handleChange}
            placeholder="https://linkedin.com/in/yourname"
          />
          <FormInput
            label="GitHub URL"
            name="githubUrl"
            value={formData.githubUrl}
            onChange={handleChange}
            placeholder="https://github.com/yourname"
          />
          <div className="md:col-span-2">
            <TextAreaInput
              label="Professional Summary"
              name="professionalSummary"
              value={formData.professionalSummary}
              onChange={handleChange}
              placeholder="Write a short summary, or use the AI helper below."
            />
          </div>
        </div>
      </SectionCard>

      <SectionCard title="Resume Template" subtitle="Choose how the final resume should be presented.">
        <div className="grid gap-3 md:grid-cols-3">
          {resumeTemplates.map((template) => (
            <label
              key={template.id}
              className={`cursor-pointer rounded-2xl border p-4 transition ${
                formData.resumeTemplate === template.id
                  ? "border-brand-500 bg-brand-50 text-brand-900"
                  : "border-slate-200 bg-slate-50 text-slate-700 hover:border-slate-300"
              }`}
            >
              <input
                className="sr-only"
                type="radio"
                name="resumeTemplate"
                value={template.id}
                checked={formData.resumeTemplate === template.id}
                onChange={handleChange}
              />
              <span className="block text-sm font-semibold">{template.name}</span>
              <span className="mt-2 block text-sm">{template.description}</span>
            </label>
          ))}
        </div>
      </SectionCard>

      <SectionCard title="Education">
        <ArraySection
          title="Education"
          items={formData.educations}
          fields={[
            { label: "Degree", name: "degree", placeholder: "B.Tech, B.Sc, M.Tech" },
            { label: "College Name", name: "collegeName", placeholder: "College or University" },
            { label: "Year", name: "year", placeholder: "2026" },
            {
              label: "CGPA / Percentage",
              name: "cgpaOrPercentage",
              placeholder: "8.5 CGPA or 85%",
            },
          ]}
          onAdd={() => addArrayItem("educations", createEmptyEducation)}
          onRemove={(index) => removeArrayItem("educations", index)}
          onChange={(index, field, value) => handleArrayChange("educations", index, field, value)}
        />
      </SectionCard>

      <SectionCard title="Skills">
        <ArraySection
          title="Skill"
          items={formData.skills}
          fields={[{ label: "Skill Name", name: "skillName", placeholder: "React, Java, SQL" }]}
          onAdd={() => addArrayItem("skills", createEmptySkill)}
          onRemove={(index) => removeArrayItem("skills", index)}
          onChange={(index, field, value) => handleArrayChange("skills", index, field, value)}
        />
      </SectionCard>

      <SectionCard title="Projects">
        <ArraySection
          title="Project"
          items={formData.projects}
          fields={[
            { label: "Project Title", name: "projectTitle", placeholder: "Resume Builder" },
            {
              label: "Technologies Used",
              name: "technologiesUsed",
              placeholder: "React, Spring Boot, MySQL",
            },
            {
              label: "Description",
              name: "description",
              placeholder: "Describe your project and your contribution",
              type: "textarea",
            },
          ]}
          onAdd={() => addArrayItem("projects", createEmptyProject)}
          onRemove={(index) => removeArrayItem("projects", index)}
          onChange={(index, field, value) => handleArrayChange("projects", index, field, value)}
        />
      </SectionCard>

      <SectionCard title="Experience">
        <ArraySection
          title="Experience"
          items={formData.experiences}
          fields={[
            { label: "Company Name", name: "companyName", placeholder: "ABC Technologies" },
            { label: "Role", name: "role", placeholder: "Frontend Intern" },
            { label: "Duration", name: "duration", placeholder: "Jan 2025 - May 2025" },
            {
              label: "Description",
              name: "description",
              placeholder: "Explain your responsibilities and achievements",
              type: "textarea",
            },
          ]}
          onAdd={() => addArrayItem("experiences", createEmptyExperience)}
          onRemove={(index) => removeArrayItem("experiences", index)}
          onChange={(index, field, value) =>
            handleArrayChange("experiences", index, field, value)
          }
        />
      </SectionCard>

      <SectionCard title="Certifications">
        <ArraySection
          title="Certification"
          items={formData.certifications}
          fields={[
            {
              label: "Certification Name",
              name: "certificationName",
              placeholder: "Java Full Stack Development",
            },
            { label: "Organization", name: "organization", placeholder: "Coursera" },
            { label: "Year", name: "year", placeholder: "2026" },
          ]}
          onAdd={() => addArrayItem("certifications", createEmptyCertification)}
          onRemove={(index) => removeArrayItem("certifications", index)}
          onChange={(index, field, value) =>
            handleArrayChange("certifications", index, field, value)
          }
        />
      </SectionCard>

      <div className="flex flex-wrap items-center gap-3">
        <button
          type="submit"
          disabled={saving}
          className="rounded-full bg-brand-500 px-6 py-3 text-sm font-semibold text-white transition hover:bg-brand-600 disabled:cursor-not-allowed disabled:opacity-70"
        >
          {saving ? "Saving..." : "Save Resume"}
        </button>
        <button
          type="button"
          onClick={() => navigate("/")}
          className="rounded-full bg-slate-200 px-6 py-3 text-sm font-semibold text-slate-700"
        >
          Back to Dashboard
        </button>
      </div>
    </form>
  );
}

export default ResumeFormPage;
