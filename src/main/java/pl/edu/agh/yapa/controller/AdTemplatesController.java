package pl.edu.agh.yapa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;
import pl.edu.agh.yapa.service.AdService;

import javax.servlet.http.HttpServletRequest;

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

    @RequestMapping(value = "/showSelect", method = RequestMethod.GET)
    public ModelAndView showSelect() throws InvalidDatabaseStateException {
        ModelAndView modelAndView = new ModelAndView("SelectAdType");
        modelAndView.addObject("types", adService.getAdTypes());
        modelAndView.addObject("template", new AdTemplate());

        return modelAndView;
    }

    @RequestMapping(value = "/processSelect", method = RequestMethod.GET)
    public ModelAndView showCostam() {
        ModelAndView modelAndView = new ModelAndView("AddAdTemplate");

        return modelAndView;
    }

    @RequestMapping(value = "/processSelect", method = RequestMethod.POST)
    public ModelAndView processSelect(final AdTemplate template, final HttpServletRequest req) throws InvalidDatabaseStateException {
        System.out.println("In processSelect()...");
        System.out.println(template.getType());

        ModelAndView modelAndView = new ModelAndView("AddAdTemplate");
        modelAndView.addObject("template", template);

        return modelAndView;
    }

//    @RequestMapping(value = "/processSelect", method = RequestMethod.POST)
//    public String processTemplate(final AdTemplate template) {
//        System.out.println(template);
//        return "redirect:/templates";
//    }
}
