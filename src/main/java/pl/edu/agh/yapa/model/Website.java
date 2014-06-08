package pl.edu.agh.yapa.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Dominik
 * Date: 01.05.14
 * Time: 10:08
 * Top level website along with how we access the subsites
 * (e.g. a URL of a list of auctions and XPath(s) to get the
 * URLs of a single auction), and also mechanism to access subsequent pages
 */
public class Website {
    private String topURL;
    private Collection<String> subURLXPaths;
    private String nextPageXPath;
    private boolean multiPage;

    public Website(String topURL) {
        this.topURL = topURL;
        this.subURLXPaths = new ArrayList<String>();
    }

    public void addSubURLXPath(String subURLXPath) {
        this.subURLXPaths.add(subURLXPath);
    }

    public void setNextPageXPath(String nextPageXPath) {
        this.nextPageXPath = nextPageXPath;
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

    public void setMultiPage(boolean multiPage) {
        this.multiPage = multiPage;
    }
}
