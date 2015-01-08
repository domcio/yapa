package pl.edu.agh.yapa.persistence;

import com.mongodb.*;
import org.bson.types.ObjectId;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import pl.edu.agh.yapa.conversion.FieldsContainer;
import pl.edu.agh.yapa.model.Ad;
import pl.edu.agh.yapa.model.AdType;
import pl.edu.agh.yapa.model.SnapshotStamp;
import pl.edu.agh.yapa.search.SearchQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author pawel
 */
public class AdDaoImpl implements AdDao {
    private static final String WEBSITES_COLLECTION = "AdWebsites";
    private static final String NEW_ADS_COLL = "ads";
    private static final DateTimeFormatter formatter = ISODateTimeFormat.dateTime();
    private final DB database;

    public AdDaoImpl(DB database) {
        this.database = database;
        database.createCollection(WEBSITES_COLLECTION, null);

        DBCollection adsCollection = database.createCollection(NEW_ADS_COLL, null);
        adsCollection.createIndex(new BasicDBObject("$**", "text"));
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
    public List<Ad> search(SearchQuery searchQuery) throws InvalidDatabaseStateException {
        List<Ad> foundAds = new ArrayList<>();

        DBCollection collection = database.getCollection(NEW_ADS_COLL);
        DBCursor result = new MongoSearcher(collection).search(searchQuery);

        for (DBObject dbObject : result) {
            foundAds.add( Ad.fromJSON(dbObject) );
        }

        return foundAds;
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
}
