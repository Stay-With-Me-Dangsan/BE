package stay.with.me.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ResponseDto {

    private String status;
    private String message;
    private Map<String, Object> data;

}
