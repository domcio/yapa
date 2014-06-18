package pl.edu.agh.yapa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.yapa.model.Website;
import pl.edu.agh.yapa.persistence.AdsDao;

import java.util.List;

/**
 * Created by Dominik on 18.06.2014.
 */
@Service
public class WebsiteService {
    private final AdsDao adsDao;

    @Autowired
    public WebsiteService(AdsDao adsDao) {
        this.adsDao = adsDao;
    }

    public List<Website> getWebsites() {
        return adsDao.getWebsites();
    }
}
