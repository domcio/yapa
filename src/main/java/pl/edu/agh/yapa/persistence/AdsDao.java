package pl.edu.agh.yapa.persistence;

import org.bson.types.ObjectId;
import pl.edu.agh.yapa.conversion.FieldsContainer;
import pl.edu.agh.yapa.model.*;

import java.util.List;

/**
 * @author pawel
 */
public interface AdsDao {
    List<Ad> getAds() throws InvalidDatabaseStateException;

    List<Ad> getAdsByType(String adTypeName) throws InvalidDatabaseStateException;

    List<AdType> getTypes() throws InvalidDatabaseStateException;

    List<AdTemplate> getTemplates() throws InvalidDatabaseStateException;

    Job getJobByName(String jobName) throws InvalidDatabaseStateException;

    ObjectId insertTemplate(AdTemplate adTemplate) throws InvalidDatabaseStateException;

    AdType getTypeByName(String typeName) throws InvalidDatabaseStateException;

    ObjectId insertAd(Ad ad, AdType adType) throws InvalidDatabaseStateException;
    ObjectId insertType(AdType adType) throws InvalidDatabaseStateException;

    void executeJob(Job job) throws Exception;

    List<Job> getJobs() throws InvalidDatabaseStateException;

    ObjectId insertJob(MonitoringJob job) throws InvalidDatabaseStateException;

    void clear();

    void removeTypeByName(String typeName);

    List<Ad> search(FieldsContainer container) throws InvalidDatabaseStateException;
}
