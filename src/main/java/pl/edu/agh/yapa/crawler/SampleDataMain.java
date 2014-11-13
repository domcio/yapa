package pl.edu.agh.yapa.crawler;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import pl.edu.agh.yapa.persistence.AdsDao;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;

/**
 * Created with IntelliJ IDEA.
 * User: Dominik
 * Date: 19.05.14
 * Time: 23:54
 */
public class SampleDataMain {
    public static void main(String[] args) {
        ApplicationContext ctx = new FileSystemXmlApplicationContext("src\\main\\webapp\\WEB-INF\\applicationContext.xml");
        AdsDao adsDao = (AdsDao) ctx.getBean("adsDao");

        String line;
        java.io.BufferedReader in = new java.io.BufferedReader(
                new java.io.InputStreamReader(System.in));
        try {
            do {
                System.out.println("(c) clear database (i) insert sample data (x) exit\n>");
                System.out.flush();
                line = in.readLine();
                if (line.equals("i")) {
                    Construct.sampleGumtreeJob(adsDao);
                    Construct.sampleOlxJob(adsDao);
                } else if (line.equals("c")) {
                    adsDao.clear();
                }
            }
            while (!line.equals("x"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
