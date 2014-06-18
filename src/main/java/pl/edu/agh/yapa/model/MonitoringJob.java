package pl.edu.agh.yapa.model;

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
        return engine.extractAds(website, template);
    }

    @Override
    public AdTemplate getTemplate() {
        return template;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
