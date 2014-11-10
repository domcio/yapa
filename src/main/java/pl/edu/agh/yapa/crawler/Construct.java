package pl.edu.agh.yapa.crawler;

import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.model.AdType;
import pl.edu.agh.yapa.model.MonitoringJob;
import pl.edu.agh.yapa.persistence.AdsDao;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Dominik
 * Date: 30.04.14
 * Time: 15:54
 */
public class Construct {
    public static void sampleGumtreeJob(AdsDao adsDao) throws Exception, InvalidDatabaseStateException {
        AdType type = new AdType(Arrays.asList("title", "description", "locality"), "gumtreeAGDAd");

        String website = "http://www.gumtree.pl/fp-agd/c9366";

        AdTemplate template = new AdTemplate(type);
        template.setPath("title", "//meta[@property=\'og:title\']/@content");
        template.setPath("description", "//meta[@property=\'og:description\']/@content");
        template.setPath("locality", "//meta[@property=\'og:locality\']/@content");

        MonitoringJob job = new MonitoringJob("gumtreeJob", website, template);

        adsDao.insertJob(job);
    }

    public static void sampleOlxJob(AdsDao adsDao) throws Exception, InvalidDatabaseStateException {
        AdType adType = new AdType(Arrays.asList("title", "price"), "olx-mieszkanie");

        String website = "http://olx.pl/nieruchomosci/mieszkania/";

        AdTemplate template = new AdTemplate(adType);
        template.setPath("title", "//p[@class=\'xx-large fbold lheight26 pdingtop10\']/text()");
        template.setPath("price", "//strong[@class=\'xxxx-large margintop7 block arranged\']/text()");

        MonitoringJob job = new MonitoringJob("olxJob", website, template);

        adsDao.insertType(adType);
        adsDao.insertJob(job);
    }
}
