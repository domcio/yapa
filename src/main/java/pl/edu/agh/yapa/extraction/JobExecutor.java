package pl.edu.agh.yapa.extraction;

import pl.edu.agh.yapa.model.Job;
import pl.edu.agh.yapa.persistence.AdsDao;

/**
 * Created by Dominik on 18.06.2014.
 */
public class JobExecutor implements Runnable {
    private final AdsDao adsDao;
    private Job job;
    private JobScheduler jobScheduler;

    // TODO Remove
    public JobExecutor(AdsDao adsDao) {
        this.adsDao = adsDao;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public JobScheduler getJobScheduler() {
        return jobScheduler;
    }

    public void setJobScheduler(JobScheduler jobScheduler) {
        this.jobScheduler = jobScheduler;
    }

    @Override
    public void run() {
        try {
            adsDao.executeJob(job);
        } catch (Exception e) {
            e.printStackTrace();
        }
        jobScheduler.remove(job);
    }
}
