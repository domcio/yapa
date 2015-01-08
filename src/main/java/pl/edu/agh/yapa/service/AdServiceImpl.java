package pl.edu.agh.yapa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.yapa.model.Ad;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.model.AdType;
import pl.edu.agh.yapa.persistence.AdTemplateDao;
import pl.edu.agh.yapa.persistence.AdTypeDao;
import pl.edu.agh.yapa.persistence.AdsDao;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;

import java.util.List;

/**
 * @author Piotr Góralczyk
 */
@Service
public class AdServiceImpl implements AdService {
    private final AdsDao adsDao;
    private final AdTypeDao adTypeDao;
    private final AdTemplateDao adTemplateDao;

    @Autowired
    public AdServiceImpl(AdsDao adsDao, AdTypeDao adTypeDao, AdTemplateDao adTemplateDao) {
        this.adsDao = adsDao;
        this.adTypeDao = adTypeDao;
        this.adTemplateDao = adTemplateDao;
    }

    @Override
    public List<Ad> getAds() throws InvalidDatabaseStateException {
        return adsDao.getAds();
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
