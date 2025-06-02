package stay.with.me.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.ScrapDTO;
import stay.with.me.api.model.mapper.ScrapMapper;
import stay.with.me.api.service.ScrapService;

import java.util.List;

@Service
public class ScrapServiceImpl implements ScrapService {

    @Autowired
    private ScrapMapper scrapMapper;

    @Override
    public List<ScrapDTO> getAllScraps() {
        return scrapMapper.findAll();
    }

    @Override
    public ScrapDTO getScrapById(Long id) {
        return scrapMapper.findById(id);
    }

    @Override
    public void createScrap(ScrapDTO scrapDTO) {
        scrapMapper.insert(scrapDTO);
    }

    @Override
    public void deleteScrap(Long id) {
        scrapMapper.delete(id);
    }
}
