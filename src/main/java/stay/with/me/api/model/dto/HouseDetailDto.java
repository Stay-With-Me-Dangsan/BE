package stay.with.me.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.postgresql.geometric.PGpoint;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseDetailDto {

    private int houseMainId;
    private int houseDetailId;
    private String houseDetailAddress;
    private String houseDescription;
    private String housePhone;
    private String[] houseKeyword;
    private String[] houseFeatures;
    private String propertyType;
    private BigInteger houseDeposit;
    private BigInteger houseRent;
    private LocalDate shareStartDate;
    private LocalDate shareEndDate;
    private PGpoint coordinates;
    private String createdBy;
    private int floor;
    private BigDecimal maintenance;
    private int management;
    private String houseFilePath;
    private String elevator;
    private String parking;
    private Boolean liked;
    private String createdAt;
}
