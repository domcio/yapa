package pl.edu.agh.yapa.persistence;

import pl.edu.agh.yapa.model.Ad;
import pl.edu.agh.yapa.model.AdType;

import java.util.List;

/**
 * @author pawel
 */
public interface AdsDao {

    List<Ad> getAllAds(String adTypeName) throws InvalidDatabaseStateException;
    List<AdType> getTypes() throws InvalidDatabaseStateException;

}
