package pl.edu.agh.yapa.crawler;

import org.bson.types.ObjectId;
import pl.edu.agh.yapa.extraction.ExtractionEngine;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.model.AdType;
import pl.edu.agh.yapa.model.MonitoringJob;
import pl.edu.agh.yapa.model.Website;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Dominik
 * Date: 30.04.14
 * Time: 15:54
 */
public class ConstructAndExecute {
    public static void sampleGumtreeJob() throws Exception {
        AdType agdAdType = new AdType(Arrays.asList("title", "description", "locality"), "gumtreeAGDAd");
        ObjectId typeID = DBUtils.insertType(agdAdType);
        ExtractionEngine engine = new ExtractionEngine();

        Website gumtreeWebsite = new Website("http://www.gumtree.pl/fp-agd/c9366");
        gumtreeWebsite.addSubURLXPath("//a[@class=\'adLinkSB\']/@href");
        Object websiteID = DBUtils.insertWebsite(gumtreeWebsite);
        System.out.println("inserted website id: " + websiteID);

        AdTemplate gumtreeTemplate = new AdTemplate(agdAdType);
        gumtreeTemplate.setPath("title", "//meta[@property=\'og:title\']/@content");
        gumtreeTemplate.setPath("description", "//meta[@property=\'og:description\']/@content");
        gumtreeTemplate.setPath("locality", "//meta[@property=\'og:locality\']/@content");
        Object templateID = DBUtils.insertTemplate(gumtreeTemplate, typeID);
        System.out.println("inserted template id: " + templateID);

        MonitoringJob job = new MonitoringJob(gumtreeWebsite, gumtreeTemplate, engine, 100);
        job.update(DBUtils.getConnection());
        DBUtils.insertJob(job, templateID, websiteID);
    }

    public static void sampleOlxJob() throws Exception {
        AdType adType = new AdType(Arrays.asList("title", "price"), "olx-mieszkanie");
        ObjectId typeID = DBUtils.insertType(adType);
        ExtractionEngine engine = new ExtractionEngine();

        Website website = new Website("http://olx.pl/nieruchomosci/mieszkania/");
        website.addSubURLXPath("//a[@class=\'thumb vtop inlblk rel tdnone linkWithHash scale4 detailsLink\']/@href");
        website.addSubURLXPath("//a[@class=\'thumb vtop inlblk rel tdnone linkWithHash scale4 detailsLinkPromoted\']/@href");
        Object websiteID = DBUtils.insertWebsite(website);
        System.out.println("inserted website id: " + websiteID);

        AdTemplate template = new AdTemplate(adType);
        template.setPath("title", "//p[@class=\'xx-large fbold lheight26 pdingtop10\']/text()");
        template.setPath("price", "//strong[@class=\'xxxx-large margintop7 block arranged\']/text()");
        Object templateID = DBUtils.insertTemplate(template, typeID);
        System.out.println("inserted template id: " + templateID);

        MonitoringJob job = new MonitoringJob(website, template, engine, 100);
        job.update(DBUtils.getConnection());
        DBUtils.insertJob(job, templateID, websiteID);
    }
}
