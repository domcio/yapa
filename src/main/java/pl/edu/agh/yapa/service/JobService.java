package pl.edu.agh.yapa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.yapa.extraction.JobTask;
import pl.edu.agh.yapa.extraction.XPathEngine;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.model.AdType;
import pl.edu.agh.yapa.model.MonitoringJob;
import pl.edu.agh.yapa.model.Website;
import pl.edu.agh.yapa.persistence.AdsDao;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;

import java.util.Arrays;
import java.util.List;

/**
 * @author Piotr Góralczyk
 */
@Service
public class JobService {
    private final AdsDao adsDao;

    @Autowired
    public JobService(AdsDao adsDao) {
        this.adsDao = adsDao;
    }

    public List<String> getAllJobsNames() throws InvalidDatabaseStateException {
        return adsDao.getJobsNames();
    }

    private MonitoringJob createTheOnlyJob() {
        AdType type = new AdType(Arrays.asList("title", "description", "locality"), "gumtreeAGDAd");
        XPathEngine engine = new XPathEngine();

        Website website = new Website("http://www.gumtree.pl/fp-agd/c9366");
        website.addSubURLXPath("//a[@class=\'adLinkSB\']/@href");
        website.setNextPageXPath("//a[@class=\'prevNextLink\'][contains(., 'Nast')]/@href");

        AdTemplate template = new AdTemplate(type);
        template.setPath("title", "//meta[@property=\'og:title\']/@content");
        template.setPath("description", "//meta[@property=\'og:description\']/@content");
        template.setPath("locality", "//meta[@property=\'og:locality\']/@content");

        return new MonitoringJob("gumtreeJob", website, template, engine, 100);
    }

    public void runJob(String jobName) throws InvalidDatabaseStateException, Exception {
        MonitoringJob job = adsDao.getJobByName(jobName);
        new Thread(new JobTask(adsDao, job)).start();
    }
}
