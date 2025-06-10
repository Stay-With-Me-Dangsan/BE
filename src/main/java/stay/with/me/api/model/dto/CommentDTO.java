package stay.with.me.api.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class CommentDTO {
    private Long postId;
    private Long userId;
    private Long commentId;
    private String nickname;
    private String boardType;
    private String title;
    private String content;
    private String createdAt;
    private String updatedAt;
    private Integer views;
    private Integer status;
    private Integer type;
    private Integer commentCount;
    private Integer likeCount;
    private Integer viewCount;
    private List<String> imageUrls;
    private Boolean liked;
}
