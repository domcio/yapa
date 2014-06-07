package pl.edu.agh.yapa.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dominik
 * Date: 30.04.14
 * Time: 18:00
 * A template for obtaining the ad data from a concrete website.
 */
public class AdTemplate {
    private Map<String, String> paths;  //XPaths for accessing fields
    private AdType type;

    public AdTemplate(AdType type) {
        this.type = type;
        this.paths = new HashMap<String, String>();
    }

    public AdTemplate(AdType type, Collection<String> paths) {
        this.type = type;
        Map<String, String> fieldPaths = new HashMap<String, String>();
        Collection<String> fields = type.getFields();
        if (fields.size() > paths.size()) throw new IllegalArgumentException();
        Iterator<String> it1 = fields.iterator();
        Iterator<String> it2 = paths.iterator();
        while (it1.hasNext()) {
            fieldPaths.put(it1.next(), it2.next());
        }
        this.paths = fieldPaths;
    }

    public void setPath(String field, String path) {
        paths.put(field, path);
    }

    public Map<String, String> getPaths() {
        return paths;
    }

    public AdType getType() {
        return type;
    }
}
