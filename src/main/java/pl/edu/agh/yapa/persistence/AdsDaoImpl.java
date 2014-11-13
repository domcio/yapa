package pl.edu.agh.yapa.persistence;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import pl.edu.agh.yapa.conversion.FieldsContainer;
import pl.edu.agh.yapa.model.Ad;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.model.AdType;
import pl.edu.agh.yapa.model.Job;
import pl.edu.agh.yapa.model.MonitoringJob;
import pl.edu.agh.yapa.model.SnapshotStamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author pawel
 */
public class AdsDaoImpl implements AdsDao {

    private static final String PYTHON_CRAWLER_PATH = "D:\\yapa-final\\yapa\\src\\main\\java\\pl\\edu\\agh\\yapa\\python\\";
    private static final String TYPES_COLLECTION = "AdTypes";
    private static final String TEMPLATES_COLLECTION = "AdTemplates";
    private static final String WEBSITES_COLLECTION = "AdWebsites";
    private static final String JOBS_COLLECTION = "AdJobs";
    private static final String NEW_ADS_COLL = "ads";
    private static final DateTimeFormatter formatter = ISODateTimeFormat.dateTime();
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
        DBCollection typesCollection = database.getCollection(NEW_ADS_COLL);
        for (DBObject adJson : typesCollection.find()) {
            adsList.add(adFromJson(adJson));
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
    public Job getJobByName(String jobName) throws InvalidDatabaseStateException {
        DBCollection jobs = database.getCollection(JOBS_COLLECTION);
        DBObject finder = new BasicDBObject().append("name", jobName);
        DBCursor cursor = jobs.find(finder);
        DBObject found = cursor.next();//we assume there would be a job with this name
        return jobFromJson(found);
    }

    @Override
    public void clear() {
        database.dropDatabase();
    }

    @Override
    public void removeTypeByName(String typeName) {
        DBCollection types = database.getCollection(TYPES_COLLECTION);
        types.remove(new BasicDBObject().append("name", typeName));
    }

    @Override
    public List<Ad> search(FieldsContainer container) throws InvalidDatabaseStateException {
        //TODO implement actual search
        BasicDBObject query = new BasicDBObject();

        List<String> constraints = container.getFieldXPaths();
        List<String> fields = container.getAdType().getFields();

        for (int i = 0; i < fields.size(); i++) {
            if (constraints.get(i).equals("")) {
                query.append(fields.get(i), Pattern.compile(".*", Pattern.CASE_INSENSITIVE));
            } else {
                Pattern pattern = Pattern.compile(".*" + constraints.get(i) + ".*", Pattern.CASE_INSENSITIVE);
                query.append(fields.get(i), pattern);
            }
        }
        List<Ad> ads = new ArrayList<>();
        DBCollection collection = database.getCollection(container.getAdType().getName());
        for (DBObject object : collection.find(query)) {
            ads.add(adFromJson(object));
        }
        return ads;
    }

    @Override
    public ObjectId insertAd(Ad ad, AdType adType) throws InvalidDatabaseStateException {
        String tableName = adType.getName();
        if (!database.collectionExists(tableName)) {
            database.createCollection(tableName, null);
        }
        DBCollection ads = database.getCollection(tableName);

        BasicDBObject adJson = new BasicDBObject();
        adJson.putAll(ad.getFieldValues());
        ads.insert(adJson);
        return (ObjectId) adJson.get("_id");
    }

    @Override
    public ObjectId insertType(AdType adType) throws InvalidDatabaseStateException {
        DBCollection typesCollection = database.getCollection(TYPES_COLLECTION);

        DBObject newType = new BasicDBObject();
        newType.put("name", adType.getName());
        BasicDBList fieldsList = new BasicDBList();
        for (String field : adType.getFields()) {
            fieldsList.add(field);
        }
        newType.put("fields", fieldsList);
        typesCollection.insert(newType);

        DBCollection thisTypeCollection = database.getCollection(adType.getName());
        //TODO: on known fields only
        thisTypeCollection.createIndex(new BasicDBObject("$**", "text"));

        return (ObjectId) newType.get("_id");
    }

    @Override
    public void executeJob(Job job) throws Exception {
        MonitoringJob mJob = (MonitoringJob) job;
        AdTemplate template = mJob.getTemplate();
        String jsonizedTemplate = jsonize(template);
        System.out.println("Running Python for " + mJob.getWebsite() + " and " + jsonizedTemplate);
//
        ProcessBuilder builder = new ProcessBuilder("python", PYTHON_CRAWLER_PATH + "main.py", mJob.getWebsite(), jsonizedTemplate);
        builder.start(); //fire and forget...
    }

    private String jsonize(AdTemplate template) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (Map.Entry<String, String> entry : template.getPaths().entrySet()) {
            builder.append("\\\"").append(entry.getKey()).append("\\\"").append(":").append("\\\"").append(entry.getValue()).append("\\\"").append(",");
        }
        builder.append("}");
        return builder.toString();
    }

    private DBObject adToJson(Ad ad) {
        DBObject json = new BasicDBObject();
        json.putAll(ad.getFieldValues());

        SnapshotStamp stamp = ad.getSnapshot();
        DBObject stampJson = new BasicDBObject();
        stampJson.put("date", stamp.getDatetime().toString(formatter));
        stampJson.put("job", stamp.getJobName());

        json.put("__stamp", stampJson);

        return json;
    }

    @Override
    public List<Job> getJobs() throws InvalidDatabaseStateException {
        DBCollection jobsCollection = database.getCollection(JOBS_COLLECTION);
        List<Job> jobs = new ArrayList<>();
        for (DBObject jobJson : jobsCollection.find()) {
            jobs.add(jobFromJson(jobJson));
        }
        return jobs;
    }

    @Override
    public ObjectId insertJob(MonitoringJob job) throws InvalidDatabaseStateException {
        DBCollection coll = database.getCollection(JOBS_COLLECTION);
        BasicDBObject jobObj = new BasicDBObject();
        jobObj.append("name", job.getName());
        jobObj.append("website", job.getWebsite());
        jobObj.append("template", insertTemplate(job.getTemplate()));
        coll.insert(jobObj);
        return (ObjectId) jobObj.get("_id");
    }

    @Override
    public ObjectId insertTemplate(AdTemplate adTemplate) throws InvalidDatabaseStateException {
        DBCollection templates = database.getCollection(TEMPLATES_COLLECTION);
        DBObject newTemplate = new BasicDBObject();
        ObjectId typeId = getTypeId(adTemplate.getType());
        if (typeId == null) {
            newTemplate.put("type", insertType(adTemplate.getType()));
        } else {
            newTemplate.put("type", typeId);
        }
        newTemplate.putAll(adTemplate.getPaths());
        templates.insert(newTemplate);
        return (ObjectId) newTemplate.get("_id");
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
            if (!field.equals("url") && !field.equals("_id") && !field.equals("__stamp")) {
                ad.setValue(field, (String) json.get(field));
            }
        }
        ad.setUrl((String) json.get("url"));

//        DBObject stamp = (DBObject) json.get("__stamp");

//        DateTime date = formatter.parseDateTime((String) stamp.get("date"));
//        String jobName = (String) stamp.get("job");

//        ad.setSnapshot(new SnapshotStamp(date, jobName));
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

    private Job jobFromJson(DBObject json) throws InvalidDatabaseStateException {
        MonitoringJob job = new MonitoringJob();
        job.setName((String) json.get("name"));
        job.setTemplate(getTemplateByID((ObjectId) json.get("template")));
        job.setWebsite((String) json.get("website"));
        return job;
    }
}
