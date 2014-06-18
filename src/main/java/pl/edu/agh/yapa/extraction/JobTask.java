package pl.edu.agh.yapa.extraction;

import pl.edu.agh.yapa.model.MonitoringJob;
import pl.edu.agh.yapa.persistence.AdsDao;

/**
 * Created by Dominik on 18.06.2014.
 */
public class JobTask implements Runnable {
    private AdsDao adsDao;
    private MonitoringJob job;

    public JobTask(AdsDao adsDao, MonitoringJob job) {
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
