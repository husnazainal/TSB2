package com.heroku.java.controller;

import javax.sql.DataSource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.heroku.java.model.Staff;
import com.heroku.java.model.StaffModel;

import jakarta.servlet.http.HttpSession;

@Controller
public class staffloginController {
    private final DataSource dataSource;

    // Constants for session attribute names
    public static final String SESSION_STAFF_ID = "staffid";
    public static final String SESSION_STAFF_EMAIL = "loggedInStaff";

    public staffloginController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/loginStaff")
    public String loginForm(Model model) {
        if (!model.containsAttribute("staffModel")) {
            model.addAttribute("staffModel", new StaffModel());
        }
        return "loginStaff";
    }

    @PostMapping("/loginStaff")
    public String login(@ModelAttribute("staffModel") StaffModel staffModel, 
                        HttpSession session,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        try {
            StaffModel loggedInStaff = StaffDAO.getStaffByStaffemail(dataSource, staffModel.getStaffEmail());
            if (loggedInStaff == null || !loggedInStaff.getStaffPassword().equals(staffModel.getStaffPassword())) {
                model.addAttribute("error", "Invalid email or password. Please try again.");
                return "loginStaff";
            }
            session.setAttribute(SESSION_STAFF_ID, loggedInStaff.getStaffId());
            session.setAttribute(SESSION_STAFF_EMAIL, loggedInStaff.getStaffEmail());
            return "redirect:/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred. Please try again.");
            return "loginStaff";
        }
    }

    @GetMapping("/logoutStaff")
    public String logoutStaff(HttpSession session) {
        // Remove specific attributes
        session.removeAttribute(SESSION_STAFF_ID);
        session.removeAttribute(SESSION_STAFF_EMAIL);

        // Invalidate the entire session
        session.invalidate();

        return "redirect:/";
    }
}