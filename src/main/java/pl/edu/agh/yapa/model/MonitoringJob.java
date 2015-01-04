package pl.edu.agh.yapa.model;

import org.joda.time.DateTime;
import pl.edu.agh.yapa.extraction.Engine;

import java.util.Collection;

public class MonitoringJob implements Job {
    private String website;
    private AdTemplate template;
    private long intervalSeconds;
    private String name;

    public MonitoringJob(String name, String website, AdTemplate template) {
        this.website = website;
        this.name = name;
        this.template = template;
    }

    public MonitoringJob() {}

    @Override
    public Collection<Ad> update() throws Exception {
        return null;
    }

    @Override
    public AdTemplate getTemplate() {
        return template;
    }

    @Override
    public long getInterval() {
        return intervalSeconds;
    }

    public void setInterval(long interval) {
        this.intervalSeconds = interval;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setTemplate(AdTemplate template) {
        this.template = template;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    // TODO use ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MonitoringJob)) return false;

        MonitoringJob that = (MonitoringJob) o;

        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    // TODO use ID
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
