package stay.with.me.common.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import stay.with.me.api.model.dto.ResponseDto;


import java.util.Map;

public class ResponseUtil {
    public static ResponseEntity<ResponseDto> buildResponse(int status, String message, Map<String, Object> data, HttpStatus httpStatus) {
        ResponseDto res = new ResponseDto();
        res.setStatus(status);
        res.setMessage(message);
        res.setData(data);
        return new ResponseEntity<>(res, httpStatus);
    }
}
