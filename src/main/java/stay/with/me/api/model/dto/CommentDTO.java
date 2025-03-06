package stay.with.me.api.model.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private Long commentId;
    private Long postId;
    private Integer userId;
    private String content;
    private String createdAt;
    private String updatedAt;
}
