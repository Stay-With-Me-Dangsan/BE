package stay.with.me.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stay.with.me.api.model.dto.ScrapDTO;
import stay.with.me.api.service.ScrapService;

import java.util.List;

@RestController
@RequestMapping("/api/scraps")
public class ScrapController {

    @Autowired
    private ScrapService scrapService;

    // 모든 스크랩 조회
    @GetMapping
    public List<ScrapDTO> getAllScraps() {
        return scrapService.getAllScraps();
    }

    // 특정 스크랩 조회
    @GetMapping("/{id}")
    public ScrapDTO getScrapById(@PathVariable Long id) {
        return scrapService.getScrapById(id);
    }

    // 스크랩 생성
    @PostMapping
    public void createScrap(@RequestBody ScrapDTO scrapDTO) {
        scrapService.createScrap(scrapDTO);
    }

    // 스크랩 삭제
    @DeleteMapping("/{id}")
    public void deleteScrap(@PathVariable Long id) {
        scrapService.deleteScrap(id);
    }
}
