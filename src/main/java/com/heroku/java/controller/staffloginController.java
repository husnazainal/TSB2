package com.heroku.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.heroku.java.model.StaffModel;

import jakarta.servlet.http.HttpSession;

import java.sql.SQLException;

import javax.sql.DataSource;

@Controller
public class staffloginController {

    public static final String SESSION_STAFF_ID = "staffid";
    public static final String SESSION_STAFF_EMAIL = "loggedInStaff";

    private final DataSource dataSource;

    @Autowired
    public staffloginController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/loginStaff")
    public String loginForm(Model model) {
        System.out.println("Login form requested");
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
            StaffDAO staffDAO = new StaffDAO(dataSource);
            StaffModel loggedInStaff = StaffDAO.getStaffByStaffemail(dataSource, staffModel.getStaffEmail());
            if (loggedInStaff == null || !loggedInStaff.getStaffPassword().equals(staffModel.getStaffPassword())) {
                model.addAttribute("error", "Invalid email or password. Please try again.");
                return "loginStaff";
            }

            session.setAttribute(SESSION_STAFF_ID, loggedInStaff.getStaffId());
            session.setAttribute(SESSION_STAFF_EMAIL, loggedInStaff.getStaffEmail());

            System.out.println("Staff ID set in session during login: " + loggedInStaff.getStaffId());
            System.out.println("Staff ID retrieved immediately after setting: " + session.getAttribute(SESSION_STAFF_ID));

            return "redirect:/dashboard";
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred. Please try again.");
            return "loginStaff";
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred. Please try again.");
            return "loginStaff";
        }
    }
}
