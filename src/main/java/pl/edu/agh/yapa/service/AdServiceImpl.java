package pl.edu.agh.yapa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.yapa.model.Ad;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.model.AdType;
import pl.edu.agh.yapa.persistence.AdTemplateDao;
import pl.edu.agh.yapa.persistence.AdTypeDao;
import pl.edu.agh.yapa.persistence.AdDao;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;

import java.util.List;

/**
 * @author Piotr Góralczyk
 */
@Service
public class AdServiceImpl implements AdService {
    private final AdDao adDao;
    private final AdTypeDao adTypeDao;
    private final AdTemplateDao adTemplateDao;

    @Autowired
    public AdServiceImpl(AdDao adDao, AdTypeDao adTypeDao, AdTemplateDao adTemplateDao) {
        this.adDao = adDao;
        this.adTypeDao = adTypeDao;
        this.adTemplateDao = adTemplateDao;
    }

    @Override
    public List<Ad> getAds() throws InvalidDatabaseStateException {
        return adDao.getAds();
    }

    @Override
    public List<AdType> getAdTypes() throws InvalidDatabaseStateException {
        return adTypeDao.getTypes();
    }

    @Override
    public List<AdTemplate> getAdTemplates() throws InvalidDatabaseStateException {
        return adTemplateDao.getTemplates();
    }

    @Override
    public void insertAdType(AdType adType) throws InvalidDatabaseStateException {
        adTypeDao.insertType(adType);
    }

    @Override
    public void insertAdTemplate(AdTemplate adTemplate) throws InvalidDatabaseStateException {
        adTemplateDao.insertTemplate(adTemplate);
    }

    @Override
    public void removeAdType(String typeName) {
        adTypeDao.removeTypeByName(typeName);
    }
}
