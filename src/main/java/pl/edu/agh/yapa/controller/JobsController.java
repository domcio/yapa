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
        modelAndView.addObject("jobs", jobService.getJobsAndStatuses());

        return modelAndView;
    }

    @RequestMapping("/jobs/{name}/run")
    public String runJob(@PathVariable("name") String name) throws InvalidDatabaseStateException, Exception {
        System.out.println("running: " + name);
        jobService.runJob(name);

        return "redirect:" + "/ads";
    }

    @RequestMapping("/jobs/{name}/activate")
    public String activateJob(@PathVariable("name") String name) throws InvalidDatabaseStateException, Exception {
        System.out.println("Activated: " + name);
        jobService.activateJob(name);

        return "redirect:" + "/ads";
    }

    @RequestMapping("/jobs/{name}/deactivate")
    public String deactivateJob(@PathVariable("name") String name) throws InvalidDatabaseStateException, Exception {
        System.out.println("Deactivated: " + name);
        jobService.deactivateJob(name);

        return "redirect:" + "/ads";
    }
}
