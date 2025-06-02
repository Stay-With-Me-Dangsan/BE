package stay.with.me.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stay.with.me.api.model.dto.*;
import stay.with.me.api.service.HouseService;
import stay.with.me.common.util.ResponseUtil;
import stay.with.me.common.ResponseStatus;

import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/house")
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;

    @GetMapping("/getMain")
    public ResponseEntity<ResponseDto> getMain(@RequestParam("houseMainId") int houseMainId) {
        try {
            HouseMainDto dto = houseService.getMain(houseMainId);

            if(dto == null) {
                return ResponseUtil.buildResponse(ResponseStatus.NOT_FOUND.getCode(), ResponseStatus.NOT_FOUND.getMessage(), null, HttpStatus.NOT_FOUND);
            }
            Map<String, Object> data = Map.of("result", dto);
            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);
        } catch(Exception e) {
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getDetail")
    public ResponseEntity<ResponseDto> getDetail(@RequestParam("houseDetailId") int houseDetailId) {
        try {
            HouseDetailDto dto = houseService.getDetail(houseDetailId);

            if(dto == null) {
                return ResponseUtil.buildResponse(ResponseStatus.NOT_FOUND.getCode(), ResponseStatus.NOT_FOUND.getMessage(), null, HttpStatus.NOT_FOUND);
            }
            Map<String, Object> data = Map.of("result", dto);
            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);
        } catch(Exception e) {
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getDetails")
    public ResponseEntity<ResponseDto> getDetails(@RequestParam("minX") double minX, @RequestParam("minY") double  minY, @RequestParam("maxX") double  maxX, @RequestParam("maxY") double  maxY) {
        try {
            List<HouseDetailDto> list = houseService.getDetails(minX, minY, maxX, maxY);

            if(list.size() < 1) {
                return ResponseUtil.buildResponse(ResponseStatus.NOT_FOUND.getCode(), ResponseStatus.NOT_FOUND.getMessage(), null, HttpStatus.NOT_FOUND);
            }
            Map<String, Object> data = Map.of("result", list);
            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);
        } catch(Exception e) {
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/main/clustered")
    public ResponseEntity<ResponseDto> getMainClusteredHouses() throws Exception {
        try {
            List<ClusterWithHousesDto> list = houseService.getMainClusteredHouses();

            if(list.size() < 1) {
                return ResponseUtil.buildResponse(ResponseStatus.NOT_FOUND.getCode(), ResponseStatus.NOT_FOUND.getMessage(), null, HttpStatus.NOT_FOUND);
            }
            Map<String, Object> data = Map.of("result", list);
            System.out.println("메인지도: " +data);
            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);
        } catch(Exception e) {
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/clustered")
    public ResponseEntity<ResponseDto> getClusteredHouses(@RequestParam("minX") double minX, @RequestParam("minY") double  minY, @RequestParam("maxX") double  maxX, @RequestParam("maxY") double  maxY, @RequestParam("precision") int precision) throws Exception {
        try {
            List<ClusterWithHousesDto> list = houseService.getClusteredHouses(minX, minY, maxX, maxY, precision);

            if(list.size() < 1) {
                return ResponseUtil.buildResponse(ResponseStatus.NOT_FOUND.getCode(), ResponseStatus.NOT_FOUND.getMessage(), null, HttpStatus.NOT_FOUND);
            }
            Map<String, Object> data = Map.of("result", list);

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);
        } catch(Exception e) {
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/postDetailsByCondition")
    public ResponseEntity<ResponseDto> getDetailsByCondition(@RequestBody HouseFilterDto param) {
        try {

            List<ClusterWithHousesDto> list = houseService.getDetailsByCondition(param);

            Map<String, Object> data = Map.of("result", list);
            if (list.isEmpty()) {
                return ResponseUtil.buildResponse( ResponseStatus.SUCCESS.getCode(),"해당 조건에 맞는 결과가 없습니다.",data,HttpStatus.OK);}

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);

        } catch(Exception e) {
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createMain")
    public ResponseEntity<ResponseDto> createMain(@RequestBody HouseMainDto param) {
        try {
            int createdRow = houseService.createMain(param);
            if (createdRow != 1) {
                return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Map<String, Object> data = Map.of("cnt", createdRow);
            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createDetail")
    public ResponseEntity<ResponseDto> createDetail(@RequestBody HouseDetailDto param) {
        try {
            int createdRow = houseService.createDetail(param);
            if (createdRow != 1) {
                return ResponseUtil.buildResponse(ResponseStatus.NOT_FOUND.getCode(), ResponseStatus.NOT_FOUND.getMessage(), null, HttpStatus.NOT_FOUND);
            }
            Map<String, Object> data = Map.of("cnt", createdRow);
            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/updateDetail")
    public ResponseEntity<ResponseDto> updateDetail(@RequestBody HouseDetailDto param) {
        try {
            int updatedRow = houseService.updateDetail(param);
            Map<String, Object> data = Map.of("cnt", updatedRow);
            if (updatedRow != 1) {
                return ResponseUtil.buildResponse(ResponseStatus.NOT_FOUND.getCode(), ResponseStatus.NOT_FOUND.getMessage(), data, HttpStatus.NOT_FOUND);
            }
            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteDetail")
    public ResponseEntity<ResponseDto> deleteDetail(@RequestParam("houseDetailId") int houseDetailId) {
        try {
            boolean isDeleted = houseService.deleteDetail(houseDetailId);
            if(!isDeleted) {
                return ResponseUtil.buildResponse(ResponseStatus.NOT_FOUND.getCode(), ResponseStatus.NOT_FOUND.getMessage(), null, HttpStatus.NOT_FOUND);
            }
            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), null, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}


