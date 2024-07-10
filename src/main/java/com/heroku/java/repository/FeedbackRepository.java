package com.heroku.java.repository;

import java.util.List;
import com.heroku.java.model.feedback;

public interface FeedbackRepository {
    void saveFeedback(feedback feedback);
    List<feedback> getAllFeedback();
    feedback getFeedbackById(int feedbackId);
}