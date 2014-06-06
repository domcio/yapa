package pl.edu.agh.yapa.persistence;

import pl.edu.agh.yapa.monitoring.Ad;

import java.util.List;

/**
 * @author pawel
 */
public interface AdsDao {

    List<Ad> getAllAds(String adTypeName) throws InvalidDatabaseStateException;

}
