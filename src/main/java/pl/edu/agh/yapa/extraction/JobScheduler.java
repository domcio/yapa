package pl.edu.agh.yapa.extraction;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.scheduling.SchedulingTaskExecutor;
import pl.edu.agh.yapa.conversion.JobStatus;
import pl.edu.agh.yapa.model.Job;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Piotr Góralczyk
 */
public class JobScheduler {
    private final Map<Job, Future<Integer>> activeJobs = new HashMap<>();
    private final ObjectFactory<JobExecutor> jobExecutorFactory;
    private final SchedulingTaskExecutor executor;

    public JobScheduler(SchedulingTaskExecutor executor, ObjectFactory<JobExecutor> jobExecutorFactory) {
        this.executor = executor;
        this.jobExecutorFactory = jobExecutorFactory;
    }

    /**
     * Run a job oneOff.
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
        final Future<Integer> scheduledJobFuture = activeJobs.remove(job);
        if (scheduledJobFuture == null) {
            return false;
        } else {
            scheduledJobFuture.cancel(false);
            return true;
        }
    }

    /**
     * Remove a job without cancelling it (for jobs that run oneOff to deactivate themselves).
     *
     * @param job
     * @return true if the job was deactivated as a result of this call, false otherwise
     */
    synchronized boolean remove(Job job) {
        return activeJobs.remove(job) != null;
    }

//    public synchronized boolean isActive(Job job) {
//        return activeJobs.get(job);
//    }

    private void schedule(Job job, boolean oneOff) {
        checkNotActive(job);
        // TODO Get a fully initialized JobExecutor from a factory (use parameters)
        final JobExecutor jobExecutor = jobExecutorFactory.getObject();
        jobExecutor.setJob(job);
        jobExecutor.setJobScheduler(this); // for callback (one-off jobs have to cancel themselves)
        jobExecutor.setOneOff(oneOff);
        final Future<Integer> scheduledJobFuture;
        if (oneOff) {
            scheduledJobFuture = executor.submit(jobExecutor);
        } else {
            throw new IllegalArgumentException("tylko one off!");
            // TODO delete?
            // scheduledJobFuture = scheduler.scheduleWithFixedDelay(jobExecutor, job.getInterval()*1000);
            // Comment to put one-off jobs into active jobs
            // activeJobs.put(job, scheduledJobFuture);
        }
        // Uncomment to put one-off jobs into active jobs
        activeJobs.put(job, scheduledJobFuture);
    }

    private void checkNotActive(Job job) {
        if (activeJobs.containsKey(job)) {
            if (!activeJobs.get(job).isDone()) {
                throw new IllegalArgumentException("Job " + job.getName() + "is already active.");
            }
        }
    }

    public JobStatus getStatus(Job job) throws ExecutionException, InterruptedException {
        if (activeJobs.containsKey(job)) {
            Future<Integer> jobFuture = activeJobs.get(job);
            if (jobFuture.isDone()) {
                Integer exitCode = jobFuture.get();
                return JobStatus.notRunning(exitCode);
            } else {
                return JobStatus.RUNNING;
            }
        } else {
            return JobStatus.notRunning(0);
        }
    }
}
