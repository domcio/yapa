package pl.edu.agh.yapa.conversion;

import org.springframework.format.Formatter;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.persistence.AdsDao;

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
        return null;
    }

    @Override
    public String print(AdTemplate adTemplate, Locale locale) {
        return null;
    }
}
