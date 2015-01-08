package pl.edu.agh.yapa.service;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.yapa.model.Ad;
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
public class SearchServiceImpl implements SearchService {
    private final AdsDao adsDao;
    private final DB database;

    @Autowired
    public SearchServiceImpl(AdsDao adsDao, DB database) {
        this.adsDao = adsDao;
        this.database = database;
    }

    @Override
    public List<Ad> smartSearch(String query) throws InvalidDatabaseStateException {
        SearchQuery searchQuery = new SearchQuery(query);
        List<Ad> foundAds = new ArrayList<>();

        DBCollection collection = database.getCollection( adsDao.getAdsCollectionName() );
        DBCursor result = new MongoSearcher(collection).search(searchQuery);

        for (DBObject dbObject : result) {
            foundAds.add( Ad.fromJSON(dbObject) );
        }

        return foundAds;
    }
}
