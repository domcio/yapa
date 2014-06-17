package pl.edu.agh.yapa.conversion;

import pl.edu.agh.yapa.model.AdType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Piotr Góralczyk
 */
public class FieldsContainer {
    private List<String> fieldXPaths = new ArrayList<>();
    private AdType adType;

    public FieldsContainer() {
    }

    public FieldsContainer(int count) {
        for (int i = 0; i < count; i++) {
            fieldXPaths.add("");
        }
    }

    public List<String> getFieldXPaths() {
        return fieldXPaths;
    }

    public void setFieldXPaths(List<String> fieldXPaths) {
        this.fieldXPaths = fieldXPaths;
    }

    public AdType getAdType() {
        return adType;
    }

    public void setAdType(AdType adType) {
        this.adType = adType;
    }
}
