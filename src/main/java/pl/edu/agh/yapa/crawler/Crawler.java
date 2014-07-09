package pl.edu.agh.yapa.crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import sun.plugin.dom.html.HTMLDocument;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * Created by Dominik on 2014-07-09.
 */
public class Crawler extends WebCrawler {
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"
            + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf"
            + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
    private final HtmlCleaner cleaner;

    public Crawler() {
        this.cleaner = new HtmlCleaner();
        CleanerProperties props = cleaner.getProperties();
        props.setAllowHtmlInsideAttributes(true);
        props.setAllowMultiWordAttributes(true);
        props.setRecognizeUnicodeChars(true);
        props.setOmitComments(true);
    }

    @Override
    public boolean shouldVisit(WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches() && href.startsWith("http://www.gumtree.pl/");
    }

    @Override
    public void visit(Page page) {
        System.out.println("Visiting: " + page.getWebURL().getPath());
        HtmlParseData data = (HtmlParseData) page.getParseData();

        TagNode topLevelNode = cleaner.clean(data.getHtml());
        try {
            Document doc = new DomSerializer(new CleanerProperties()).createDOM(topLevelNode);
            XPath path = XPathFactory.newInstance().newXPath();
            String title = "//meta[@property=\'og:title\']/@content";
            String description = "//meta[@property=\'og:description\']/@content";
            String locality = "//meta[@property=\'og:locality\']/@content";

            NodeList list = (NodeList) path.evaluate(title, doc, XPathConstants.NODESET);
            if (list.getLength() != 0)
                 System.out.println("title: " + list.item(0).getNodeValue());

            list = (NodeList) path.evaluate(description, doc, XPathConstants.NODESET);
            if (list.getLength() != 0)
                System.out.println("description: " + list.item(0).getNodeValue());

            list = (NodeList) path.evaluate(locality, doc, XPathConstants.NODESET);
            if (list.getLength() != 0)
                System.out.println("locality: " + list.item(0).getNodeValue());

        } catch (ParserConfigurationException | XPathExpressionException e) {
            e.printStackTrace();
        }
    }
}
