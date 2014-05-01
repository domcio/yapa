package pl.edu.agh.yapa.monitoring;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

import java.util.Collection;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dominik
 * Date: 01.05.14
 * Time: 09:50
 * In future this and AggregatedMonitoringJob will implement Runnable (???)
 * TODO Actually check for new content instead of writing all
 */
public class MonitoringJob {
    private Website website;
    private AdTemplate template;
    private ExtractionEngine engine;
    private long intervalSeconds;
    private Object interval;

    public MonitoringJob(Website website, AdTemplate template, ExtractionEngine engine, long intervalSeconds) {
        this.website = website;
        this.template = template;
        this.engine = engine;
        this.intervalSeconds = intervalSeconds;
    }

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
            BasicDBObject newAd = new BasicDBObject();
            for (Map.Entry<String, String> entry : ad.getFieldValues().entrySet()) {
                newAd.append(entry.getKey(), entry.getValue());
            }
            coll.insert(newAd);
        }

    }

    public long getInterval() {
        return intervalSeconds;
    }
}
