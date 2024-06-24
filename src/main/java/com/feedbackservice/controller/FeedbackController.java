package com.feedbackservice.controller;


import com.feedbackservice.model.Feedback;
import com.feedbackservice.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FeedbackController {

    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackController(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @GetMapping("/feedback")
    public String showFeedbackForm(Model model) {
        model.addAttribute("feedback", new Feedback());
        return "feedbackForm"; // Assuming you have a corresponding HTML form template
    }

    @PostMapping("/feedback")
    public String submitFeedback(Feedback feedback) {
        feedbackRepository.save(feedback);
        return "redirect:/thankyou"; // Redirect to a thank you page or homepage
    }

    // Additional methods as needed (e.g., listing feedback)
}
