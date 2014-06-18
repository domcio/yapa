package pl.edu.agh.yapa.persistence;

import com.mongodb.*;
import org.bson.types.ObjectId;
import pl.edu.agh.yapa.extraction.EngineFactory;
import pl.edu.agh.yapa.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author pawel
 */
public class AdsDaoImpl implements AdsDao {

    private static final String TYPES_COLLECTION = "AdTypes";
    private static final String TEMPLATES_COLLECTION = "AdTemplates";
    private static final String WEBSITES_COLLECTION = "AdWebsites";
    private static final String JOBS_COLLECTION = "AdJobs";
    private final DB database;

    public AdsDaoImpl(DB database) {
        this.database = database;
        database.createCollection(TYPES_COLLECTION, null);
        database.createCollection(WEBSITES_COLLECTION, null);
        database.createCollection(TEMPLATES_COLLECTION, null);
        database.createCollection(JOBS_COLLECTION, null);
    }

    @Override
    public List<Ad> getAds() throws InvalidDatabaseStateException {
        List<Ad> adsList = new ArrayList<>();
        DBCollection typesCollection = database.getCollection(TYPES_COLLECTION);
        for (DBObject adJson : typesCollection.find()) {
            adsList.addAll(getAdsByType((String) adJson.get("name")));
        }
        return adsList;
    }

    @Override
    public List<Ad> getAdsByType(String adTypeName) throws InvalidDatabaseStateException {
        if (!database.collectionExists(adTypeName)) {
            return new ArrayList<>();
        }
        List<Ad> resultList = new ArrayList<>();
        DBCollection collection = database.getCollection(adTypeName);

        for (DBObject adJson : collection.find()) {
            resultList.add(adFromJson(adJson));
        }
        return resultList;
    }

    @Override
    public List<AdType> getTypes() throws InvalidDatabaseStateException {
        List<AdType> typesList = new ArrayList<>();
        DBCollection typesCollection = database.getCollection(TYPES_COLLECTION);
        for (DBObject typeObj : typesCollection.find()) {
            typesList.add(typeFromJson(typeObj));
        }
        return typesList;
    }

    private ObjectId getTypeId(AdType type) throws InvalidDatabaseStateException {
        DBCollection typesCollection = database.getCollection(TYPES_COLLECTION);
        for (DBObject typeObj : typesCollection.find()) {
            if (type.getName().equals((String) typeObj.get("name"))) {
                return (ObjectId) typeObj.get("_id");
            }
        }
        return null;
    }

    @Override
    public List<AdTemplate> getTemplates() throws InvalidDatabaseStateException {
        List<AdTemplate> result = new ArrayList<>();
        DBCollection templates = database.getCollection(TEMPLATES_COLLECTION);
        for (DBObject templateDoc : templates.find()) {
            result.add(templateFromJson(templateDoc));
        }
        return result;
    }

    @Override
    public MonitoringJob getJobByName(String jobName) throws InvalidDatabaseStateException {
        DBCollection jobs = database.getCollection(JOBS_COLLECTION);
        DBObject finder = new BasicDBObject().append("name", jobName);
        DBCursor cursor = jobs.find(finder);
        DBObject found = cursor.next();//we assume there would be a job with this name
        return jobFromJson(found);
    }

    @Override
    public void insertType(AdType adType) throws InvalidDatabaseStateException {
        DBCollection typesCollection = database.getCollection(TYPES_COLLECTION);

        //TODO collection.insert(typeToJson(type));
        DBObject newType = new BasicDBObject();
        newType.put("name", adType.getName());
        BasicDBList fieldsList = new BasicDBList();
        for (String field : adType.getFields()) {
            fieldsList.add(field);
        }
        newType.put("fields", fieldsList);
        typesCollection.insert(newType);
    }

    @Override
    public void executeJob(Job job) throws Exception {
        insertAds(job.update(), job.getTemplate().getType());
    }

    private void insertAds(Collection<Ad> ads, AdType type) {
        DBCollection collection = database.getCollection(type.getName());
        for (Ad ad : ads) {
            collection.insert(adToJson(ad));
        }
    }

    private DBObject adToJson(Ad ad) {
        DBObject json = new BasicDBObject();
        json.putAll(ad.getFieldValues());
        return json;
    }

    @Override
    public List<String> getJobsNames() {
        DBCollection jobs = database.getCollection(JOBS_COLLECTION);
        List<String> result = new ArrayList<>();
        for (DBObject obj : jobs.find()) {
            result.add((String) obj.get("name"));
        }
        return result;
    }

    @Override
    public void insertTemplate(AdTemplate adTemplate) throws InvalidDatabaseStateException {
        DBCollection templates = database.getCollection(TEMPLATES_COLLECTION);
        DBObject newTemplate = new BasicDBObject();
        newTemplate.put("type", getTypeId(adTemplate.getType()));
        for (Map.Entry<String, String> entry : adTemplate.getPaths().entrySet()) {
            newTemplate.put(entry.getKey(), entry.getValue());
        }
        templates.insert(newTemplate);
    }

    public AdType getTypeByName(String name) throws InvalidDatabaseStateException {
        DBCollection collection = database.getCollection(TYPES_COLLECTION);
        System.out.println("name " + name);
        return typeFromJson(collection.findOne(new BasicDBObject("name", name)));
    }

    private AdType getTypeByID(ObjectId id) throws InvalidDatabaseStateException {
        return typeFromJson(getByID(id, TYPES_COLLECTION));
    }

    private AdTemplate getTemplateByID(ObjectId id) throws InvalidDatabaseStateException {
        return templateFromJson(getByID(id, TEMPLATES_COLLECTION));
    }

    private Website getWebsiteByID(ObjectId id) throws InvalidDatabaseStateException {
        return websiteFromJson(getByID(id, WEBSITES_COLLECTION));
    }

    private DBObject getByID(ObjectId id, String collection) throws InvalidDatabaseStateException {
        if (!database.collectionExists(collection)) {
            throw new InvalidDatabaseStateException("Collection " + collection + " does not exist");
        }
        DBCollection types = database.getCollection(collection);
        DBObject finder = new BasicDBObject("_id", id);
        return types.findOne(finder);
    }

    private Ad adFromJson(DBObject json) {
        Ad ad = new Ad();
        for (String field : json.keySet()) {
            if (!field.equals("_id")) {
                ad.setValue(field, (String) json.get(field));
            }
        }
        return ad;
    }

    private AdType typeFromJson(DBObject json) {
        AdType type = new AdType();
        type.setName((String) json.get("name"));
        BasicDBList fieldList = (BasicDBList) json.get("fields");
        for (Object field : fieldList) {
            type.addField((String) field);
        }
        return type;
    }

    private Website websiteFromJson(DBObject json) {
        Website website = new Website((String) json.get("topURL"));
        for (Object subURLs : (BasicDBList) json.get("subURLXPaths")) {
            website.addSubURLXPath((String) subURLs);
        }
        website.setNextPageXPath((String) json.get("nextPageXPath"));
        return website;
    }

    private AdTemplate templateFromJson(DBObject json) throws InvalidDatabaseStateException {
        AdType type = getTypeByID((ObjectId) json.get("type"));
        AdTemplate template = new AdTemplate(type);
        for (String adField : json.keySet()) {
            if (!adField.equals("_id") && !adField.equals("type")) {
                template.setPath(adField, (String) json.get(adField));
            }
        }
        return template;
    }

    private MonitoringJob jobFromJson(DBObject json) throws InvalidDatabaseStateException {
        MonitoringJob job = new MonitoringJob();
        job.setName((String) json.get("name"));
        job.setTemplate(getTemplateByID((ObjectId) json.get("template")));
        job.setWebsite(getWebsiteByID((ObjectId) json.get("website")));
        job.setEngine(EngineFactory.getEngineByName((String) json.get("engine")));
        job.setInterval((Long) json.get("interval"));
        return job;
    }
}
