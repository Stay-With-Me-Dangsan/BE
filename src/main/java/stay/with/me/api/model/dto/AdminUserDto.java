package stay.with.me.api.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminUserDto {
    private Long userId;
    private String email;
    private String password;
    private String nickname;
    private String birth;
    private LocalDate createdDate;
    private Long postId;
    private String boardType;
    private String title;
    private String content;
    private String createdAt;
    private String updatedAt;
    private Integer views;
    private Integer status;
    private Integer likeCount;
    private Integer commentCount;
    private Long commentId;
    private String comment;
}
