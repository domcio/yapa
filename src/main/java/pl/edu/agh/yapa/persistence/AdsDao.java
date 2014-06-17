package pl.edu.agh.yapa.persistence;

import pl.edu.agh.yapa.model.Ad;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.model.AdType;

import java.util.List;

/**
 * @author pawel
 */
public interface AdsDao {
    List<Ad> getAds() throws InvalidDatabaseStateException;
    List<Ad> getAdsByType(String adTypeName) throws InvalidDatabaseStateException;
    List<AdType> getTypes() throws InvalidDatabaseStateException;
    List<AdTemplate> getTemplates() throws InvalidDatabaseStateException;

    void insertTemplate(AdTemplate adTemplate) throws InvalidDatabaseStateException;

    AdType getTypeByName(String typeName) throws InvalidDatabaseStateException;

    void insertType(AdType adType) throws InvalidDatabaseStateException;
}
