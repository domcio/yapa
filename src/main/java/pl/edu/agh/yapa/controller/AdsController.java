package pl.edu.agh.yapa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.edu.agh.yapa.persistence.AdsDao;

/**
 * @author pawel
 */
@Controller
public class AdsController {
    private final AdsDao adsDao;

    @Autowired
    public AdsController(AdsDao adsDao) {
        this.adsDao = adsDao;
    }

    @RequestMapping("/ads")
    public ModelAndView list(){
        ModelAndView modelAndView = new ModelAndView("ListAds");

        //TODO: cool things here

        return modelAndView;
    }

}
