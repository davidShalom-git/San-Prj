import { Route, Routes } from "react-router-dom";
import AppLayout from "./layouts/AppLayout";
import DashboardPage from "./pages/DashboardPage";
import ResumeFormPage from "./pages/ResumeFormPage";
import ResumePreviewPage from "./pages/ResumePreviewPage";
import PdfDownloadPage from "./pages/PdfDownloadPage";

function App() {
  return (
    <Routes>
      <Route path="/" element={<AppLayout />}>
        <Route index element={<DashboardPage />} />
        <Route path="resume/new" element={<ResumeFormPage />} />
        <Route path="resume/edit/:userId" element={<ResumeFormPage />} />
        <Route path="resume/preview/:userId" element={<ResumePreviewPage />} />
        <Route path="resume/pdf/:userId" element={<PdfDownloadPage />} />
      </Route>
    </Routes>
  );
}

export default App;
