package pl.edu.agh.yapa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by piotrek on 09.01.15.
 */
@Controller
public class HomeController {
    @RequestMapping("/")
    public ModelAndView home() throws Exception {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("message", "hiho");

        return modelAndView;
    }
}
