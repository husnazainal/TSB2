package com.heroku.java.controller;

import com.heroku.java.model.Staff;
import com.heroku.java.repository.AccountRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {

    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping("/signup")
    public String showSignupForm() {
        return "signup";
    }

    @PostMapping("/loginStaff")
    public String registerStaff(@RequestParam String username,
                                @RequestParam String email,
                                @RequestParam String password,
                                Model model) {
        // Check if email already exists
        if (accountRepository.findByStaffEmail(email) != null) {
            model.addAttribute("error", "Email already exists");
            return "signup";
        }

        // Create new staff
        Staff newStaff = new Staff(username, email, password);
        accountRepository.save(newStaff);

        return "redirect:/loginStaff";
    }

    @GetMapping("/loginStaff")
    public String showLoginForm() {
        return "loginStaff";
    }
}