package pl.edu.agh.yapa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.yapa.conversion.JobStatus;
import pl.edu.agh.yapa.extraction.JobScheduler;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.model.Job;
import pl.edu.agh.yapa.model.MonitoringJob;
import pl.edu.agh.yapa.persistence.AdsDao;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author Piotr Góralczyk
 */
@Service
public class JobService {
    private final AdsDao adsDao;
    private final JobScheduler jobScheduler;

    @Autowired
    public JobService(AdsDao adsDao, JobScheduler jobScheduler) {
        this.adsDao = adsDao;
        this.jobScheduler = jobScheduler;
    }

    public Map<Job, JobStatus> getJobsAndStatuses() throws InvalidDatabaseStateException, ExecutionException, InterruptedException {
        List<Job> jobs = adsDao.getJobs();
        Map<Job, JobStatus> jobStatuses = new HashMap<>();
        for (Job job : jobs) {
            jobStatuses.put(job, jobScheduler.getStatus(job));
        }
        return jobStatuses;
    }

    public void runJob(String jobName) throws InvalidDatabaseStateException {
        Job job = adsDao.getJobByName(jobName);
        jobScheduler.run(job);
    }

    public void activateJob(String jobName) throws InvalidDatabaseStateException {
        Job job = adsDao.getJobByName(jobName);
        jobScheduler.activate(job);
    }

    public void deactivateJob(String jobName) throws InvalidDatabaseStateException {
        Job job = adsDao.getJobByName(jobName);
        jobScheduler.deactivate(job);
    }

    public List<AdTemplate> getTemplates() throws  InvalidDatabaseStateException { return adsDao.getTemplates(); }

    public void addJob(MonitoringJob job) throws InvalidDatabaseStateException {
        adsDao.insertJob(job);
    }
}
