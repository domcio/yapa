package pl.edu.agh.yapa.model;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Dominik
 * Date: 30.04.14
 * Time: 18:00
 * A template for obtaining the ad data from a concrete URL.
 */
public class AdTemplate {
    private Map<String, String> paths;  //XPaths for accessing fields
    private AdType type;

    public AdTemplate() {
        this.paths = new HashMap<>();
    }

    public AdTemplate(AdType type) {
        this.type = type;
        this.paths = new HashMap<>();
    }

    public AdTemplate(AdType type, List<String> paths) {
        this.type = type;
        Map<String, String> fieldPaths = new HashMap<String, String>();
        List<String> fields = type.getFields();
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

    public void setType(AdType type) {
        this.type = type;
    }

    public String print() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(type.getName()).append(":");
        for(Map.Entry entry : paths.entrySet()) {
            buffer.append(entry.getKey()).append(":").append(entry.getValue());
        }
        return buffer.toString();
    }
}
