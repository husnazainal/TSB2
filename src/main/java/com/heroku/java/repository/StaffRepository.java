package com.heroku.java.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.heroku.java.model.StaffModel;

@Repository
public class StaffRepository {
    private static final Logger logger = LoggerFactory.getLogger(StaffRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public StaffRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveStaff(StaffModel staffModel) {
        String sql = "INSERT INTO staff (staffName, staffEmail, staffPassword) VALUES (?, ?, ?)";
        try {
            logger.debug("Attempting to save staff: {}", staffModel);
            jdbcTemplate.update(sql,
                staffModel.getStaffName(),
                staffModel.getStaffEmail(),
                staffModel.getStaffPassword());
            logger.debug("Staff saved successfully");
        } catch (DataAccessException e) {
            logger.error("Error saving staff to database", e);
            throw new RuntimeException("Failed to save staff: " + e.getMessage(), e);
        }
    }
}