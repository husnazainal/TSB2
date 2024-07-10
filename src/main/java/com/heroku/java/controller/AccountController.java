package com.heroku.java.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.heroku.java.model.StaffModel;
import com.heroku.java.repository.StaffRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("loggedInUser")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    private final StaffRepository staffRepository;

    @Autowired
    public AccountController(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @ModelAttribute("loggedInUser")
    public StaffModel loggedInUser() {
        return new StaffModel();
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        logger.debug("Showing registration form");
        if (!model.containsAttribute("staffModel")) {
            model.addAttribute("staffModel", new StaffModel());
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerStaff(@ModelAttribute StaffModel staffModel, RedirectAttributes redirectAttributes) {
        try {
            staffRepository.saveStaff(staffModel);
            redirectAttributes.addFlashAttribute("message", "Registration successful. Please log in.");
            return "redirect:/loginStaff";
        } catch (Exception e) {
            logger.error("Error registering staff", e);
            redirectAttributes.addFlashAttribute("error", "Registration failed. Error: " + e.getMessage());
            return "redirect:/register?error";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Check if user is logged in
        if (session.getAttribute(staffloginController.SESSION_STAFF_ID) == null) {
            return "redirect:/loginStaff";
        }
        // Add any necessary attributes to the model
        return "dashboard";
    }
}
