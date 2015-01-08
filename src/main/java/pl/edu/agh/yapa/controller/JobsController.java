package pl.edu.agh.yapa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.edu.agh.yapa.conversion.JobFieldsContainer;
import pl.edu.agh.yapa.model.MonitoringJob;
import pl.edu.agh.yapa.persistence.InvalidDatabaseStateException;
import pl.edu.agh.yapa.service.AdService;
import pl.edu.agh.yapa.service.JobService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author pawel
 */
@Controller
public class JobsController {

    private final JobService jobService;
    private final AdService adService;

    @Autowired
    public JobsController(JobService jobService, AdService adService) {
        this.jobService = jobService;
        this.adService = adService;
    }

    @RequestMapping("/jobs")
    public ModelAndView list() throws Exception {
        ModelAndView modelAndView = new ModelAndView("ListJobs");
        modelAndView.addObject("jobs", jobService.getJobsAndStatuses());

        return modelAndView;
    }

    @RequestMapping("/jobs/{name}/run")
    public String runJob(@PathVariable("name") String name) throws Exception {
        System.out.println("running: " + name);
        jobService.runJob(name);

        return "redirect:" + "/jobs";
    }

    @RequestMapping("/jobs/{name}/activate")
    public String activateJob(@PathVariable("name") String name) throws Exception {
        System.out.println("Activated: " + name);
        jobService.activateJob(name);

        return "redirect:" + "/ads";
    }

    @RequestMapping("/jobs/{name}/deactivate")
    public String deactivateJob(@PathVariable("name") String name) throws Exception {
        System.out.println("Deactivated: " + name);
        jobService.deactivateJob(name);

        return "redirect:" + "/ads";
    }


    @RequestMapping(value = "/selectTemplate", method = RequestMethod.GET)
    public String selectTemplate(Model model) throws InvalidDatabaseStateException {
        model.addAttribute("templates", adService.getTemplates());
        model.addAttribute("monitoringJob", new MonitoringJob());

        return "SelectAdTemplate";
    }

    @RequestMapping(value = "/selectTemplate", params = {"submitTemplate"})
    public String submitTemplate(final MonitoringJob monitoringJob, final BindingResult bindingResult, final HttpServletRequest req, Model model) {
        if (bindingResult.hasErrors()) {
            System.out.println(monitoringJob.getName());
            System.out.println("Binding rezult ma errory");
            return "SelectAdTemplate";
        }

        JobFieldsContainer container = new JobFieldsContainer();
        container.setTemplate(monitoringJob.getTemplate());
        model.addAttribute("container", container);

        return "AddJob";
    }

    @RequestMapping(value = "/processJob", method = RequestMethod.POST)
    public String processSelect(final JobFieldsContainer container, final BindingResult bindingResult, final HttpServletRequest req) throws InvalidDatabaseStateException {
        if (bindingResult.hasErrors()) {
            System.out.println("Binding rezult ma errory");
            return "SelectAdTemplate";
        }

        MonitoringJob job = new MonitoringJob();
        job.setTemplate(container.getTemplate());
        job.setName(container.getName());
        job.setWebsite(container.getUrl());
        jobService.addJob(job);

        return "redirect:/jobs";
    }
}
