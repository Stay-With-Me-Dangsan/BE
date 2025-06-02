package stay.with.me.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.ReportDTO;
import stay.with.me.api.model.mapper.ReportMapper;
import stay.with.me.api.service.ReportService;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Override
    public List<ReportDTO> getAllReports() {
        return reportMapper.findAll();
    }

    @Override
    public ReportDTO getReportById(Long id) {
        return reportMapper.findById(id);
    }

    @Override
    public void createReport(ReportDTO reportDTO) {
        reportMapper.insert(reportDTO);
    }

    @Override
    public void updateReport(Long id, ReportDTO reportDTO) {
        reportMapper.update(id, reportDTO);
    }

    @Override
    public void deleteReport(Long id) {
        reportMapper.delete(id);
    }
}
