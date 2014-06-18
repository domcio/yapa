package pl.edu.agh.yapa.extraction;

import pl.edu.agh.yapa.model.Ad;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.model.Website;

import java.util.Collection;

/**
 * Created by Dominik on 18.06.2014.
 */
public interface Engine {
    Collection<Ad> extractAds(Website website, AdTemplate template) throws Exception;
}
