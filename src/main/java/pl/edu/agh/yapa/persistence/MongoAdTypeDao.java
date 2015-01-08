package pl.edu.agh.yapa.persistence;

import com.mongodb.*;
import org.bson.types.ObjectId;
import pl.edu.agh.yapa.model.AdType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piotrek on 08.01.15.
 */
public class MongoAdTypeDao implements AdTypeDao {
    private static final String TYPES_COLLECTION = "AdTypes";

    private final DB database;

    public MongoAdTypeDao(DB database) {
        this.database = database;
        database.createCollection(TYPES_COLLECTION, null);
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

    ObjectId getTypeId(AdType type) throws InvalidDatabaseStateException {
        DBCollection typesCollection = database.getCollection(TYPES_COLLECTION);
        for (DBObject typeObj : typesCollection.find()) {
            if (type.getName().equals((String) typeObj.get("name"))) {
                return (ObjectId) typeObj.get("_id");
            }
        }
        return null;
    }

    @Override
    public void removeTypeByName(String typeName) {
        DBCollection types = database.getCollection(TYPES_COLLECTION);
        types.remove(new BasicDBObject().append("name", typeName));
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

        return (ObjectId) newType.get("_id");
    }

    public AdType getTypeByName(String name) throws InvalidDatabaseStateException {
        DBCollection collection = database.getCollection(TYPES_COLLECTION);
        System.out.println("name " + name);
        return typeFromJson(collection.findOne(new BasicDBObject("name", name)));
    }

    AdType getTypeByID(ObjectId id) throws InvalidDatabaseStateException {
        return typeFromJson(getByID(id, TYPES_COLLECTION));
    }

    private DBObject getByID(ObjectId id, String collection) throws InvalidDatabaseStateException {
        if (!database.collectionExists(collection)) {
            throw new InvalidDatabaseStateException("Collection " + collection + " does not exist");
        }
        DBCollection types = database.getCollection(collection);
        DBObject finder = new BasicDBObject("_id", id);
        return types.findOne(finder);
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
}
