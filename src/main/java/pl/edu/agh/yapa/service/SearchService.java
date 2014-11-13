package pl.edu.agh.yapa.service;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Piotr Góralczyk
 */
@Service
public class SearchService {
    private final AdsDao adsDao;
    private final DB database;

    @Autowired
    public SearchService(AdsDao adsDao, DB database) {
        this.adsDao = adsDao;
        this.database = database;
    }

    public List<Ad> smartSearch(String query) throws InvalidDatabaseStateException {
        SearchQuery searchQuery = new SearchQuery(query);
        List<Ad> foundAds = new ArrayList<>();

        for (AdType adType : adsDao.getTypes()) {
            DBCollection collection = database.getCollection(adType.getName());
            DBCursor result = new MongoSearcher(collection).search(searchQuery);

            for (DBObject dbObject : result) {
                foundAds.add( Ad.fromJSON(dbObject) );
            }
        }

        return foundAds;
    }
}