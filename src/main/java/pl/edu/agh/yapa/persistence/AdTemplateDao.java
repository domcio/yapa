package pl.edu.agh.yapa.persistence;

import org.bson.types.ObjectId;
import pl.edu.agh.yapa.model.AdTemplate;

import java.util.List;

/**
 * Created by piotrek on 08.01.15.
 */
public interface AdTemplateDao {
    List<AdTemplate> getTemplates() throws InvalidDatabaseStateException;

    ObjectId insertTemplate(AdTemplate adTemplate) throws InvalidDatabaseStateException;
}
