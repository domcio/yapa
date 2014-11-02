package pl.edu.agh.yapa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.yapa.extraction.JobScheduler;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.model.Job;
import pl.edu.agh.yapa.model.MonitoringJob;
import pl.edu.agh.yapa.model.Website;
import pl.edu.agh.yapa.persistence.AdsDao;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<Job, Boolean> getJobsAndStatuses() throws InvalidDatabaseStateException {
        List<Job> jobs = adsDao.getJobs();
        Map<Job, Boolean> jobStatuses = new HashMap<>();
        for (Job job : jobs) {
            jobStatuses.put(job, jobScheduler.isActive(job));
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

    public boolean isJobActive(String jobName) throws InvalidDatabaseStateException {
        Job job = adsDao.getJobByName(jobName);
        return jobScheduler.isActive(job);
    }

    public List<AdTemplate> getTemplates() throws  InvalidDatabaseStateException { return adsDao.getTemplates(); }

    public void addJob(String field, MonitoringJob job) throws InvalidDatabaseStateException {
        job.setWebsite(new Website());
        job.getWebsite().setTopURL(field);
        adsDao.insertJob(job);
    }
}
