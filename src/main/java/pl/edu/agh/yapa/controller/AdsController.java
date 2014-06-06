package pl.edu.agh.yapa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author pawel
 */
@Controller
public class AdsController {

    @RequestMapping("/ads")
    public ModelAndView list(){
        ModelAndView modelAndView = new ModelAndView("ListAds");

        //TODO: cool things here

        return modelAndView;
    }

}
