package stay.with.me.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stay.with.me.api.model.dto.ReportDTO;
import stay.with.me.api.service.ReportService;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    public List<ReportDTO> getAllReports() {
        return reportService.getAllReports();
    }

    @GetMapping("/{id}")
    public ReportDTO getReportById(@PathVariable Long id) {
        return reportService.getReportById(id);
    }

    @PostMapping
    public void createReport(@RequestBody ReportDTO reportDTO) {
        reportService.createReport(reportDTO);
    }

    @PutMapping("/{id}")
    public void updateReport(@PathVariable Long id, @RequestBody ReportDTO reportDTO) {
        reportService.updateReport(id, reportDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
    }
}
