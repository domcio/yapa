package pl.edu.agh.yapa.persistence;

import com.mongodb.*;
import pl.edu.agh.yapa.monitoring.Ad;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pawel
 */
public class AdsDaoImpl implements AdsDao {

    public static final String HOSTNAME = "127.0.0.1";
    public static final int PORT = 27017;
    public static final String DATABASE_NAME = "yapa";

    public static final String TYPES_COLLECTION = "AdTypes";
    private final DB database;

    public AdsDaoImpl(DB database) {
        this.database = database;
    }

    @Override
    public List<Ad> getAllAds(String adTypeName) throws InvalidDatabaseStateException {
        if ( !database.collectionExists(adTypeName) ){
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
                ad.setValue( field, (String) adObject.get(field) );
            }
            resultList.add(ad);
        }

        return resultList;
    }

    private String[] getFields(String adTypeName) throws InvalidDatabaseStateException {
        if ( !database.collectionExists(TYPES_COLLECTION) ){
            throw new InvalidDatabaseStateException("No " + TYPES_COLLECTION + " collection existing");
        }

        DBCollection collection = database.getCollection(TYPES_COLLECTION);
        BasicDBList fieldsListObject = (BasicDBList) collection.findOne( new BasicDBObject("name", adTypeName) );

        //TODO: kind of WTF-casting
        return (String[]) fieldsListObject.toArray();
    }

}
