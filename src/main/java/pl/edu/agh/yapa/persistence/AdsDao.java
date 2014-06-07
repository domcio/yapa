package pl.edu.agh.yapa.persistence;

import pl.edu.agh.yapa.model.Ad;

import java.util.List;

/**
 * @author pawel
 */
public interface AdsDao {

    List<Ad> getAllAds(String adTypeName) throws InvalidDatabaseStateException;

}
