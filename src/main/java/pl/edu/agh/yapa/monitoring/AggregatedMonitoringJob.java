package pl.edu.agh.yapa.monitoring;

import com.mongodb.DB;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Dominik
 * Date: 01.05.14
 * Time: 11:07
 */
public class AggregatedMonitoringJob {
    private Collection<MonitoringJob> jobs;

    public AggregatedMonitoringJob() {
        this.jobs = new ArrayList<MonitoringJob>();
    }

    public AggregatedMonitoringJob(Collection<MonitoringJob> jobs) {
        this.jobs = new ArrayList<MonitoringJob>(jobs);
    }

    public void addJob(MonitoringJob job) {
        this.jobs.add(job);
    }

    public void update(DB connection) throws Exception {
        for (MonitoringJob job : jobs) {
            job.update(connection);
        }
    }
}
