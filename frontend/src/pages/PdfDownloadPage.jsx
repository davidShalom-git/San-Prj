import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import LoadingSpinner from "../components/LoadingSpinner";
import SectionCard from "../components/SectionCard";
import StatusBanner from "../components/StatusBanner";
import { downloadResumePdf } from "../services/resumeService";

function PdfDownloadPage() {
  const { userId } = useParams();
  const [downloading, setDownloading] = useState(true);
  const [status, setStatus] = useState({ type: "info", message: "Preparing PDF download..." });
  const pdfUrl = `http://localhost:8080/api/resume/${userId}/pdf`;

  useEffect(() => {
    const handleDownload = async () => {
      try {
        setDownloading(true);
        const pdfBlob = await downloadResumePdf(userId);
        const url = window.URL.createObjectURL(new Blob([pdfBlob], { type: "application/pdf" }));
        const link = document.createElement("a");
        link.href = url;
        link.download = `resume-${userId}.pdf`;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.setTimeout(() => window.URL.revokeObjectURL(url), 1000);
        setStatus({ type: "success", message: "PDF downloaded successfully." });
      } catch (error) {
        setStatus({
          type: "error",
          message: error.response?.data?.message || "Failed to download PDF.",
        });
      } finally {
        setDownloading(false);
      }
    };

    handleDownload();
  }, [userId]);

  return (
    <SectionCard title="PDF Download" subtitle="Your professional resume PDF is being prepared.">
      <div className="space-y-4">
        {downloading ? <LoadingSpinner label="Downloading resume PDF..." /> : null}
        <StatusBanner type={status.type} message={status.message} />
        <div className="flex flex-wrap gap-3">
          <a
            href={pdfUrl}
            download={`resume-${userId}.pdf`}
            className="rounded-full bg-slate-900 px-5 py-3 text-sm font-medium text-white"
          >
            Download PDF
          </a>
          <Link
            to={`/resume/preview/${userId}`}
            className="rounded-full bg-brand-500 px-5 py-3 text-sm font-medium text-white"
          >
            Back to Preview
          </Link>
          <Link
            to="/"
            className="rounded-full bg-slate-200 px-5 py-3 text-sm font-medium text-slate-700"
          >
            Dashboard
          </Link>
        </div>
      </div>
    </SectionCard>
  );
}

export default PdfDownloadPage;
