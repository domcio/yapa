package pl.edu.agh.yapa.model;

import com.mongodb.DBObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dominik
 * Date: 01.05.14
 * Time: 10:16
 * An instance of ad data obtained using a template.
 */
public class Ad {
    private String url;
    private Map<String, String> fieldValues;
    private SnapshotStamp snapshot;

    public Ad() {
        this.fieldValues = new HashMap<>();
    }

    public void setValue(String field, String value) {
        fieldValues.put(field, value);
    }

    public Map<String, String> getFieldValues() {
        return fieldValues;
    }

    public SnapshotStamp getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(SnapshotStamp snapshot) {
        this.snapshot = snapshot;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : fieldValues.entrySet()) {
            builder.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
        }
        return builder.toString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static Ad fromJSON(DBObject json) {
        //TODO: some cleanup cause its copied from somewhere
        Ad ad = new Ad();
        for (String field : json.keySet()) {
            if ( !field.equals("url") && !field.startsWith("_") ) {
                ad.setValue(field, (String) json.get(field));
            }
        }
        ad.setUrl((String) json.get("url"));

        return ad;
    }
}
