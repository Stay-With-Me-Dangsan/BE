package stay.with.me.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseReviewDto {

    private int reviewId;
    private int houseDetailId;
    private int star;
    private String reviewDetail;
    private String createdBy;

}
