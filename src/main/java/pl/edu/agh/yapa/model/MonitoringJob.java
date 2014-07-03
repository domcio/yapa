package pl.edu.agh.yapa.model;

import org.joda.time.DateTime;
import pl.edu.agh.yapa.extraction.Engine;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Dominik
 * Date: 01.05.14
 * Time: 09:50
 * In future: change to implement Runnable
 * TODO Make this and AggregatedMonitoringJob implement the same interface
 */
public class MonitoringJob implements Job {
    private Website website;
    private AdTemplate template;
    private Engine engine;
    private long intervalSeconds;
    private String name;

    public MonitoringJob(String name, Website website, AdTemplate template, Engine engine, long intervalSeconds) {
        this.name = name;
        this.website = website;
        this.template = template;
        this.engine = engine;
        this.intervalSeconds = intervalSeconds;
    }

    public MonitoringJob() {

    }

    //prototype method
    @Override
    public Collection<Ad> update() throws Exception {
        engine.setStamp( new SnapshotStamp(DateTime.now(), name) );

        return engine.extractAds(website, template);
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

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setWebsite(Website website) {
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

    public Website getWebsite() {
        return website;
    }

    // TODO use ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MonitoringJob)) return false;

        MonitoringJob that = (MonitoringJob) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    // TODO use ID
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
