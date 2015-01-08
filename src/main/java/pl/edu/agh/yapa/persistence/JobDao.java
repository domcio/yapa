package pl.edu.agh.yapa.persistence;

import org.bson.types.ObjectId;
import pl.edu.agh.yapa.model.Job;
import pl.edu.agh.yapa.model.MonitoringJob;

import java.util.List;

/**
 * Created by piotrek on 08.01.15.
 */
public interface JobDao {
    List<Job> getJobs() throws InvalidDatabaseStateException;

    Job getJobByName(String jobName) throws InvalidDatabaseStateException;

    ObjectId insertJob(MonitoringJob job) throws InvalidDatabaseStateException;
}
