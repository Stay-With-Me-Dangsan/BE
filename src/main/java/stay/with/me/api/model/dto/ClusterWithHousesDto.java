package stay.with.me.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClusterWithHousesDto {
    private BigDecimal lat;
    private BigDecimal lng;
    private Long count;
    private List<HouseDetailDto> houses;
}
