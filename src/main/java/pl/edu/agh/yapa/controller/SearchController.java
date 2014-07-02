package pl.edu.agh.yapa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.edu.agh.yapa.conversion.FieldsContainer;
import pl.edu.agh.yapa.model.Ad;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;
import pl.edu.agh.yapa.service.AdService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Dominik on 02.07.2014.
 */
@Controller
public class SearchController {
    private final AdService adService;

    @Autowired
    public SearchController(AdService adService) {
        this.adService = adService;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView selectType() throws InvalidDatabaseStateException {
        ModelAndView modelAndView = new ModelAndView("SelectAdTypeForSearch");
        modelAndView.addObject("types", adService.getAdTypes());
        modelAndView.addObject("template", new AdTemplate());

        return modelAndView;
    }

    @RequestMapping(value = "/search", params = {"submitType"})
    public String submitType(final AdTemplate adTemplate, final BindingResult bindingResult, final HttpServletRequest req, Model model) {
        FieldsContainer fieldsContainer = new FieldsContainer(adTemplate.getType().getFields().size());
        fieldsContainer.setAdType(adTemplate.getType());
        model.addAttribute("fieldContainer", fieldsContainer);
        return "AdSearch";
    }

    @RequestMapping(value = "/processSearch", method = RequestMethod.POST)
    public ModelAndView processSearch(final AdTemplate template, final FieldsContainer container, final BindingResult bindingResult, final HttpServletRequest req) throws InvalidDatabaseStateException {
        List<Ad> ads = adService.search(container);
        ModelAndView modelAndView = new ModelAndView("ListAds");
        modelAndView.addObject("ads", ads);

        return modelAndView;
    }
}
