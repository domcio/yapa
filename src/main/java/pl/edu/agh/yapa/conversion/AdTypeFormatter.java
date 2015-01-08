package pl.edu.agh.yapa.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import pl.edu.agh.yapa.model.AdType;
import pl.edu.agh.yapa.persistence.AdTypeDao;
import pl.edu.agh.yapa.persistence.AdsDao;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Dominik on 16.06.2014.
 */
public class AdTypeFormatter implements Formatter<AdType> {
    // TODO move to service layer
    private AdTypeDao adTypeDao;

    @Autowired
    public AdTypeFormatter(AdTypeDao adTypeDao) {
        this.adTypeDao = adTypeDao;
    }

    public AdTypeFormatter() {
        this.adTypeDao = null;
    }

    @Override
    public AdType parse(String s, Locale locale) throws ParseException {
        try {
            return adTypeDao.getTypeByName(s);
        } catch (InvalidDatabaseStateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String print(AdType adType, Locale locale) {
        return adType.getName();
    }
}
