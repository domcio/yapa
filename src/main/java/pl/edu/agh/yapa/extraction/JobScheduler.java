package pl.edu.agh.yapa.extraction;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.scheduling.TaskScheduler;
import pl.edu.agh.yapa.model.Job;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Piotr Góralczyk
 */
public class JobScheduler {
    private final Map<Job, ScheduledFuture> activeJobs = new HashMap<>();
    private final ObjectFactory<JobExecutor> jobExecutorFactory;
    private final TaskScheduler scheduler;

    public JobScheduler(TaskScheduler scheduler, ObjectFactory<JobExecutor> jobExecutorFactory) {
        this.scheduler = scheduler;
        this.jobExecutorFactory = jobExecutorFactory;
    }

    /**
     * Run a job once.
     *
     * @param job
     */
    public synchronized void run(Job job) {
        schedule(job, true);
    }

    /**
     * Run a job periodically with an interval specified by the job
     * (between the completion of one execution and the start of the next).
     *
     * @param job
     */
    public synchronized void activate(Job job) {
        schedule(job, false);
    }

    /**
     * Cancel a job. For already running jobs it currently only removes the job from active jobs
     * (the jobs runs uninterrupted).
     *
     * @param job
     * @return true if the job was deactivated as a result of this call, false otherwise
     */
    public synchronized boolean deactivate(Job job) {
        final ScheduledFuture<?> scheduledJobFuture = activeJobs.remove(job);
        if (scheduledJobFuture == null) {
            return false;
        } else {
            scheduledJobFuture.cancel(false);
            return true;
        }
    }

    /**
     * Remove a job without cancelling it (for jobs that run once to deactivate themselves).
     *
     * @param job
     * @return true if the job was deactivated as a result of this call, false otherwise
     */
    synchronized boolean remove(Job job) {
        return activeJobs.remove(job) != null;
    }

    public synchronized boolean isActive(Job job) {
        return activeJobs.containsKey(job);
    }

    private void schedule(Job job, boolean once) {
        checkNotActive(job);
        // TODO Get a fully initialized JobExecutor from a factory (use parameters)
        final JobExecutor jobExecutor = jobExecutorFactory.getObject();
        jobExecutor.setJob(job);
        jobExecutor.setJobScheduler(this);
        final ScheduledFuture<?> scheduledJobFuture;
        if (once) {
            scheduledJobFuture = scheduler.schedule(jobExecutor, new Date());
        } else {
            scheduledJobFuture = scheduler.scheduleWithFixedDelay(jobExecutor, job.getInterval());
        }
        activeJobs.put(job, scheduledJobFuture);
    }

    private void checkNotActive(Job job) {
        if (activeJobs.containsKey(job)) {
            throw new IllegalArgumentException("Job " + job.getName() + "is already active.");
        }
    }
}
