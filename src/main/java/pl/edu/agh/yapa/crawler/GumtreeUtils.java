package pl.edu.agh.yapa.crawler;

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
public class GumtreeUtils {
    public static void constructAndExecuteSampleJob() throws Exception {
        AdType agdAdType = new AdType(Arrays.asList("title", "description", "locality"), "gumtreeAGDAd");
        Object typeID = DBUtils.insertType(agdAdType);
        ExtractionEngine engine = new ExtractionEngine();

        Website gumtreeWebsite = new Website("http://www.gumtree.pl/fp-agd/c9366");
        gumtreeWebsite.addSubURLXPath("//a[@class=\'adLinkSB\']/@href");
        Object websiteID = DBUtils.insertWebsite(gumtreeWebsite);

        AdTemplate gumtreeTemplate = new AdTemplate(agdAdType);
        gumtreeTemplate.setPath("title", "//meta[@property=\'og:title\']/@content");
        gumtreeTemplate.setPath("description", "//meta[@property=\'og:description\']/@content");
        gumtreeTemplate.setPath("locality", "//meta[@property=\'og:locality\']/@content");
        Object templateID = DBUtils.insertTemplate(gumtreeTemplate, typeID);

        MonitoringJob job = new MonitoringJob(gumtreeWebsite, gumtreeTemplate, engine, 100);
        job.update(DBUtils.getConnection());
        DBUtils.insertJob(job, templateID, websiteID);
    }
}
