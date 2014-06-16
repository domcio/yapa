package pl.edu.agh.yapa.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.yapa.extraction.ExtractionEngine;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.model.AdType;
import pl.edu.agh.yapa.model.MonitoringJob;
import pl.edu.agh.yapa.model.Website;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;

import java.util.Arrays;
import java.util.List;

/**
 * @author Piotr Góralczyk
 */
@Service
public class JobService {

    public List<String> getAllJobs() throws InvalidDatabaseStateException {
        //TODO: ask DAO
        return Arrays.asList( "GumtreeJob" );
    }

    public void runTheOnlyJob(){
        final MonitoringJob job = createTheOnlyJob();

        new Thread( new Runnable() {
            @Override
            public void run() {
                try {
                    job.update();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } ).start();
    }

    private MonitoringJob createTheOnlyJob(){
        AdType type = new AdType(Arrays.asList("title", "description", "locality"), "gumtreeAGDAd");
        ExtractionEngine engine = new ExtractionEngine();

        Website website = new Website("http://www.gumtree.pl/fp-agd/c9366");
        website.addSubURLXPath("//a[@class=\'adLinkSB\']/@href");
        website.setNextPageXPath("//a[@class=\'prevNextLink\'][contains(., 'Nast')]/@href");

        AdTemplate template = new AdTemplate(type);
        template.setPath("title", "//meta[@property=\'og:title\']/@content");
        template.setPath("description", "//meta[@property=\'og:description\']/@content");
        template.setPath("locality", "//meta[@property=\'og:locality\']/@content");

        return new MonitoringJob(website, template, engine, 100);
    }

}
