package pl.edu.agh.yapa.search;

import com.mongodb.*;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;

public class MongoSearcher {

    private final DBCollection collection;

    public MongoSearcher(DBCollection collection) throws InvalidDatabaseStateException {
        if ( collection.getIndexInfo().size() < 2 ) {
            // TODO: real check for text index
            throw new InvalidDatabaseStateException("Collection is expected to have text index");
        }

        this.collection = collection;
    }

    public DBCursor search(SearchQuery searchQuery) {
        BasicDBObject findQueryObject = new BasicDBObject("$text", new BasicDBObject("$search", searchQuery.asMongoQuery()));
        BasicDBObject metaObject = new BasicDBObject("$meta", "textScore");
        BasicDBObject presentQuery = new BasicDBObject("_score", metaObject);

        return collection.find(findQueryObject, presentQuery).sort(presentQuery);
    }

}
