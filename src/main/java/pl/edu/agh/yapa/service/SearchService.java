package pl.edu.agh.yapa.service;

import pl.edu.agh.yapa.model.Ad;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;

import java.util.List;

/**
 * Created by piotrek on 08.01.15.
 */
public interface SearchService {
    List<Ad> smartSearch(String query) throws InvalidDatabaseStateException;
}
