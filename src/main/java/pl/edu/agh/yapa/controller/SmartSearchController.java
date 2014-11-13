package pl.edu.agh.yapa.controller;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.edu.agh.yapa.conversion.FieldsContainer;
import pl.edu.agh.yapa.model.Ad;
import pl.edu.agh.yapa.model.AdSnapshot;
import pl.edu.agh.yapa.model.AdTemplate;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;
import pl.edu.agh.yapa.service.AdService;
import pl.edu.agh.yapa.service.SearchService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class SmartSearchController {

    private final SearchService searchService;

    @Autowired
    public SmartSearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @RequestMapping(value = "/smartSearch", method = RequestMethod.POST)
    public ModelAndView search(final String query, final HttpServletRequest req) throws InvalidDatabaseStateException {
        ModelAndView modelAndView = new ModelAndView("SmartSearchResults");
        modelAndView.addObject("kwery", query);
        modelAndView.addObject("ads", searchService.smartSearch(query));

        return modelAndView;
    }
}
