package pl.edu.agh.yapa.persistence;

import com.mongodb.*;
import org.bson.types.ObjectId;
import pl.edu.agh.yapa.model.Ad;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.model.AdType;
import pl.edu.agh.yapa.model.Website;

import java.util.ArrayList;
import java.util.List;

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
}
