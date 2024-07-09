package com.heroku.java.controller;

import javax.sql.DataSource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.heroku.java.model.Staff;

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
        System.out.println("Login form requested");
        if (!model.containsAttribute("staff")) {
            model.addAttribute("staff", new Staff());
        }
        return "loginStaff";
    }

    @PostMapping("/loginStaff")
    public String login(HttpSession session, @ModelAttribute Staff staff, Model model) {
        try {
            Staff loggedInStaff = StaffDAO.getStaffByStaffemail(dataSource, staff.getStaffEmail());
            model.addAttribute("staff", new Staff());

            if (loggedInStaff == null || !loggedInStaff.getStaffPassword().equals(staff.getStaffPassword())) {
                model.addAttribute("error", "Invalid email or password. Please try again.");
                return "loginStaff";
            }

            session.setAttribute(SESSION_STAFF_ID, loggedInStaff.getId());
            session.setAttribute(SESSION_STAFF_EMAIL, loggedInStaff.getStaffEmail());

            System.out.println("Staff ID set in session during login: " + loggedInStaff.getId());
            System.out
                    .println("Staff ID retrieved immediately after setting: " + session.getAttribute(SESSION_STAFF_ID));

            return "redirect:/admindashboard";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred. Please try again.");
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