package pl.edu.agh.yapa.service;

import pl.edu.agh.yapa.conversion.JobStatus;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.model.Job;
import pl.edu.agh.yapa.model.MonitoringJob;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by piotrek on 08.01.15.
 */
public interface JobService {
    void addJob(MonitoringJob job) throws InvalidDatabaseStateException;

    Map<Job, JobStatus> getJobsAndStatuses() throws InvalidDatabaseStateException, ExecutionException, InterruptedException;

    void runJob(String jobName) throws InvalidDatabaseStateException;

    void activateJob(String jobName) throws InvalidDatabaseStateException;

    void deactivateJob(String jobName) throws InvalidDatabaseStateException;
}
