package pl.edu.agh.yapa.persistence;

import com.mongodb.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;
import pl.edu.agh.yapa.persistence.MongoSearcher;
import pl.edu.agh.yapa.search.SearchQuery;

import static org.fest.assertions.Assertions.assertThat;

public class MongoSearcherTest {

    private DB database;

    private MongoSearcher underTest;

    @Before
    public void setUp() throws Exception {
        MongoClient mongoClient = new MongoClient("localhost");
        database = mongoClient.getDB("__test_yapa_database");
    }

    private DBCollection createCollectionWithIndex(DB database) {
        DBCollection collection = database.getCollection("__test_collection");

        collection.createIndex(new BasicDBObject("$**", "text"));

        collection.insert(new BasicDBObject("desc", "really cool stuff"));
        collection.insert(new BasicDBObject("desc", "some things that someone might find cool"));
        collection.insert(new BasicDBObject("desc", "things that are not useful"));
        collection.insert(new BasicDBObject("desc", "absolutely not useful at all"));
        collection.insert(new BasicDBObject("desc", "this is extremely cool"));

        return collection;
    }

    private DBCollection createCollectionWithNoIndexAtAll(DB database) {
        DBCollection collection = database.getCollection("__test_collection_without_index");

        collection.insert(new BasicDBObject("desc", "this does not matter"));
        collection.insert(new BasicDBObject("desc", "nothing important"));

        return collection;
    }

    @Test
    public void testFindSomething() throws Exception {
        // given
        DBCollection collection = createCollectionWithIndex(database);
        underTest = new MongoSearcher(collection);
        SearchQuery searchQuery = new SearchQuery("cool");

        // when
        DBCursor results = underTest.search(searchQuery);

        // then
        assertThat( results.toArray() ).hasSize(3);
    }

    @Test(expected = InvalidDatabaseStateException.class)
    public void testInvalidCollection() throws Exception {
        // given
        DBCollection collection = createCollectionWithNoIndexAtAll(database);

        // when
        underTest = new MongoSearcher(collection);
    }

    @After
    public void tearDown() throws Exception {
        database.dropDatabase();
    }
}