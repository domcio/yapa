package pl.edu.agh.yapa.model;

import org.joda.time.DateTime;

/**
 * @author pawel
 */
public class SnapshotStamp {

    private final DateTime datetime;
    private final String jobName;

    public SnapshotStamp(DateTime datetime, String jobName) {
        this.datetime = datetime;
        this.jobName = jobName;
    }

    public DateTime getDatetime() {
        return datetime;
    }

    public String getJobName() {
        return jobName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SnapshotStamp that = (SnapshotStamp) o;

        if (!datetime.equals(that.datetime)) return false;
        if (!jobName.equals(that.jobName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = datetime.hashCode();
        result = 31 * result + jobName.hashCode();
        return result;
    }

}
