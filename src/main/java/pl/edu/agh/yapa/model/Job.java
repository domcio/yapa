package pl.edu.agh.yapa.model;

import java.util.Collection;

/**
 * Created by Dominik on 18.06.2014.
 */
public interface Job {
    Collection<Ad> update() throws Exception;
    AdTemplate getTemplate();
}
