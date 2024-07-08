package com.heroku.java.controller;

import com.heroku.java.model.StaffModel;
import com.heroku.java.repository.StaffRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    private final StaffRepository staffRepository;

    @Autowired
    public AccountController(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        logger.debug("Showing registration form");
        model.addAttribute("staffModel", new StaffModel());
        return "register";
    }

    @PostMapping("/register")
    public String registerStaff(@ModelAttribute("staffModel") StaffModel staffModel, HttpSession session) {
        logger.debug("Registering staff: {}", staffModel);
        staffRepository.saveStaff(staffModel);

        session.setAttribute("loggedInUser", staffModel);

        return "redirect:/staff/dashboard";
    }
}