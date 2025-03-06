package stay.with.me.api.service;

import stay.with.me.api.model.dto.ReportDTO;

import java.util.List;

public interface ReportService {
    List<ReportDTO> getAllReports();
    ReportDTO getReportById(Long id);
    void createReport(ReportDTO reportDTO);
    void updateReport(Long id, ReportDTO reportDTO);
    void deleteReport(Long id);
}
