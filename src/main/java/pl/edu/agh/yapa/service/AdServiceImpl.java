package pl.edu.agh.yapa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.yapa.conversion.FieldsContainer;
import pl.edu.agh.yapa.model.Ad;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.model.AdType;
import pl.edu.agh.yapa.persistence.AdsDao;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;

import java.util.List;

/**
 * @author Piotr Góralczyk
 */
@Service
public class AdServiceImpl implements AdService {
    private final AdsDao adsDao;

    @Autowired
    public AdServiceImpl(AdsDao adsDao) {
        this.adsDao = adsDao;
    }

    @Override
    public List<Ad> getAds() throws InvalidDatabaseStateException { return adsDao.getAds(); }
    @Override
    public List<Ad> getAdsByType(String adTypeName) throws InvalidDatabaseStateException {
        return adsDao.getAdsByType(adTypeName);
    }
    @Override
    public List<AdType> getAdTypes() throws InvalidDatabaseStateException { return adsDao.getTypes(); }
    @Override
    public List<AdTemplate> getTemplates() throws  InvalidDatabaseStateException { return adsDao.getTemplates(); }
    @Override
    public void insertAdType(AdType adType) throws InvalidDatabaseStateException { adsDao.insertType(adType); }

    @Override
    public AdType getTypeByName(String typeName) throws InvalidDatabaseStateException {
        return adsDao.getTypeByName(typeName);
    }

    @Override
    public void insertAdTemplate(AdTemplate adTemplate) throws InvalidDatabaseStateException {
        adsDao.insertTemplate(adTemplate);
    }

    @Override
    public void removeType(String typeName) {
        adsDao.removeTypeByName(typeName);
    }

    @Override
    public List<Ad> search(FieldsContainer container) throws InvalidDatabaseStateException {
        return adsDao.search(container);
//        System.out.println("Search po typie " + container.getAdType());
//        for (String path : container.getFieldXPaths()) {
//            System.out.println("Slowo kluczowe: " + path);
//        }
    }
}
