package pl.edu.agh.yapa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.edu.agh.yapa.model.AdSnapshot;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;
import pl.edu.agh.yapa.service.AdService;

/**
 * @author pawel
 */
@Controller
public class AdsController {
    private final AdService adService;

    @Autowired
    public AdsController(AdService adService) {
        this.adService = adService;
    }

//    @RequestMapping("/ads")
//    public ModelAndView list() throws InvalidDatabaseStateException {
//        ModelAndView modelAndView = new ModelAndView("ListAds");
//        modelAndView.addObject("ads", adService.getAds());
//        List<AdSnapshot> snapshots = AdSnapshot.groupBySnapshots(adService.getAds());
//
//        return modelAndView;
//    }

    @RequestMapping("/ads")
    public ModelAndView list() throws InvalidDatabaseStateException {
        ModelAndView modelAndView = new ModelAndView("ListAdsBySnapshots");
        modelAndView.addObject("snapshots", AdSnapshot.groupBySnapshots(adService.getAds()));

        return modelAndView;
    }
}
