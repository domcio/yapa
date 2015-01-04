package pl.edu.agh.yapa.conversion;

/**
 * Created by piotrek on 04.01.15.
 */
public class JobStatus {
    public static final JobStatus RUNNING = new JobStatus(true, null);

    private final boolean running;
    private final Integer exitCode;

    public JobStatus(boolean running, Integer exitCode) {
        this.running = running;
        this.exitCode = exitCode;
    }

    public static JobStatus notRunning(Integer exitCode) {
        return new JobStatus(false, exitCode);
    }

    public boolean isRunning() {
        return running;
    }

    public int getExitCode() {
        return exitCode;
    }
}
