package stay.with.me.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import stay.with.me.api.service.RedisService;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisController {

    private final RedisService redisService;

    @PostMapping("/save")
    public String saveData(@RequestParam String key, @RequestParam String value) {
        redisService.saveData(key, value, 10);  // 10분 동안 저장
        return "Data saved!";
    }

    @GetMapping("/get")
    public String getData(@RequestParam String key) {
        String value = redisService.getData(key);
        return value != null ? "Value: " + value : "No data found";
    }

    @DeleteMapping("/delete")
    public String deleteData(@RequestParam String key) {
        redisService.deleteData(key);
        return "Data deleted!";
    }
}
