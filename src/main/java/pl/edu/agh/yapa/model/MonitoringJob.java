package pl.edu.agh.yapa.model;

import pl.edu.agh.yapa.crawler.DBUtils;
import pl.edu.agh.yapa.extraction.ExtractionEngine;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Dominik
 * Date: 01.05.14
 * Time: 09:50
 * In future: change to implement Runnable
 * TODO Make this and AggregatedMonitoringJob implement the same interface
 */
public class MonitoringJob {
    private Website website;
    private AdTemplate template;
    private ExtractionEngine engine;
    private long intervalSeconds;

    public MonitoringJob(Website website, AdTemplate template, ExtractionEngine engine, long intervalSeconds) {
        this.website = website;
        this.template = template;
        this.engine = engine;
        this.intervalSeconds = intervalSeconds;
    }

    //prototype method
    public void update() throws Exception {
        Collection<Ad> ads = engine.extractAds(website, template);
        for (Ad ad : ads) {
            DBUtils.insertAd(ad, template.getType());
        }
    }

    public long getInterval() {
        return intervalSeconds;
    }
}
