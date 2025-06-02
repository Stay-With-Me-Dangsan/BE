package stay.with.me.api.model.dto;

import lombok.Data;

@Data
public class ScrapDTO {
    private Long scrapId;
    private Long postId;
    private Integer userId;
    private String createdAt;
}
