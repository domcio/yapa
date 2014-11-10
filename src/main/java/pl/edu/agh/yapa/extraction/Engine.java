package pl.edu.agh.yapa.extraction;

import pl.edu.agh.yapa.model.Ad;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.model.SnapshotStamp;

import java.util.Collection;

/**
 * Created by Dominik on 18.06.2014.
 */
public interface Engine {
    Collection<Ad> extractAds(String website, AdTemplate template) throws Exception;
    void setStamp(SnapshotStamp stamp);
}
