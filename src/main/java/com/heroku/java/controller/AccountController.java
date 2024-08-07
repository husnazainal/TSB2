package com.heroku.java.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    // @GetMapping("/account/edit")
    // public String showEditForm(@ModelAttribute("loggedInUser") StaffModel loggedInUser, Model model) {
    //     if (loggedInUser.getStaffId() == null) {
    //         return "redirect:/loginStaff";
    //     }
    //     model.addAttribute("staffModel", loggedInUser);
    //     return "editAccount";
    // }
    // @PostMapping("/account/update")
    // public String updateAccount(@ModelAttribute("staffModel") StaffModel updatedStaff,
    //                             @ModelAttribute("loggedInUser") StaffModel loggedInUser,
    //                             RedirectAttributes redirectAttributes) {
    //     if (loggedInUser.getStaffId() == null) {
    //         return "redirect:/loginStaff";
    //     }
    //     try {
    //         // Update only allowed fields
    //         loggedInUser.setStaffName(updatedStaff.getStaffName());
    //         loggedInUser.setStaffEmail(updatedStaff.getStaffEmail());
    //         // Add more fields as needed, but be cautious with sensitive data
    //         staffRepository.updateStaff(loggedInUser);
    //         redirectAttributes.addFlashAttribute("message", "Account updated successfully");
    //         return "redirect:/account";
    //     } catch (Exception e) {
    //         logger.error("Error updating account", e);
    //         redirectAttributes.addFlashAttribute("error", "Update failed. Error: " + e.getMessage());
    //         return "redirect:/account/edit";
    //     }
    // }
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
    public String dashboard(Model model, HttpSession session) {
        Long staffId = (Long) session.getAttribute(staffloginController.SESSION_STAFF_ID);
        if (staffId == null) {
            return "redirect:/loginStaff";
        }

        StaffModel loggedInUser = staffRepository.getStaffById(staffId);
        model.addAttribute("loggedInUser", loggedInUser);

        return "dashboard";
    }

    @GetMapping("/stafflist")
    public String listStaff(Model model, HttpSession session) {
        // Check if user is logged in
        if (session.getAttribute(staffloginController.SESSION_STAFF_ID) == null) {
            return "redirect:/loginStaff";
        }

        try {
            List<StaffModel> staffList = staffRepository.getAllStaff();
            model.addAttribute("staffList", staffList);
            return "stafflist";
        } catch (Exception e) {
            logger.error("Error retrieving staff list", e);
            model.addAttribute("error", "An error occurred while retrieving the staff list");
            return "error";
        }
    }

    @GetMapping("/viewStaff")
    public String viewStaff(@RequestParam("staffid") Long staffId, Model model, HttpSession session) {
        if (session.getAttribute(staffloginController.SESSION_STAFF_ID) == null) {
            return "redirect:/loginStaff";
        }

        try {
            StaffModel staff = staffRepository.getStaffById(staffId);
            if (staff == null) {
                model.addAttribute("error", "Staff not found");
                return "error";
            }
            model.addAttribute("staffModel", staff);
            return "viewStaff";
        } catch (Exception e) {
            logger.error("Error viewing staff", e);
            model.addAttribute("error", "An error occurred while retrieving staff information");
            return "error";
        }
    }

    @GetMapping("/updateStaff")
    public String showUpdateStaffForm(@RequestParam("staffid") Long staffId, Model model, HttpSession session) {
        if (session.getAttribute(staffloginController.SESSION_STAFF_ID) == null) {
            return "redirect:/loginStaff";
        }

        try {
            StaffModel staff = staffRepository.getStaffById(staffId);
            if (staff == null) {
                model.addAttribute("error", "Staff not found");
                return "error";
            }
            model.addAttribute("staffModel", staff);
            return "updateStaff";
        } catch (Exception e) {
            logger.error("Error preparing update form", e);
            model.addAttribute("error", "An error occurred while preparing the update form");
            return "error";
        }
    }

    @PostMapping("/updateStaff")
    public String updateStaff(@ModelAttribute("staffModel") StaffModel updatedStaff,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        if (session.getAttribute(staffloginController.SESSION_STAFF_ID) == null) {
            return "redirect:/loginStaff";
        }

        try {
            StaffModel existingStaff = staffRepository.getStaffById(updatedStaff.getStaffId());
            if (existingStaff == null) {
                redirectAttributes.addFlashAttribute("error", "Staff not found");
                return "redirect:/viewStaff";
            }

            existingStaff.setStaffName(updatedStaff.getStaffName());
            existingStaff.setStaffEmail(updatedStaff.getStaffEmail());
            // Only update password if a new one is provided
            if (updatedStaff.getStaffPassword() != null && !updatedStaff.getStaffPassword().isEmpty()) {
                existingStaff.setStaffPassword(updatedStaff.getStaffPassword());
            }

            staffRepository.updateStaff(existingStaff);
            redirectAttributes.addFlashAttribute("message", "Staff updated successfully");
            return "redirect:/viewStaff";
        } catch (Exception e) {
            logger.error("Error updating staff", e);
            redirectAttributes.addFlashAttribute("error", "Update failed. Error: " + e.getMessage());
            return "redirect:/updateStaff?staffid=" + updatedStaff.getStaffId();
        }
    }

    @PostMapping("/deleteStaff")
    public String deleteStaff(@RequestParam("staffid") Long staffId, RedirectAttributes redirectAttributes, HttpSession session) {
        if (session.getAttribute(staffloginController.SESSION_STAFF_ID) == null) {
            return "redirect:/loginStaff";
        }

        try {
            StaffModel staff = staffRepository.getStaffById(staffId);
            if (staff == null) {
                redirectAttributes.addFlashAttribute("error", "Staff not found");
                return "redirect:/stafflist";
            }

            staffRepository.deleteStaff(staffId);
            redirectAttributes.addFlashAttribute("message", "Staff deleted successfully");
            return "redirect:/stafflist";
        } catch (Exception e) {
            logger.error("Error deleting staff", e);
            redirectAttributes.addFlashAttribute("error", "Delete failed. Error: " + e.getMessage());
            return "redirect:/stafflist";
        }
    }
}
