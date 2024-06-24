package com.feedbackservice.repository;


import com.feedbackservice.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    void save(com.feedbackservice.model.Feedback feedback);
    // Additional custom query methods if needed
}
