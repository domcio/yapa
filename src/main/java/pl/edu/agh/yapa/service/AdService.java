package pl.edu.agh.yapa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.yapa.model.Ad;
import pl.edu.agh.yapa.model.AdType;
import pl.edu.agh.yapa.persistence.AdsDao;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;

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

    public List<Ad> getAllAds(String adTypeName) throws InvalidDatabaseStateException {
        return adsDao.getAllAds(adTypeName);
    }
    public List<AdType> getAdTypes() throws InvalidDatabaseStateException { return adsDao.getTypes(); }
}
