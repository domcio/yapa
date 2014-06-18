package pl.edu.agh.yapa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.edu.agh.yapa.service.WebsiteService;

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
}
