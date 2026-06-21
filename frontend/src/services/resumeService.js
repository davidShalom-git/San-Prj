import api from "./api";
import { createUser, getUserById, updateUser } from "./userService";

const sectionConfig = [
  {
    key: "educations",
    endpoint: "/education",
    userEndpoint: "/education/user",
    fields: ["degree", "collegeName", "year", "cgpaOrPercentage"],
  },
  {
    key: "skills",
    endpoint: "/skills",
    userEndpoint: "/skills/user",
    fields: ["skillName"],
  },
  {
    key: "projects",
    endpoint: "/projects",
    userEndpoint: "/projects/user",
    fields: ["projectTitle", "description", "technologiesUsed"],
  },
  {
    key: "experiences",
    endpoint: "/experience",
    userEndpoint: "/experience/user",
    fields: ["companyName", "role", "duration", "description"],
  },
  {
    key: "certifications",
    endpoint: "/certifications",
    userEndpoint: "/certifications/user",
    fields: ["certificationName", "organization", "year"],
  },
];

export const getResumeByUserId = async (userId) => {
  const response = await api.get(`/resume/${userId}`);
  return response.data;
};

export const tailorResumeWithAi = async (prompt, resume) => {
  const response = await api.post("/ai/resume-tailor", { prompt, resume });
  return response.data;
};

export const downloadResumePdf = async (userId) => {
  const response = await api.get(`/resume/${userId}/pdf`, { responseType: "blob" });
  return response.data;
};

const fetchSectionItems = async (userId, userEndpoint) => {
  const response = await api.get(`${userEndpoint}/${userId}`);
  return response.data;
};

const createSectionItems = async (endpoint, items, userId) => {
  for (const item of items) {
    await api.post(endpoint, { ...item, userId });
  }
};

const deleteSectionItems = async (endpoint, items) => {
  for (const item of items) {
    await api.delete(`${endpoint}/${item.id}`);
  }
};

const pickFields = (item, fields) =>
  fields.reduce((result, field) => {
    result[field] = item[field] ?? "";
    return result;
  }, {});

export const saveResume = async (formData, userId) => {
  const userPayload = {
    fullName: formData.fullName,
    email: formData.email,
    phoneNumber: formData.phoneNumber,
    address: formData.address,
    linkedInUrl: formData.linkedInUrl,
    githubUrl: formData.githubUrl,
    professionalSummary: formData.professionalSummary,
    resumeTemplate: formData.resumeTemplate,
  };

  const savedUser = userId
    ? await updateUser(userId, userPayload)
    : await createUser(userPayload);

  for (const section of sectionConfig) {
    const currentItems = userId ? await fetchSectionItems(savedUser.id, section.userEndpoint) : [];
    if (currentItems.length > 0) {
      await deleteSectionItems(section.endpoint, currentItems);
    }

    const nextItems = formData[section.key]
      .map((item) => pickFields(item, section.fields))
      .filter((item) => Object.values(item).some((value) => String(value).trim() !== ""));

    await createSectionItems(section.endpoint, nextItems, savedUser.id);
  }

  return getResumeByUserId(savedUser.id);
};

export const getResumeFormData = async (userId) => {
  const resume = await getResumeByUserId(userId);
  const user = await getUserById(userId);

  return {
    ...user,
    educations: resume.educations.map((item) =>
      pickFields(item, ["degree", "collegeName", "year", "cgpaOrPercentage"])
    ),
    skills: resume.skills.map((item) => pickFields(item, ["skillName"])),
    projects: resume.projects.map((item) =>
      pickFields(item, ["projectTitle", "description", "technologiesUsed"])
    ),
    experiences: resume.experiences.map((item) =>
      pickFields(item, ["companyName", "role", "duration", "description"])
    ),
    certifications: resume.certifications.map((item) =>
      pickFields(item, ["certificationName", "organization", "year"])
    ),
  };
};
