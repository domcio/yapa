package pl.edu.agh.yapa.persistence;

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
    MonitoringJob getJobByName(String jobName) throws InvalidDatabaseStateException;

    void insertTemplate(AdTemplate adTemplate) throws InvalidDatabaseStateException;

    AdType getTypeByName(String typeName) throws InvalidDatabaseStateException;

    void insertType(AdType adType) throws InvalidDatabaseStateException;

    void executeJob(Job job) throws Exception;

    List<String> getJobsNames();
}
