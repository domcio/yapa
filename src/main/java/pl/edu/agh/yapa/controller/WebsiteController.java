package pl.edu.agh.yapa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.edu.agh.yapa.model.Website;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;
import pl.edu.agh.yapa.service.WebsiteService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Dominik on 18.06.2014.
 */
@Controller
public class WebsiteController {

    private final WebsiteService websiteService;

    @Autowired
    public WebsiteController(WebsiteService websiteService) {
        this.websiteService = websiteService;
    }

    @RequestMapping(value = "/websites")
    public ModelAndView listWebsites() {
        ModelAndView modelAndView = new ModelAndView("ListWebsites");
        modelAndView.addObject("websites", websiteService.getWebsites());

        return modelAndView;
    }


    @RequestMapping(value = "/addWebsite", method = RequestMethod.GET)
    public String showForm(Model model) {
        model.addAttribute("website", new Website());
        return "AddWebsite";
    }

    @RequestMapping(value = "/addWebsite", params = {"addField"})
    public String addRow(final Website website, final BindingResult bindingResult, final HttpServletRequest req) {
        website.addSubURLXPath(req.getParameter("addField"));
        return "AddWebsite";
    }

    @RequestMapping(value = "/addWebsite", params = {"removeField"})
    public String removeRow(
            final Website website, final BindingResult bindingResult, final HttpServletRequest req) {
        website.removeField(Integer.parseInt(req.getParameter("removeField")));
        return "AddWebsite";
    }

    @RequestMapping(value = "/addWebsite")
    public String submitType(final Website website, final HttpServletRequest req) throws InvalidDatabaseStateException {
        websiteService.insertWebsite(website);
        return "redirect:/websites";
    }
}
