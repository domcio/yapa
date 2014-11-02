package pl.edu.agh.yapa.conversion;

import org.springframework.format.Formatter;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.model.AdType;
import pl.edu.agh.yapa.persistence.AdsDao;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Dominik on 16.06.2014.
 */
public class AdTemplateFormatter implements Formatter<AdTemplate> {
    private AdsDao adsDao;

    public AdTemplateFormatter(AdsDao adsDao) {
        this.adsDao = adsDao;
    }

    @Override
    public AdTemplate parse(String s, Locale locale) throws ParseException {
        String[] strs = s.split(":");
        AdTemplate template = null;
        try {
            template = new AdTemplate();
            AdType type = adsDao.getTypeByName(strs[0]);
            template.setType(type);

            for (int i = 1; i < strs.length - 1; i += 2) {
                template.setPath(strs[i], strs[i + 1]);
            }
        } catch (InvalidDatabaseStateException e) {
            e.printStackTrace();
        }
        return template;
    }

    @Override
    public String print(AdTemplate adTemplate, Locale locale) {
        return adTemplate.print();
    }
}
