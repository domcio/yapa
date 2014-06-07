package pl.edu.agh.yapa.extraction;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import pl.edu.agh.yapa.model.Ad;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.model.Website;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dominik
 * Date: 01.05.14
 * Time: 10:14
 * In the final release - interface (?)
 */
public class ExtractionEngine {
    private HtmlCleaner cleaner;

    public ExtractionEngine() {
        //set up the cleaner
        this.cleaner = new HtmlCleaner();
        CleanerProperties props = cleaner.getProperties();
        props.setAllowHtmlInsideAttributes(true);
        props.setAllowMultiWordAttributes(true);
        props.setRecognizeUnicodeChars(true);
        props.setOmitComments(true);
    }

    //extract from top-level website
    public Collection<Ad> extractAds(Website website, AdTemplate template) throws Exception {
        TagNode topLevelNode = cleaner.clean(new URL(website.getTopURL()));
        Collection<String> subURLXPaths = website.getSubURLXPaths();
        Collection<Ad> ads = new ArrayList<>();
        for (String subURLXPath : subURLXPaths) {
            Object[] subURLs = topLevelNode.evaluateXPath(subURLXPath);
            for (Object subURL : subURLs) {
                String subURLString = subURL.toString();
                ads.add(extractAd(new URL(subURLString), template));
            }
        }
        return ads;
    }

    //extract from this website
    private Ad extractAd(URL subURL, AdTemplate template) throws IOException, XPatherException {
        Ad ad = new Ad();
        Collection<String> fields = template.getType().getFields();
        Map<String, String> fieldXPaths = template.getPaths();
        TagNode rootNode = cleaner.clean(subURL);
        for (String field : fields) {
            if (!fieldXPaths.containsKey(field)) {
                ad.setValue(field, null);
            } else {
                Object[] result = rootNode.evaluateXPath(fieldXPaths.get(field));
                if (result.length != 1) throw new IllegalStateException();
                ad.setValue(field, (String) result[0]);
            }
        }
        return ad;
    }
}
