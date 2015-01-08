package pl.edu.agh.yapa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.yapa.conversion.FieldsContainer;
import pl.edu.agh.yapa.model.Ad;
import pl.edu.agh.yapa.persistence.AdDao;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;
import pl.edu.agh.yapa.search.SearchQuery;

import java.util.List;

/**
 * @author Piotr Góralczyk
 */
@Service
public class SearchServiceImpl implements SearchService {
    private final AdDao adDao;

    @Autowired
    public SearchServiceImpl(AdDao adDao) {
        this.adDao = adDao;
    }

    @Override
    public List<Ad> search(FieldsContainer container) throws InvalidDatabaseStateException {
        return adDao.search(container);
//        System.out.println("Search po typie " + container.getAdType());
//        for (String path : container.getFieldXPaths()) {
//            System.out.println("Slowo kluczowe: " + path);
//        }
    }

    @Override
    public List<Ad> smartSearch(String query) throws InvalidDatabaseStateException {
        SearchQuery searchQuery = new SearchQuery(query);
        return adDao.search(searchQuery);
    }
}
