package stay.with.me.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseMainDto {

    private int houseMainId;
    private String houseAddress;
    private int houseZonecode;
    private String houseName;

}
