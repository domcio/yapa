package pl.edu.agh.yapa.crawler;

import com.mongodb.*;
import org.bson.types.ObjectId;
import pl.edu.agh.yapa.model.*;
import pl.edu.agh.yapa.persistence.AdsDao;

import java.net.UnknownHostException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dominik
 * Date: 01.05.14
 * Time: 11:46
 * TODO better way of mapping...
 * MongoDB server running on localhost is assumed
 */
public class DBUtils {
    public static final String HOSTNAME = "127.0.0.1";
    public static final int PORT = 27017;
    public static final String DATABASE_NAME = "yapa";
    public static final String TYPES_TABLE = "AdTypes";
    public static final String TEMPLATES_TABLE = "AdTemplates";
    public static final String JOBS_TABLE = "AdJobs";
    public static final String WEBSITES_TABLE = "AdWebsites";
    private static DB connection = null;
    private AdsDao adsDao;

    public static DB getConnection() throws UnknownHostException {
        if (connection == null) {
            MongoClient client = new MongoClient(HOSTNAME, PORT);
            connection = client.getDB(DATABASE_NAME);
        }
        return connection;
    }

    public static String typeNameToTableName(String typeName) {
        return typeName;
    }

    public static void insertAd(Ad ad, AdType type) throws UnknownHostException {
        DB connection = getConnection();
        String tableName = DBUtils.typeNameToTableName(type.getName());
        if (!connection.collectionExists(tableName)) {
            connection.createCollection(tableName, null);
        }
        DBCollection coll = connection.getCollection(tableName);

        BasicDBObject newAd = new BasicDBObject();
        for (Map.Entry<String, String> entry : ad.getFieldValues().entrySet()) {
            newAd.append(entry.getKey(), entry.getValue());
        }
        // do we need reference to adType in this document?
        coll.insert(newAd);
    }

    public static ObjectId insertType(AdType type) throws UnknownHostException {
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
        coll.insert(adType);
        return (ObjectId) adType.get("_id");
    }

    public static ObjectId insertTemplate(AdTemplate template, ObjectId typeID) throws UnknownHostException {
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
        coll.insert(templateObj);
        return (ObjectId) templateObj.get("_id");
    }

    public static ObjectId insertWebsite(Website website) throws UnknownHostException {
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
        sampleWebsite.append("nextPageXPath", website.getNextPageXPath());
        coll.insert(sampleWebsite);
        return (ObjectId) sampleWebsite.get("_id");
    }

    public static ObjectId insertJob(MonitoringJob job, Object templateID, Object websiteID) throws UnknownHostException {
        DB connection = getConnection();
        if (!connection.collectionExists(JOBS_TABLE)) {
            connection.createCollection(JOBS_TABLE, null);
        }
        DBCollection coll = connection.getCollection(JOBS_TABLE);
        BasicDBObject jobObj = new BasicDBObject();
        jobObj.append("name", job.getName());
        jobObj.append("website", websiteID);
        jobObj.append("template", templateID);
        jobObj.append("engine", "htmlCleaner"); //??
        jobObj.append("interval", job.getInterval());
        coll.insert(jobObj);
        return (ObjectId) jobObj.get("_id");
    }

    public static void listTable(String tableName) throws UnknownHostException {
        DB connection = getConnection();
        DBCollection coll = connection.getCollection(tableName);
        for (DBObject obj : coll.find()){
            System.out.println(obj.toString());
        }
    }

    public static void listAds() throws UnknownHostException {
        DB connection = getConnection();
        DBCollection coll = connection.getCollection(TYPES_TABLE);
        for (DBObject obj : coll.find()){
            String adTableName = (String) obj.get("name");
            listTable(typeNameToTableName(adTableName));
        }
    }

    public static void clearDB() throws UnknownHostException {
        DB connection = getConnection();
        connection.dropDatabase();
    }
}
