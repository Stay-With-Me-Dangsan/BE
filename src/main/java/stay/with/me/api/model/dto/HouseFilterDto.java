package stay.with.me.api.model.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseFilterDto {
    private Double minX;
    private Double maxX;
    private Double minY;
    private Double maxY;
    private Integer precision; // 줌레벨에 따른 좌표 라운딩 정밀도

    private List<Integer> depositRange;
    private List<Integer> monthlyRentRange; // ex) [0, 4]
    private List<String> spaceType; // ex) ["원룸", "오피스텔"]
    private String genderType;

    private LocalDate shareStartDate;
    private LocalDate shareEndDate;
//    private ContractPeriod contractPeriod;
//
//    public static class ContractPeriod {
//        private String minYear;
//        private String minMonth;
//        private String maxYear;
//        private String maxMonth;
//    }
}
