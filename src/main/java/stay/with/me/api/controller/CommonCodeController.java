package stay.with.me.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stay.with.me.api.model.dto.CommonCodeDto;
import stay.with.me.api.model.dto.ResponseDto;
import stay.with.me.api.service.CommonCodeService;
import stay.with.me.common.ResponseStatus;
import stay.with.me.common.util.ResponseUtil;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/commonCode")
@RequiredArgsConstructor
public class CommonCodeController {

    private final CommonCodeService commonCodeService;

    @GetMapping("/get")
    public ResponseEntity<ResponseDto> get(@RequestParam("groupId") String groupId, @RequestParam("key") String key) {
        try {
            List<CommonCodeDto> dto = commonCodeService.get(groupId, key);
            if(dto == null) {
                return ResponseUtil.buildResponse(ResponseStatus.NOT_FOUND.getCode(), ResponseStatus.NOT_FOUND.getMessage(), null, HttpStatus.NOT_FOUND);
            }
            Map<String, Object> data = Map.of("result", dto);
            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);
        } catch(Exception e) {
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> create(@RequestBody CommonCodeDto param) {
        try {
            int createdRow = commonCodeService.create(param);
            if (createdRow != 1) {
                return ResponseUtil.buildResponse(ResponseStatus.NOT_FOUND.getCode(), ResponseStatus.NOT_FOUND.getMessage(), null, HttpStatus.NOT_FOUND);
            }
            Map<String, Object> data = Map.of("cnt", createdRow);
            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<ResponseDto> update(@RequestBody CommonCodeDto param) {
        try {
            int updatedRow = commonCodeService.update(param);
            Map<String, Object> data = Map.of("cnt", updatedRow);
            if (updatedRow != 1) {
                return ResponseUtil.buildResponse(ResponseStatus.NOT_FOUND.getCode(), ResponseStatus.NOT_FOUND.getMessage(), data, HttpStatus.NOT_FOUND);
            }
            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> delete(@RequestParam("commonCodeId") int commonCodeId) {
        try {
            boolean isDeleted = commonCodeService.delete(commonCodeId);
            if(!isDeleted) {
                return ResponseUtil.buildResponse(ResponseStatus.NOT_FOUND.getCode(), ResponseStatus.NOT_FOUND.getMessage(), null, HttpStatus.NOT_FOUND);
            }
            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), null, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}


