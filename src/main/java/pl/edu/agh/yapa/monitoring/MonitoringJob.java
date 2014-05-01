package pl.edu.agh.yapa.monitoring;

import com.mongodb.DB;
import com.mongodb.DBCollection;

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
    public void update(DB connection) throws Exception {
        Collection<Ad> ads = engine.extractAds(website, template);
        String tableName = DBUtils.typeNameToTableName(template.getType().getName());
        DBCollection coll = null;
        if (!connection.collectionExists(tableName)) {
            coll = connection.createCollection(tableName, null);
        } else {
            coll = connection.getCollection(tableName);
        }
        for (Ad ad : ads) {
            DBUtils.insertAd(ad, template.getType());
        }
    }

    public void startMonitoring() {

    }

    public void stopMonitoring() {

    }

    public long getInterval() {
        return intervalSeconds;
    }
}
