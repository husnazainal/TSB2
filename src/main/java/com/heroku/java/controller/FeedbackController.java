package com.heroku.java.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.heroku.java.model.feedback;
import com.heroku.java.model.plant;

import jakarta.servlet.http.HttpSession;

@Controller
public class FeedbackController {

    private final DataSource dataSource;

    @Autowired
    public FeedbackController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/submitFeedback")
    public String showFeedbackForm(Model model) {
        model.addAttribute("feedback", new feedback());
        model.addAttribute("plants", getPlants()); // Add method to fetch plants
        return "submitFeedback";
    }

    @PostMapping("/submitFeedback")
    @Transactional
    public String submitFeedback(@Validated @ModelAttribute("feedback") feedback feedback, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO feedback(plantid, message, datecreated, visitorname, visitoremail, visitorphoneno) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, feedback.getPlantId());
                statement.setString(2, feedback.getMessage());
                statement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                statement.setString(4, feedback.getVisitorName());
                statement.setString(5, feedback.getVisitorEmail());
                statement.setString(6, feedback.getVisitorPhoneno());
                statement.executeUpdate();
            }
            redirectAttributes.addFlashAttribute("message", "Feedback submitted successfully");
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("error", "Error submitting feedback: " + e.getMessage());
        }
        return "redirect:/submitFeedback";
    }

    @GetMapping("/feedbackList")
    public String feedbackList(Model model, HttpSession session) {
        if (session.getAttribute(staffloginController.SESSION_STAFF_ID) == null) {
            return "redirect:/loginStaff";
        }

        List<feedback> feedbacks = new ArrayList<>();
        String sql = "SELECT f.*, p.comname FROM feedback f JOIN plant p ON f.plantid = p.plantid ORDER BY f.datecreated DESC";

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                feedback feedback = new feedback();
                feedback.setFeedbackId(resultSet.getInt("feedbackId"));
                feedback.setPlantId(resultSet.getInt("plantId"));
                feedback.setMessage(resultSet.getString("message"));
                feedback.setDateCreated(resultSet.getDate("dateCreated"));
                feedback.setVisitorName(resultSet.getString("visitorName"));
                feedback.setVisitorEmail(resultSet.getString("visitorEmail"));
                feedback.setVisitorPhoneno(resultSet.getString("visitorPhoneno"));

                plant plant = new plant();
                plant.setComName(resultSet.getString("comname"));
                feedback.setPlant(plant);

                feedbacks.add(feedback);
            }
        } catch (SQLException e) {
            model.addAttribute("error", "Database error: " + e.getMessage());
            return "error";
        }

        model.addAttribute("feedbacks", feedbacks);
        return "feedbackList";
    }

    @GetMapping("/ViewFeedback")
    public String viewFeedback(@RequestParam("feedbackId") int feedbackId, Model model, HttpSession session) {
        if (session.getAttribute(staffloginController.SESSION_STAFF_ID) == null) {
            return "redirect:/loginStaff";
        }
        String sql = "SELECT f.*, p.comname FROM feedback f JOIN plant p ON f.plantid = p.plantid WHERE f.feedbackid = ?";

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, feedbackId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    feedback feedback = new feedback();
                    feedback.setFeedbackId(resultSet.getInt("feedbackId"));
                    feedback.setPlantId(resultSet.getInt("plantId"));
                    feedback.setMessage(resultSet.getString("message"));
                    feedback.setDateCreated(resultSet.getDate("dateCreated"));
                    feedback.setVisitorName(resultSet.getString("visitorName"));
                    feedback.setVisitorEmail(resultSet.getString("visitorEmail"));
                    feedback.setVisitorPhoneno(resultSet.getString("visitorPhoneno"));

                    plant plant = new plant();
                    plant.setComName(resultSet.getString("comname"));
                    feedback.setPlant(plant);

                    model.addAttribute("feedback", feedback);
                } else {
                    model.addAttribute("error", "feedback not found");
                    return "error";
                }
            }
        } catch (SQLException e) {
            model.addAttribute("error", "Database error: " + e.getMessage());
            return "error";
        }
        return "ViewFeedback";
    }

    private List<plant> getPlants() {
        List<plant> plants = new ArrayList<>();
        String sql = "SELECT plantid, comname FROM plant ORDER BY comname";

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                plant plant = new plant();
                plant.setPlantId(resultSet.getInt("plantid"));
                plant.setComName(resultSet.getString("comname"));
                plants.add(plant);
            }
        } catch (SQLException e) {
            // Log the error
            System.err.println("Error fetching plants: " + e.getMessage());
        }

        return plants;
    }
}
