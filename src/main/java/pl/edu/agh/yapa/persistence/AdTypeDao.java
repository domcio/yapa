package pl.edu.agh.yapa.persistence;

import org.bson.types.ObjectId;
import pl.edu.agh.yapa.model.AdType;

import java.util.List;

/**
 * Created by piotrek on 08.01.15.
 */
public interface AdTypeDao {
    List<AdType> getTypes() throws InvalidDatabaseStateException;

    AdType getTypeByName(String typeName) throws InvalidDatabaseStateException;

    ObjectId insertType(AdType adType) throws InvalidDatabaseStateException;

    void removeTypeByName(String typeName);
}
