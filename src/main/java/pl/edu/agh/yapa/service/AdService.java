package pl.edu.agh.yapa.service;

import pl.edu.agh.yapa.conversion.FieldsContainer;
import pl.edu.agh.yapa.model.Ad;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.model.AdType;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;

import java.util.List;

/**
 * Created by piotrek on 08.01.15.
 */
public interface AdService {
    List<Ad> getAds() throws InvalidDatabaseStateException;

    List<AdType> getAdTypes() throws InvalidDatabaseStateException;

    void insertAdType(AdType adType) throws InvalidDatabaseStateException;

    void removeType(String typeName);

    List<AdTemplate> getAdTemplates() throws  InvalidDatabaseStateException;

    void insertAdTemplate(AdTemplate adTemplate) throws InvalidDatabaseStateException;
}
