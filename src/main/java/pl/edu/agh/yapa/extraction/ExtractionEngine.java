package pl.edu.agh.yapa.extraction;

import org.htmlcleaner.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import pl.edu.agh.yapa.model.Ad;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.model.Website;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
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
    //TODO move this to MonitoringJob/Website
    private static final int PAGE_LIMIT = 10; // limit the amount of pages traversed within a single job

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
        Document doc = new DomSerializer(new CleanerProperties()).createDOM(topLevelNode);
        XPath path = XPathFactory.newInstance().newXPath();
        Collection<String> subURLXPaths = website.getSubURLXPaths();
        Collection<Ad> ads = new ArrayList<>();

        for (String subURLXPath : subURLXPaths) {
            NodeList list = (NodeList) path.evaluate(subURLXPath, doc, XPathConstants.NODESET);
            for (int i = 0; i < list.getLength(); i++) {
                String subURLString = list.item(i).getNodeValue();
                ads.add(extractAd(new URL(subURLString), template));
            }
        }

        if (website.isMultiPage()) {
            int n = 0;
            NodeList nextPageList = (NodeList) path.evaluate(website.getNextPageXPath(), doc, XPathConstants.NODESET);
            do {
                n++;
                System.out.println("fetching new page, " + nextPageList.item(0).getNodeValue());
                topLevelNode = cleaner.clean(new URL(nextPageList.item(0).getNodeValue()));
                doc = new DomSerializer(new CleanerProperties()).createDOM(topLevelNode);

                for (String subURLXPath : subURLXPaths) {
                    NodeList list = (NodeList) path.evaluate(subURLXPath, doc, XPathConstants.NODESET);
                    for (int i = 0; i < list.getLength(); i++) {
                        String subURLString = list.item(i).getNodeValue();
                        ads.add(extractAd(new URL(subURLString), template));
                    }
                }
                nextPageList = (NodeList) path.evaluate(website.getNextPageXPath(), doc, XPathConstants.NODESET);
            } while ((nextPageList.getLength() != 0) && (n < PAGE_LIMIT));
            System.out.println("Reached page limit");
        }
        return ads;
    }

    //extract from this website
    private Ad extractAd(URL subURL, AdTemplate template) throws IOException, XPatherException, ParserConfigurationException, XPathExpressionException {
        System.out.println("Extracting from URL: " + subURL);
        Ad ad = new Ad();
        Collection<String> fields = template.getType().getFields();
        Map<String, String> fieldXPaths = template.getPaths();
        TagNode rootNode = cleaner.clean(subURL);
        Document doc = new DomSerializer(new CleanerProperties()).createDOM(rootNode);
        XPath path = XPathFactory.newInstance().newXPath();
        for (String field : fields) {
            if (!fieldXPaths.containsKey(field)) {
                ad.setValue(field, null);
            } else {
                //TODO better solution as to what we do if there are more than 1 or there are none
                NodeList list = (NodeList) path.evaluate(fieldXPaths.get(field), doc, XPathConstants.NODESET);
                if (list.getLength() == 0) {
                    ad.setValue(field, null);
                } else {
                    ad.setValue(field, list.item(0).getNodeValue());
                }
            }
        }
        return ad;
    }
}
