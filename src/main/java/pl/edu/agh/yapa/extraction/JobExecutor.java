package pl.edu.agh.yapa.extraction;

import pl.edu.agh.yapa.model.Job;
import pl.edu.agh.yapa.persistence.AdsDao;

/**
 * Created by Dominik on 18.06.2014.
 */
public class JobExecutor implements Runnable {
    private AdsDao adsDao;
    private Job job;

    public JobExecutor(AdsDao adsDao, Job job) {
        this.adsDao = adsDao;
        this.job = job;
    }

    @Override
    public void run() {
        try {
            adsDao.executeJob(job);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
