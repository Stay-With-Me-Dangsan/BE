package stay.with.me.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import stay.with.me.api.model.dto.HouseDto;
import stay.with.me.api.service.HouseService;

import java.util.List;

@RestController
@RequestMapping("/house")
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;

    @PostMapping("/getDetail")
    public List<HouseDto> getDetail(@RequestBody HouseDto param) {
        return houseService.getDetail(param);
    }

//    @PostMapping("/getDetails")
//    public List<HouseDto> getDetails(@RequestBody List<HouseDto> params) {
//        return houseService.getDetails(params);
//    }

    @PostMapping("/createDetail")
    public void createDetail(@RequestBody HouseDto param) {
        houseService.createDetail(param);
    }

//    @PostMapping("/updateDetail")
//    public void updateDetail(@RequestParam HouseDto param) {
//        houseService.updateDetail(param);
//    }

//    @PostMapping("/deleteDetail")
//    public void deleteDetail(@RequestBody HouseDto param) {
//        houseService.deleteDetail(param);
//    }

}


