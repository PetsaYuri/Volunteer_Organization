package com.volunteer.Volunteer.Organization.controllers;

import com.volunteer.Volunteer.Organization.models.ProjectInfo;
import com.volunteer.Volunteer.Organization.repository.ProjectInfoRepository;
import com.volunteer.Volunteer.Organization.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @Autowired
    private ProjectInfoRepository projectInfoRepository;

    @Autowired
    private MainService mainService;

    @GetMapping("/error")
    public String handleError(Model model) {
        ProjectInfo projectInfo = projectInfoRepository.getById(Long.valueOf(1));
        model.addAttribute("projectInfo", projectInfo);
        model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
        return "page_error";
    }
}
