package pl.edu.agh.yapa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;
import pl.edu.agh.yapa.service.AdService;

/**
 * Created by Dominik on 09.06.2014.
 */
@Controller
public class AdTemplatesController {
    private final AdService adService;

    @Autowired
    public AdTemplatesController(AdService adService) {
        this.adService = adService;
    }

    @RequestMapping("/templates")
    public ModelAndView list() throws InvalidDatabaseStateException {
        ModelAndView modelAndView = new ModelAndView("ListTemplates");
        modelAndView.addObject("templates", adService.getTemplates());

        return modelAndView;
    }
}
