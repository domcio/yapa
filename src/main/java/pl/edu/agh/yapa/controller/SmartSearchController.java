package pl.edu.agh.yapa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.edu.agh.yapa.service.SearchService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SmartSearchController {

    private final SearchService searchService;

    @Autowired
    public SmartSearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @RequestMapping(value = "/smartSearch", method = RequestMethod.POST)
    public ModelAndView search(final String query, final HttpServletRequest req) throws Exception {
        ModelAndView modelAndView = new ModelAndView("SmartSearchResults");
        modelAndView.addObject("kwery", query);
        modelAndView.addObject("ads", searchService.smartSearch(query));

        return modelAndView;
    }
}
