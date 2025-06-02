package stay.with.me.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {
    private Long reportId;
    private Integer userId;
    private String reportedReason;
    private String reportedDetails;
    private String status;
    private String content;
    private String createdAt;
}
