package pl.edu.agh.yapa.monitoring;

import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dominik
 * Date: 01.05.14
 * Time: 11:46
 */
public class DBUtils {
    public static final String TYPES_TABLE = "AdTypes";
    public static final String TEMPLATES_TABLE = "AdTemplates";
    public static final String JOBS_TABLE = "AdJobs";
    public static final String WEBSITES_TABLE = "AdWebsites";
    public static final String DATABASE_NAME = "yapa";
    private static DB connection = null;

    public static DB getConnection() throws UnknownHostException {
        if (connection == null) {
            MongoClient client = new MongoClient("localhost", 27017);
            connection = client.getDB(DATABASE_NAME);
        }
        return connection;
    }

    public static String typeNameToTableName(String typeName) {
        return typeName + "s";
    }

    public static void insertAd(Ad ad, AdType type) throws UnknownHostException {
        DB connection = getConnection();
        String tableName = DBUtils.typeNameToTableName(typeNameToTableName(type.getName()));
        if (!connection.collectionExists(tableName)) {
            connection.createCollection(tableName, null);
        }
        DBCollection coll = connection.getCollection(tableName);

        BasicDBObject newAd = new BasicDBObject();
        for (Map.Entry<String, String> entry : ad.getFieldValues().entrySet()) {
            newAd.append(entry.getKey(), entry.getValue());
        }
    }

    public static Object insertType(AdType type) throws UnknownHostException {
        DB connection = getConnection();
        if (!connection.collectionExists(TYPES_TABLE)) {
            connection.createCollection(TYPES_TABLE, null);
        }
        DBCollection coll = connection.getCollection(TYPES_TABLE);

        BasicDBObject adType = new BasicDBObject();
        adType.append("name", type.getName());
        BasicDBList list = new BasicDBList();
        for (String field : type.getFields()) list.add(field);
        adType.append("fields", list);
        return coll.insert(adType).getUpsertedId();
    }

    public static Object insertTemplate(AdTemplate template, Object typeID) throws UnknownHostException {
        DB connection = getConnection();
        if (!connection.collectionExists(TEMPLATES_TABLE)) {
            connection.createCollection(TEMPLATES_TABLE, null);
        }
        DBCollection coll = connection.getCollection(TEMPLATES_TABLE);
        BasicDBObject templateObj = new BasicDBObject();
        Map<String, String> paths = template.getPaths();
        for (Map.Entry<String, String> entry : paths.entrySet()) {
            templateObj.append(entry.getKey(), entry.getValue());
        }
        templateObj.append("type", typeID);
        return coll.insert(templateObj).getUpsertedId();
    }

    public static Object insertWebsite(Website website) throws UnknownHostException {
        DB connection = getConnection();
        if (!connection.collectionExists(WEBSITES_TABLE)) {
            connection.createCollection(WEBSITES_TABLE, null);
        }
        DBCollection coll = connection.getCollection(WEBSITES_TABLE);
        BasicDBObject sampleWebsite = new BasicDBObject();
        BasicDBList subURLsList = new BasicDBList();
        sampleWebsite.append("topURL", website.getTopURL());
        for (String subURL : website.getSubURLXPaths())
        subURLsList.add(subURL);
        sampleWebsite.append("subURLXPaths", subURLsList);
        return coll.insert(sampleWebsite).getUpsertedId();
    }

    public static void insertJob(MonitoringJob job, Object templateID, Object websiteID) throws UnknownHostException {
        DB connection = getConnection();
        if (!connection.collectionExists(JOBS_TABLE)) {
            connection.createCollection(JOBS_TABLE, null);
        }
        DBCollection coll = connection.getCollection(JOBS_TABLE);
        BasicDBObject jobObj = new BasicDBObject();
        jobObj.append("website", websiteID);
        jobObj.append("template", templateID);
        jobObj.append("engine", "htmlCleaner"); //??
        jobObj.append("interval", job.getInterval());
        coll.insert(jobObj);
    }
}
