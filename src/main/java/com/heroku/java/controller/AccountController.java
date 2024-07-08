package com.heroku.java.controller;

import com.heroku.java.model.StaffModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class AccountController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AccountController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/signup")
    public ModelAndView signup() {
        ModelAndView modelAndView = new ModelAndView("signup");
        modelAndView.addObject("staffModel", new StaffModel());
        return modelAndView;
    }

    @PostMapping("/signup")
    public String processSignupForm(@ModelAttribute("staffModel") StaffModel staffModel, HttpSession session) {
        saveStaffToDatabase(staffModel);
        
        // Set loggedInUser session attribute after signup
        session.setAttribute("loggedInUser", staffModel);

        return "redirect:/staff/dashboard";
    }

    private void saveStaffToDatabase(StaffModel staffModel) {
        String sql = "INSERT INTO staff (staffname, staffemail, staffpassword) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                            staffModel.getStaffName(),
                            staffModel.getStaffEmail(),
                            staffModel.getStaffPassword());
    }
}