package pl.edu.agh.yapa.conversion;

import org.springframework.format.Formatter;
import pl.edu.agh.yapa.model.AdType;
import pl.edu.agh.yapa.persistence.AdsDao;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Dominik on 16.06.2014.
 */
public class AdTypeFormatter implements Formatter<AdType> {
    private AdsDao adsDao;

    public AdTypeFormatter(AdsDao adsDao) {
        this.adsDao = adsDao;
    }

    public AdTypeFormatter() {
        this.adsDao = null;
    }

    @Override
    public AdType parse(String s, Locale locale) throws ParseException {
        try {
            System.out.println("Wolane parse z stringiem: "+ s);
            return adsDao.getTypeByName(s);
        } catch (InvalidDatabaseStateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String print(AdType adType, Locale locale) {
        System.out.println("Wolane print z typem: "+ adType.getName());
        return adType.getName();
    }
}
