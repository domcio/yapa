package pl.edu.agh.yapa.monitoring;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Dominik
 * Date: 01.05.14
 * Time: 09:36
 * A type of an ad i.e. a collection of fields, abstracted from the way we obtain them.
 */
public class AdType {
    private Collection<String> fields;
    private String name;

    public AdType(Collection<String> fields, String name) {
        this.fields = fields;
        this.name = name;
    }

    public Collection<String> getFields() {
        return fields;
    }

    public String getName() {
        return name;
    }
}
