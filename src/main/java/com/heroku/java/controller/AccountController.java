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

    @GetMapping("/account")
    public String viewAccount(@ModelAttribute("loggedInUser") StaffModel loggedInUser, Model model) {
        if (loggedInUser.getStaffId() == null) {
            return "redirect:/loginStaff";
        }
        model.addAttribute("staffModel", loggedInUser);
        return "account";
    }
    @GetMapping("/account/edit")
    public String showEditForm(@ModelAttribute("loggedInUser") StaffModel loggedInUser, Model model) {
        if (loggedInUser.getStaffId() == null) {
            return "redirect:/loginStaff";
        }
        model.addAttribute("staffModel", loggedInUser);
        return "editAccount";
    }
    @PostMapping("/account/update")
    public String updateAccount(@ModelAttribute("staffModel") StaffModel updatedStaff,
                                @ModelAttribute("loggedInUser") StaffModel loggedInUser,
                                RedirectAttributes redirectAttributes) {
        if (loggedInUser.getStaffId() == null) {
            return "redirect:/loginStaff";
        }

        try {
            // Update only allowed fields
            loggedInUser.setStaffName(updatedStaff.getStaffName());
            loggedInUser.setStaffEmail(updatedStaff.getStaffEmail());
            // Add more fields as needed, but be cautious with sensitive data

            staffRepository.updateStaff(loggedInUser);
            redirectAttributes.addFlashAttribute("message", "Account updated successfully");
            return "redirect:/account";
        } catch (Exception e) {
            logger.error("Error updating account", e);
            redirectAttributes.addFlashAttribute("error", "Update failed. Error: " + e.getMessage());
            return "redirect:/account/edit";
        }
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
    public String registerStaff(@ModelAttribute("staffModel") StaffModel staffModel,
            @ModelAttribute("loggedInUser") StaffModel loggedInUser,
            RedirectAttributes redirectAttributes) {
        logger.debug("Registering staff: {}", staffModel);
        try {
            staffRepository.saveStaff(staffModel);
            logger.debug("Staff saved successfully");
            // Update the session attribute
            loggedInUser.setStaffId(staffModel.getStaffId());
            loggedInUser.setStaffName(staffModel.getStaffName());
            loggedInUser.setStaffEmail(staffModel.getStaffEmail());
            // Don't set the password in the session
            return "redirect:/dashboard"; // Ensure this matches your actual mapping
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
