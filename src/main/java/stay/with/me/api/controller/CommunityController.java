package stay.with.me.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import stay.with.me.api.model.dto.CommunityDto;
import stay.with.me.api.service.CommunityService;
import stay.with.me.api.service.RedisService;

import java.util.List;

@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController {

    private final RedisService redisService;
    private final CommunityService communityService;

    @GetMapping("/{district}")
    public List<CommunityDto> getChat(@PathVariable String district) throws Exception {
        return communityService.getChat(district);
    }

    @PostMapping("/save/{district}")
    public String saveChat(@PathVariable String district, @RequestBody CommunityDto chat) {
        redisService.saveChat(chat, district);
        return "Data saved!";
    }
}
