package com.heroku.java.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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

    public StaffModel saveStaff(StaffModel staffModel) {
        String sql = "INSERT INTO staff (staffname, staffemail, staffpassword) VALUES (?, ?, ?)";
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
        return staffModel;
    }

    public void updateStaff(StaffModel staffModel) {
        String sql = "UPDATE staff SET staffname = ?, staffemail = ? WHERE staffid = ?";
        try {
            logger.debug("Attempting to update staff: {}", staffModel);
            int rowsAffected = jdbcTemplate.update(sql,
                    staffModel.getStaffName(),
                    staffModel.getStaffEmail(),
                    staffModel.getStaffId());
            if (rowsAffected > 0) {
                logger.debug("Staff updated successfully");
            } else {
                logger.warn("No staff found with ID: {}", staffModel.getStaffId());
                throw new RuntimeException("No staff found with ID: " + staffModel.getStaffId());
            }
        } catch (DataAccessException e) {
            logger.error("Error updating staff in database", e);
            throw new RuntimeException("Failed to update staff: " + e.getMessage(), e);
        }
    }

    public List<StaffModel> getAllStaff() {
        String sql = "SELECT * FROM staff";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(StaffModel.class));
    }

    public StaffModel getStaffById(Long id) {
        String sql = "SELECT * FROM staff WHERE staffid = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(StaffModel.class), id);
        } catch (EmptyResultDataAccessException e) {
            logger.warn("No staff found with ID: {}", id);
            return null;
        }
    }

    public void deleteStaff(int staffId) {
        String sql = "DELETE FROM staff WHERE staffid = ?";
        jdbcTemplate.update(sql, staffId);
    }

    public StaffModel authenticateStaff(String email, String password) {
        String sql = "SELECT * FROM staff WHERE staffemail = ? AND staffpassword = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(StaffModel.class), email, password);
        } catch (EmptyResultDataAccessException e) {
            logger.warn("Authentication failed for email: {}", email);
            return null;
        }
    }
}
