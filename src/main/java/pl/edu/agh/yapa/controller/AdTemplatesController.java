package pl.edu.agh.yapa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.edu.agh.yapa.conversion.FieldsContainer;
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
        modelAndView.addObject("templates", adService.getAdTemplates());

        return modelAndView;
    }

    @RequestMapping(value = "/showSelect", method = RequestMethod.GET)
    public String showSelect(Model model) throws InvalidDatabaseStateException {
        model.addAttribute("types", adService.getAdTypes());
        model.addAttribute("template", new AdTemplate());

        return "SelectAdType";
    }

    @RequestMapping(value = "/showSelect", params = {"submitType"})
    public String submitType(final AdTemplate adTemplate, final BindingResult bindingResult, final HttpServletRequest req, Model model) {
        if (bindingResult.hasErrors()) {
            System.out.println("Binding rezult ma errory");
            return "SelectAdType";
        }

        FieldsContainer fieldsContainer = new FieldsContainer(adTemplate.getType().getFields().size());
        fieldsContainer.setAdType(adTemplate.getType());
        model.addAttribute("fieldContainer", fieldsContainer);

        return "AddAdTemplate";
    }

    @RequestMapping(value = "/processSelect", method = RequestMethod.POST)
    public String processSelect(final AdTemplate template, final FieldsContainer container, final BindingResult bindingResult, final HttpServletRequest req) throws InvalidDatabaseStateException {
        if (bindingResult.hasErrors()) {
            System.out.println("Binding rezult ma errory");
            return "SelectAdType";
        }

        System.out.println(template);
        System.out.println(container);

        adService.insertAdTemplate(new AdTemplate(container.getAdType(), container.getFieldXPaths()));

        return "redirect:/templates";
    }
}
