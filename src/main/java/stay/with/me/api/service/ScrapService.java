package stay.with.me.api.service;

import stay.with.me.api.model.dto.ScrapDTO;

import java.util.List;

public interface ScrapService {
    List<ScrapDTO> getAllScraps();
    ScrapDTO getScrapById(Long id);
    void createScrap(ScrapDTO scrapDTO);
    void deleteScrap(Long id);
}
