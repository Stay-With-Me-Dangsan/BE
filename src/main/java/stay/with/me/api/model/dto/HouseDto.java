package stay.with.me.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.postgresql.geometric.PGpoint;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseDto {

    private int houseMainId;
    private int houseDetailId;
    private String houseDetailAddress;
    private String houseDescription;
    private String housePhone;
    private String[] houseKeyword;
    private String[] houseFeatures;
    private String propertyType;
    private int houseDeposit;
    private int houseRent;
    private Date shareStartDate;
    private Date shareEndDate;
    private PGpoint coordinates;
    private String createdBy;

}
