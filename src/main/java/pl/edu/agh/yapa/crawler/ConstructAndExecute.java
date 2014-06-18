package pl.edu.agh.yapa.crawler;

import org.bson.types.ObjectId;
import pl.edu.agh.yapa.extraction.XPathEngine;
import pl.edu.agh.yapa.model.*;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Dominik
 * Date: 30.04.14
 * Time: 15:54
 */
public class ConstructAndExecute {
    public static void sampleGumtreeJob() throws Exception {
        AdType type = new AdType(Arrays.asList("title", "description", "locality"), "gumtreeAGDAd");
        ObjectId typeID = DBUtils.insertType(type);
        XPathEngine engine = new XPathEngine();

        Website website = new Website("http://www.gumtree.pl/fp-agd/c9366");
        website.addSubURLXPath("//a[@class=\'adLinkSB\']/@href");
        website.setNextPageXPath("//a[@class=\'prevNextLink\'][contains(., 'Nast')]/@href");
        Object websiteID = DBUtils.insertWebsite(website);
        System.out.println("inserted website id: " + websiteID);

        AdTemplate template = new AdTemplate(type);
        template.setPath("title", "//meta[@property=\'og:title\']/@content");
        template.setPath("description", "//meta[@property=\'og:description\']/@content");
        template.setPath("locality", "//meta[@property=\'og:locality\']/@content");
        Object templateID = DBUtils.insertTemplate(template, typeID);
        System.out.println("inserted template id: " + templateID);

        MonitoringJob job = new MonitoringJob("gumtreeJob", website, template, engine, 100);

        for (Ad ad : job.update()) {
            DBUtils.insertAd(ad, template.getType());
        }
        DBUtils.insertJob(job, templateID, websiteID);
    }

    public static void sampleOlxJob() throws Exception {
        AdType adType = new AdType(Arrays.asList("title", "price"), "olx-mieszkanie");
        ObjectId typeID = DBUtils.insertType(adType);
        XPathEngine engine = new XPathEngine();

        Website website = new Website("http://olx.pl/nieruchomosci/mieszkania/");
        website.addSubURLXPath("//a[@class=\'thumb vtop inlblk rel tdnone linkWithHash scale4 detailsLink\']/@href");
        website.addSubURLXPath("//a[@class=\'thumb vtop inlblk rel tdnone linkWithHash scale4 detailsLinkPromoted\']/@href");
        website.setNextPageXPath("//span[@class=\'fbold next abs large\']/a/@href");
        Object websiteID = DBUtils.insertWebsite(website);
        System.out.println("inserted website id: " + websiteID);

        AdTemplate template = new AdTemplate(adType);
        template.setPath("title", "//p[@class=\'xx-large fbold lheight26 pdingtop10\']/text()");
        template.setPath("price", "//strong[@class=\'xxxx-large margintop7 block arranged\']/text()");
        Object templateID = DBUtils.insertTemplate(template, typeID);
        System.out.println("inserted template id: " + templateID);

        MonitoringJob job = new MonitoringJob("olxJob", website, template, engine, 100);

        for (Ad ad : job.update()) {
            DBUtils.insertAd(ad, template.getType());
        }
        DBUtils.insertJob(job, templateID, websiteID);
    }
}
