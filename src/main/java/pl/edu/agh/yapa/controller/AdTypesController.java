package pl.edu.agh.yapa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.edu.agh.yapa.model.AdType;
import pl.edu.agh.yapa.service.AdService;

import javax.servlet.http.HttpServletRequest;

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
    public ModelAndView list() throws Exception {
        ModelAndView modelAndView = new ModelAndView("ListAdTypes");
        modelAndView.addObject("types", adService.getAdTypes());

        return modelAndView;
    }


    @RequestMapping(value = "/types/{type}/remove")
    public String removeType(@PathVariable("type") String typeName, final HttpServletRequest request) {
        System.out.println("Usuwam typ " + typeName);
        adService.removeAdType(typeName);
        return "redirect:/types";
    }

    @RequestMapping(value = "/addType", method = RequestMethod.GET)
    public String showForm(Model model) {
        model.addAttribute("adType", new AdType());
        return "AddAdType";
    }

    @RequestMapping(value = "/addType", params = {"addField"})
    public String addRow(final AdType adType, final BindingResult bindingResult, final HttpServletRequest req) {
        adType.addField(req.getParameter("addField"));
        return "AddAdType";
    }

    @RequestMapping(value = "/addType", params = {"removeField"})
    public String removeRow(
            final AdType adType, final BindingResult bindingResult, final HttpServletRequest req) {
        adType.removeField(Integer.parseInt(req.getParameter("removeField")));
        return "AddAdType";
    }

    @RequestMapping(value = "/addType")
    public String submitType(final AdType adType, final HttpServletRequest req) throws Exception {
        adService.insertAdType(adType);
        return "redirect:/types";
    }
}
