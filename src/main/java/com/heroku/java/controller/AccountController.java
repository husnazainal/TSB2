package com.heroku.java.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.heroku.java.model.StaffModel;
import com.heroku.java.repository.StaffRepository;

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

    @GetMapping("/staff/list")
    public String viewAllStaff(@ModelAttribute("loggedInUser") StaffModel loggedInUser, Model model) {
        if (loggedInUser.getStaffId() == null) {
            return "redirect:/loginStaff";
        }

        try {
            List<StaffModel> allStaff = staffRepository.getAllStaff();
            model.addAttribute("staffList", allStaff);
            return "staffList";
        } catch (Exception e) {
            logger.error("Error retrieving staff list", e);
            model.addAttribute("error", "Failed to retrieve staff list. Error: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/viewStaff")
    public String viewStaff(@RequestParam("staffid") Integer staffId, Model model) {
        try {
            StaffModel staff = staffRepository.getStaffById(staffId);
            if (staff == null) {
                model.addAttribute("error", "Staff member not found");
                return "error";
            }
            model.addAttribute("staffModel", staff);
            return "viewStaff";
        } catch (Exception e) {
            logger.error("Error viewing staff", e);
            model.addAttribute("error", "Failed to retrieve staff information. Error: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/updateStaff")
    public String showUpdateForm(@RequestParam("staffid") Integer staffId, Model model) {
        try {
            StaffModel staff = staffRepository.getStaffById(staffId);
            if (staff == null) {
                model.addAttribute("error", "Staff member not found");
                return "error";
            }
            model.addAttribute("staffModel", staff);
            return "editStaff";
        } catch (Exception e) {
            logger.error("Error preparing staff update form", e);
            model.addAttribute("error", "Failed to retrieve staff information. Error: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/updateStaff")
    public String updateStaff(@ModelAttribute("staffModel") StaffModel updatedStaff,
            RedirectAttributes redirectAttributes) {
        try {
            staffRepository.updateStaff(updatedStaff);
            redirectAttributes.addFlashAttribute("message", "Staff updated successfully");
            return "redirect:/staff/list";
        } catch (Exception e) {
            logger.error("Error updating staff", e);
            redirectAttributes.addFlashAttribute("error", "Update failed. Error: " + e.getMessage());
            return "redirect:/updateStaff?staffid=" + updatedStaff.getStaffId();
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
    public String registerStaff(@ModelAttribute StaffModel staffModel, RedirectAttributes redirectAttributes) {
        try {
            staffRepository.saveStaff(staffModel);
            redirectAttributes.addFlashAttribute("message", "Registration successful. Please log in.");
            return "redirect:/loginStaff";
        } catch (Exception e) {
            logger.error("Error registering staff", e);
            redirectAttributes.addFlashAttribute("error", "Registration failed. Error: " + e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(@ModelAttribute("loggedInUser") StaffModel loggedInUser) {
        if (loggedInUser.getStaffId() == null) {
            return "redirect:/loginStaff";
        }
        return "dashboard";
    }

    // @GetMapping("/loginStaff")
    // public String showLoginForm(Model model) {
    //     if (!model.containsAttribute("staffModel")) {
    //         model.addAttribute("staffModel", new StaffModel());
    //     }
    //     return "loginStaff";
    // }
    // @PostMapping("/loginStaff")
    // public String loginStaff(@ModelAttribute("staffModel") StaffModel staffModel,
    //         @ModelAttribute("loggedInUser") StaffModel loggedInUser,
    //         RedirectAttributes redirectAttributes) {
    //     try {
    //         StaffModel authenticatedStaff = staffRepository.authenticateStaff(staffModel.getStaffEmail(), staffModel.getStaffPassword());
    //         if (authenticatedStaff != null) {
    //             // Update session
    //             loggedInUser.setStaffId(authenticatedStaff.getStaffId());
    //             loggedInUser.setStaffName(authenticatedStaff.getStaffName());
    //             loggedInUser.setStaffEmail(authenticatedStaff.getStaffEmail());
    //             return "redirect:/dashboard";
    //         } else {
    //             redirectAttributes.addFlashAttribute("error", "Invalid email or password");
    //             return "redirect:/loginStaff";
    //         }
    //     } catch (Exception e) {
    //         logger.error("Error during login", e);
    //         redirectAttributes.addFlashAttribute("error", "Login failed. Error: " + e.getMessage());
    //         return "redirect:/loginStaff";
    //     }
    // }
}
