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
 * URLs of a single auction)
 */
public class Website {
    private String topURL;
    private Collection<String> subURLXPaths;

    public Website(String topURL) {
        this.topURL = topURL;
        this.subURLXPaths = new ArrayList<String>();
    }

    public void addSubURLXPath(String subURLXPath) {
        this.subURLXPaths.add(subURLXPath);
    }

    public String getTopURL() {
        return topURL;
    }

    public Collection<String> getSubURLXPaths() {
        return subURLXPaths;
    }
}
