package pl.edu.agh.yapa.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Dominik
 * Date: 01.05.14
 * Time: 10:08
 * An abstraction of a website accessed for extraction. Contains:
 * - top level website, containing list of ads
 * - way of accessing a single ad page, for now by XPath evaluation
 * - optional way of accessing subsequent pages of the list
 */
public class Website {
    private String topURL;
    private Collection<String> subURLXPaths;
    private String nextPageXPath;
    private boolean multiPage;

    public Website(String topURL) {
        this.topURL = topURL;
        this.subURLXPaths = new ArrayList<>();
    }

    public Website() {
        this.subURLXPaths = new ArrayList<>();
    }

    public void addSubURLXPath(String subURLXPath) {
        this.subURLXPaths.add(subURLXPath);
    }

    public void setNextPageXPath(String nextPageXPath) {
        this.nextPageXPath = nextPageXPath;
        this.multiPage = true;
    }

    public void setTopURL(String topURL) {
        this.topURL = topURL;
    }

    public String getTopURL() {
        return topURL;
    }

    public Collection<String> getSubURLXPaths() {
        return subURLXPaths;
    }

    public String getNextPageXPath() {
        return nextPageXPath;
    }

    public boolean isMultiPage() {
        return multiPage;
    }

    public void removeField(int index) {
        ArrayList<String> subURLs = (ArrayList<String>) subURLXPaths;
        subURLs.remove(index);
    }
}
