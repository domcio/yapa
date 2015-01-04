package pl.edu.agh.yapa.extraction;

import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.model.Job;
import pl.edu.agh.yapa.model.MonitoringJob;
import pl.edu.agh.yapa.persistence.AdsDao;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by Dominik on 18.06.2014.
 */
public class JobExecutor implements Callable<Integer> {
    private static final String PYTHON_CRAWLER_PATH = "D:\\yapa-final\\yapa\\src\\main\\java\\pl\\edu\\agh\\yapa\\python\\";
//    private static final String PYTHON_CRAWLER_PATH = "/home/piotrek/Documents/YAPA/yapa/src/main/java/pl/edu/agh/yapa/python/";

    private Job job;
    private JobScheduler jobScheduler;
    private boolean isOneOff;

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

    public boolean isOneOff() {
        return isOneOff;
    }

    public void setOneOff(boolean oneOff) {
        this.isOneOff = oneOff;
    }

    @Override
    public Integer call() throws Exception {
        return executeJob(job);

//        if (isOneOff) {
//            jobScheduler.remove(job);
//        }
    }

    private int executeJob(Job job) throws Exception {
        MonitoringJob mJob = (MonitoringJob) job;
        AdTemplate template = mJob.getTemplate();
        String jsonizedTemplate = jsonize(template);
        System.out.println("Running Python for " + mJob.getWebsite() + " and " + jsonizedTemplate);

        ProcessBuilder builder = new ProcessBuilder("python", PYTHON_CRAWLER_PATH + "main.py", mJob.getWebsite(), jsonizedTemplate).inheritIO();
        Process process = builder.start();
        return process.waitFor();
    }

    private String jsonize(AdTemplate template) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (Map.Entry<String, String> entry : template.getPaths().entrySet()) {
            builder.append("\\\"").append(entry.getKey()).append("\\\"").append(":").append("\\\"").append(entry.getValue()).append("\\\"").append(",");
//            builder.append("\"").append(entry.getKey()).append("\"").append(":").append("\"").append(entry.getValue()).append("\"").append(",");
        }
        builder.append("}");
        return builder.toString();
    }
}
