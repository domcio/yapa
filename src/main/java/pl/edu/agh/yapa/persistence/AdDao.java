package pl.edu.agh.yapa.persistence;

import org.bson.types.ObjectId;
import pl.edu.agh.yapa.conversion.FieldsContainer;
import pl.edu.agh.yapa.model.*;
import pl.edu.agh.yapa.search.SearchQuery;

import java.util.List;

/**
 * @author pawel
 */
public interface AdDao {
    List<Ad> getAds() throws InvalidDatabaseStateException;

    List<Ad> getAdsByType(String adTypeName) throws InvalidDatabaseStateException;

    ObjectId insertAd(Ad ad, AdType adType) throws InvalidDatabaseStateException;

    List<Ad> search(FieldsContainer container) throws InvalidDatabaseStateException;

    List<Ad> search(SearchQuery searchQuery) throws InvalidDatabaseStateException;
}
