package stay.with.me.api.model.dto;

import lombok.Data;

@Data
public class LikeDTO {
    private Long likeID;
    private Long postID;
    private Integer userId;
    private String createAt;
}

