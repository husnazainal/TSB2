package com.heroku.java.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.model.feedback;

import jakarta.servlet.http.HttpSession;

@Controller
public class FeedbackController {

    private final DataSource dataSource;

    @Autowired
    public FeedbackController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/feedbackForm")
    public String showFeedbackForm(Model model) {
        model.addAttribute("feedback", new feedback());
        return "feedbackForm";
    }

    @PostMapping("/feedbackForm")
    @Transactional
    public String feedbackForm(@ModelAttribute("feedback") feedback feedback) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO feedback(plantId, message, dateCreated, visitorName, visitorId) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, feedback.getPlantId());
                statement.setString(2, feedback.getMessage());
                statement.setDate(3, new Date(System.currentTimeMillis())); // Set current date
                statement.setString(4, feedback.getVisitorName());
                statement.setInt(5, feedback.getVisitorId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            // Log the error
            return "redirect:/error";
        }

        return "redirect:/index";
    }

    @GetMapping("/feedbackList")
    public String feedbackList(Model model, HttpSession session) {
        // Check if staff is logged in
        if (session.getAttribute("staffId") == null) {
            return "redirect:/loginStaff";
        }

        List<feedback> feedbacks = new ArrayList<>();
        String sql = "SELECT f.*, p.comname FROM feedback f JOIN plant p ON f.plantId = p.plantid ORDER BY f.dateCreated DESC";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                feedback feedback = new feedback();
                feedback.setFeedbackId(resultSet.getInt("feedbackId"));
                feedback.setPlantId(resultSet.getInt("plantId"));
                feedback.setMessage(resultSet.getString("message"));
                feedback.setDateCreated(resultSet.getDate("dateCreated"));
                feedback.setVisitorName(resultSet.getString("visitorName"));
                feedback.setVisitorId(resultSet.getInt("visitorId"));
                // Add plant name to the model if needed
                feedbacks.add(feedback);
            }
        } catch (SQLException e) {
            // Log the error
            model.addAttribute("error", "Database error: " + e.getMessage());
            return "error";
        }

        model.addAttribute("feedbacks", feedbacks);
        return "feedbackList";
    }

    @GetMapping("/ViewFeedback")
    public String ViewFeedback(@RequestParam("feedbackId") int feedbackId, Model model, HttpSession session) {
        // Check if staff is logged in
        if (session.getAttribute("staffId") == null) {
            return "redirect:/loginStaff";
        }

        String sql = "SELECT f.*, p.comname FROM feedback f JOIN plant p ON f.plantId = p.plantid WHERE f.feedbackId = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, feedbackId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    feedback feedback = new feedback();
                    feedback.setFeedbackId(resultSet.getInt("feedbackId"));
                    feedback.setPlantId(resultSet.getInt("plantId"));
                    feedback.setMessage(resultSet.getString("message"));
                    feedback.setDateCreated(resultSet.getDate("dateCreated"));
                    feedback.setVisitorName(resultSet.getString("visitorName"));
                    feedback.setVisitorId(resultSet.getInt("visitorId"));
                    // Add plant name to the model if needed
                    model.addAttribute("feedback", feedback);
                    model.addAttribute("plantName", resultSet.getString("comname"));
                } else {
                    model.addAttribute("error", "Feedback not found");
                    return "error";
                }
            }
        } catch (SQLException e) {
            // Log the error
            model.addAttribute("error", "Database error: " + e.getMessage());
            return "error";
        }
        return "ViewFeedback";
    }
}