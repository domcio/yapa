package pl.edu.agh.yapa.persistence;

import com.mongodb.*;
import org.bson.types.ObjectId;
import pl.edu.agh.yapa.model.Ad;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.model.AdType;
import pl.edu.agh.yapa.model.Website;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author pawel
 */
public class AdsDaoImpl implements AdsDao {

    private static final String TYPES_COLLECTION = "AdTypes";
    private static final String TEMPLATES_COLLECTION = "AdTemplates";
    private static final String WEBSITES_COLLECTION = "AdWebsites";
    private final DB database;

    public AdsDaoImpl(DB database) {
        this.database = database;
        database.createCollection(TYPES_COLLECTION, null);
        database.createCollection(WEBSITES_COLLECTION, null);
        database.createCollection(TEMPLATES_COLLECTION, null);
    }

    @Override
    public List<Ad> getAllAds(String adTypeName) throws InvalidDatabaseStateException {
        if (!database.collectionExists(adTypeName)) {
            throw new IllegalArgumentException(adTypeName);
        }

        String[] fields = getFields(adTypeName);
        List<Ad> resultList = new ArrayList<>();
        DBCollection collection = database.getCollection(adTypeName);
        DBCursor cursor = collection.find();

        for (DBObject adObject : cursor) {
            Ad ad = new Ad();
            for (String field : fields) {
                //TODO: for now casting to String cause everything is to be a string
                ad.setValue(field, (String) adObject.get(field));
            }
            resultList.add(ad);
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

    @Override
    public List<AdTemplate> getTemplates() throws InvalidDatabaseStateException {
        DBCollection templates = database.getCollection(TEMPLATES_COLLECTION);
        List<AdTemplate> result = new ArrayList<>();
        for (DBObject templateDoc : templates.find()) {
            System.out.println("get templates" + templateDoc);
            result.add(templateFromJson(templateDoc));
        }
        return result;
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

    private String[] getFields(String adTypeName) throws InvalidDatabaseStateException {
        if (!database.collectionExists(TYPES_COLLECTION)) {
            throw new InvalidDatabaseStateException("Collection " + TYPES_COLLECTION + " does not exist");
        }

        DBCollection collection = database.getCollection(TYPES_COLLECTION);
        DBObject adTypeObject = collection.findOne(new BasicDBObject("name", adTypeName));

        //TODO: kind of WTF-casting
        BasicDBList fieldList = (BasicDBList) adTypeObject.get("fields");
        return fieldList.toArray(new String[0]);
    }

    private AdType getTypeByName(String name) throws InvalidDatabaseStateException {
        if (!database.collectionExists(TYPES_COLLECTION)) {
            throw new InvalidDatabaseStateException("Collection " + TYPES_COLLECTION + " does not exist");
        }
        DBCollection collection = database.getCollection(TYPES_COLLECTION);
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
        DBObject finder = new BasicDBObject("__id", id);
        return types.findOne(finder);
    }

    //TODO delete?
    private Ad adFromJson(DBObject json) {
        Ad ad = new Ad();
        Map map = json.toMap();
        Set<Map.Entry<String, String>> adFields = map.entrySet();
        for (Map.Entry<String, String> adField : adFields) {
            ad.setValue(adField.getKey(), adField.getValue());
        }
        return ad;
    }

    private AdType typeFromJson(DBObject json) {
        System.out.println("type form json" + json);
        AdType type = new AdType();
        json.get("name");
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
        Map map = json.toMap();
        Set<Map.Entry<String, String>> adFields = map.entrySet();
        for (Map.Entry<String, String> adField : adFields) {
            if (!adField.getKey().equals("_id") && !adField.getKey().equals("type")) {
                template.setPath(adField.getKey(), adField.getValue());
            }
        }
        return template;
    }
}
