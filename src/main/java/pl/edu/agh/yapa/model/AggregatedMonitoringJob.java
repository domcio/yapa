package pl.edu.agh.yapa.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Dominik
 * Date: 01.05.14
 * Time: 11:07
 */
public class AggregatedMonitoringJob implements Job {
    private Collection<Job> jobs;

    public AggregatedMonitoringJob() {
        this.jobs = new ArrayList<Job>();
    }

    public AggregatedMonitoringJob(Collection<Job> jobs) {
        this.jobs = new ArrayList<Job>(jobs);
    }

    public void addJob(MonitoringJob job) {
        this.jobs.add(job);
    }

    @Override
    public Collection<Ad> update() throws Exception {
        ArrayList<Ad> ads = new ArrayList<>();
        for (Job job : jobs) {
            ads.addAll(job.update());
        }
        return ads;
    }

    @Override
    public AdTemplate getTemplate() {
        return null;
    }
}
