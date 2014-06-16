package pl.edu.agh.yapa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;
import pl.edu.agh.yapa.service.JobService;

/**
 * @author pawel
 */
@Controller
public class JobsController {

    private final JobService jobService;

    @Autowired
    public JobsController(JobService jobService) {
        this.jobService = jobService;
    }

    @RequestMapping("/jobs")
    public ModelAndView list() throws InvalidDatabaseStateException {
        ModelAndView modelAndView = new ModelAndView("ListJobs");
        modelAndView.addObject("jobs", jobService.getAllJobs());

        return modelAndView;
    }

    @RequestMapping("/jobs/{template}/run")
    public String runJob(@PathVariable("template") String template){
        jobService.runTheOnlyJob();

        return "redirect:" + "/ads";
    }

}
