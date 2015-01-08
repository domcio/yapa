package pl.edu.agh.yapa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.yapa.conversion.FieldsContainer;
import pl.edu.agh.yapa.model.Ad;
import pl.edu.agh.yapa.persistence.AdsDao;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;
import pl.edu.agh.yapa.search.SearchQuery;

import java.util.List;

/**
 * @author Piotr Góralczyk
 */
@Service
public class SearchServiceImpl implements SearchService {
    private final AdsDao adsDao;

    @Autowired
    public SearchServiceImpl(AdsDao adsDao) {
        this.adsDao = adsDao;
    }

    @Override
    public List<Ad> search(FieldsContainer container) throws InvalidDatabaseStateException {
        return adsDao.search(container);
//        System.out.println("Search po typie " + container.getAdType());
//        for (String path : container.getFieldXPaths()) {
//            System.out.println("Slowo kluczowe: " + path);
//        }
    }

    @Override
    public List<Ad> smartSearch(String query) throws InvalidDatabaseStateException {
        SearchQuery searchQuery = new SearchQuery(query);
        return adsDao.search(searchQuery);
    }
}
