package pl.edu.agh.yapa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;
import pl.edu.agh.yapa.service.AdService;

/**
 * Created by Dominik on 07.06.2014.
 */
@Controller
public class AdTypesController {
    private final AdService adService;

    @Autowired
    public AdTypesController(AdService adService) {
        this.adService = adService;
    }

    @RequestMapping("/types")
    public ModelAndView list() throws InvalidDatabaseStateException {
        ModelAndView modelAndView = new ModelAndView("ListAdTypes");
        modelAndView.addObject("types", adService.getAdTypes());

        return modelAndView;
    }
}
