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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.heroku.java.model.feedback;
import com.heroku.java.repository.FeedbackRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class FeedbackController {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);
    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackController(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @GetMapping("/FeedbackForm")
    public String showFeedbackForm(Model model) {
        model.addAttribute("feedback", new feedback());
        return "FeedbackForm";
    }

    @PostMapping("/FeedbackForm")
    public String submitFeedback(@ModelAttribute("feedback") feedback feedback, RedirectAttributes redirectAttributes) {
        try {
            // Create a new java.sql.Date object with the current time
            java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());

            feedback.setDateCreated(sqlDate);
            feedbackRepository.saveFeedback(feedback);
            redirectAttributes.addFlashAttribute("message", "Feedback submitted successfully");
            return "redirect:/index";
        } catch (Exception e) {
            logger.error("Error submitting feedback", e);
            redirectAttributes.addFlashAttribute("error", "Error submitting feedback: " + e.getMessage());
            return "redirect:/FeedbackForm";
        }
    }

    @GetMapping("/feedbackList")
    public String listFeedback(Model model, HttpSession session) {
        if (session.getAttribute(staffloginController.SESSION_STAFF_ID) == null) {
            return "redirect:/loginStaff";
        }

        try {
            List<feedback> feedbackList = feedbackRepository.getAllFeedback();
            model.addAttribute("feedbackList", feedbackList);
            return "feedbackList";
        } catch (Exception e) {
            logger.error("Error retrieving feedback list", e);
            model.addAttribute("error", "An error occurred while retrieving the feedback list");
            return "error";
        }
    }

    @GetMapping("/ViewFeedback")
    public String ViewFeedback(@RequestParam("feedbackId") int feedbackId, Model model, HttpSession session) {
        if (session.getAttribute(staffloginController.SESSION_STAFF_ID) == null) {
            return "redirect:/loginStaff";
        }

        try {
            feedback feedback = feedbackRepository.getFeedbackById(feedbackId);
            if (feedback == null) {
                model.addAttribute("error", "Feedback not found");
                return "error";
            }
            model.addAttribute("feedback", feedback);
            return "ViewFeedback";
        } catch (Exception e) {
            logger.error("Error viewing feedback", e);
            model.addAttribute("error", "An error occurred while retrieving feedback information");
            return "error";
        }
    }
}
