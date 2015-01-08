package pl.edu.agh.yapa.persistence;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.model.AdType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piotrek on 08.01.15.
 */
public class AdTemplateDaoImpl implements AdTemplateDao {
    private static final String TEMPLATES_COLLECTION = "AdTemplates";

    private final DB database;
    private final AdTypeDao adTypeDao;

    public AdTemplateDaoImpl(DB database, AdTypeDao adTypeDao) {
        this.database = database;
        this.adTypeDao = adTypeDao;
        database.createCollection(TEMPLATES_COLLECTION, null);
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
    public ObjectId insertTemplate(AdTemplate adTemplate) throws InvalidDatabaseStateException {
        DBCollection templates = database.getCollection(TEMPLATES_COLLECTION);
        DBObject newTemplate = new BasicDBObject();
        // TODO remove cast
        ObjectId typeId = ((AdTypeDaoImpl) adTypeDao).getTypeId(adTemplate.getType());
        if (typeId == null) {
            newTemplate.put("type", adTypeDao.insertType(adTemplate.getType()));
        } else {
            newTemplate.put("type", typeId);
        }
        newTemplate.putAll(adTemplate.getPaths());
        templates.insert(newTemplate);
        return (ObjectId) newTemplate.get("_id");
    }

    AdTemplate templateFromJson(DBObject json) throws InvalidDatabaseStateException {
        // TODO remove cast
        AdType type = ((AdTypeDaoImpl) adTypeDao).getTypeByID((ObjectId) json.get("type"));
        AdTemplate template = new AdTemplate(type);
        for (String adField : json.keySet()) {
            if (!adField.equals("_id") && !adField.equals("type")) {
                template.setPath(adField, (String) json.get(adField));
            }
        }
        return template;
    }

    AdTemplate getTemplateByID(ObjectId id) throws InvalidDatabaseStateException {
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
}
