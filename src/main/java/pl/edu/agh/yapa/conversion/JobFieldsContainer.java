package pl.edu.agh.yapa.conversion;

import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.model.AdType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Piotr Góralczyk
 */
public class JobFieldsContainer {
    private AdTemplate template;
    private String url;
    private String name;

    public JobFieldsContainer() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AdTemplate getTemplate() {
        return template;
    }

    public void setTemplate(AdTemplate template) {
        this.template = template;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}