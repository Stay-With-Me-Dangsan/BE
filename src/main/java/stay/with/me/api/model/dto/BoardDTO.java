package stay.with.me.api.model.dto;

import lombok.Data;

@Data
public class BoardDTO {
    private Long postId;
    private Integer userId;
    private String boardType;
    private String title;
    private String content;
    private String createdAt;
    private String updatedAt;
    private Integer views;
    private Integer status;
    private Integer type;
}
