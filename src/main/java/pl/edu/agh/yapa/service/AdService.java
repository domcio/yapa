package pl.edu.agh.yapa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.yapa.conversion.FieldsContainer;
import pl.edu.agh.yapa.model.Ad;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.model.AdType;
import pl.edu.agh.yapa.persistence.AdsDao;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;
import pl.edu.agh.yapa.search.MongoSearcher;
import pl.edu.agh.yapa.search.SearchQuery;

import java.util.List;

/**
 * @author Piotr Góralczyk
 */
@Service
public class AdService {
    private final AdsDao adsDao;

    @Autowired
    public AdService(AdsDao adsDao) {
        this.adsDao = adsDao;
    }

    public List<Ad> getAds() throws InvalidDatabaseStateException { return adsDao.getAds(); }
    public List<Ad> getAdsByType(String adTypeName) throws InvalidDatabaseStateException {
        return adsDao.getAdsByType(adTypeName);
    }
    public List<AdType> getAdTypes() throws InvalidDatabaseStateException { return adsDao.getTypes(); }
    public List<AdTemplate> getTemplates() throws  InvalidDatabaseStateException { return adsDao.getTemplates(); }
    public void insertAdType(AdType adType) throws InvalidDatabaseStateException { adsDao.insertType(adType); }

    public AdType getTypeByName(String typeName) throws InvalidDatabaseStateException {
        return adsDao.getTypeByName(typeName);
    }

    public void insertAdTemplate(AdTemplate adTemplate) throws InvalidDatabaseStateException {
        adsDao.insertTemplate(adTemplate);
    }

    public void removeType(String typeName) {
        adsDao.removeTypeByName(typeName);
    }

    public List<Ad> search(FieldsContainer container) throws InvalidDatabaseStateException {
        return adsDao.search(container);
//        System.out.println("Search po typie " + container.getAdType());
//        for (String path : container.getFieldXPaths()) {
//            System.out.println("Slowo kluczowe: " + path);
//        }
    }
}
