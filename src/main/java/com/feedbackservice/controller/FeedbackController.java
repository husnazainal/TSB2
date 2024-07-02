package com.feedbackservice.controller;


import com.feedbackservice.model.Feedback;
import com.feedbackservice.repository.FeedbackRepository;
import com.feedbackservice.service.FeedbackService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/* 
@RestController
@RequestMapping("/Feedback")
//public class ProductController {
@Controller
public class FeedbackController {

    //private final FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @GetMapping("/feedback")
    public String showFeedbackForm(Model model) {
        model.addAttribute("feedback", new Feedback(null, null));
        return "feedbackForm"; // Assuming you have a corresponding HTML form template
    }

    @PostMapping("/feedback")
    public String submitFeedback(Feedback feedback) {
        feedbackRepository.save(feedback);
        return "redirect:/thankyou"; // Redirect to a thank you page or homepage
    }

    // Additional methods as needed (e.g., listing feedback)
}
*/

/*package com.feedbackservice.controller;

import com.feedbackservice.model.Feedback;
import com.feedbackservice.repository.FeedbackRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List; */

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackController(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @PostMapping("/add")
    public Feedback addFeedback(@RequestBody Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @GetMapping("/all")
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    @GetMapping("/{id}")
    public Feedback getFeedbackById(@PathVariable Long id) {
        return feedbackRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteFeedback(@PathVariable Long id) {
        feedbackRepository.deleteById(id);
    }
}
