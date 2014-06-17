package pl.edu.agh.yapa.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dominik
 * Date: 01.05.14
 * Time: 09:36
 * A type of an ad i.e. a collection of fields, abstracted from the way we obtain them.
 */
public class AdType {
    private List<String> fields;
    private String name;

    public AdType() {
        this.fields = new ArrayList<>();
    }

    public AdType(List<String> fields, String name) {
        this.fields = fields;
        this.name = name;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addField(String name) {
        this.fields.add(name);
    }

    public void removeField(int index) {
        ArrayList fields = (ArrayList) this.fields;
        fields.remove(index);
    }
}
