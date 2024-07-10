package com.heroku.java.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.heroku.java.model.feedback;

@Repository
public interface FeedbackRepository {
    void saveFeedback(feedback feedback);
    List<feedback> getAllFeedback();
    feedback getFeedbackById(int feedbackId);
}