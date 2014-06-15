package pl.edu.agh.yapa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    public String showSelect(Model model) throws InvalidDatabaseStateException {
        model.addAttribute("types", adService.getAdTypes());
        model.addAttribute("template", new AdTemplate());
        return "SelectAdType";
    }

    @RequestMapping(value = "/showSelect", method = RequestMethod.POST)
    public String processSelect(@ModelAttribute(value = "template") final AdTemplate template, final HttpServletRequest req) throws InvalidDatabaseStateException {
        System.out.println(template.getType());
        return "AddAdTemplate";
    }

    @RequestMapping(value = "/processSelect", method = RequestMethod.POST)
    public String processTemplate(final AdTemplate template) {
        System.out.println(template);
        return "redirect:/templates";
    }
}
