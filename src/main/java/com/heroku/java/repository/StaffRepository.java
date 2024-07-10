package com.heroku.java.repository;

import java.sql.PreparedStatement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
        String sql = "INSERT INTO staff (staffName, staffEmail, staffPassword) VALUES (?, ?, ?)";
        String[] returnId = {"staffId"};
        try {
            logger.debug("Attempting to save staff: {}", staffModel);
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, returnId);
                ps.setString(1, staffModel.getStaffName());
                ps.setString(2, staffModel.getStaffEmail());
                ps.setString(3, staffModel.getStaffPassword());
                return ps;
            }, keyHolder);

            @SuppressWarnings("null")
            int staffId = keyHolder.getKey().intValue();
            staffModel.setStaffId(staffId);
            logger.debug("Staff saved successfully with ID: {}", staffId);
            return staffModel;
        } catch (DataAccessException e) {
            logger.error("Error saving staff to database", e);
            throw new RuntimeException("Failed to save staff: " + e.getMessage(), e);
        }
    }

    public void updateStaff(StaffModel staffModel) {
        String sql = "UPDATE staff SET staffName = ?, staffEmail = ? WHERE staffId = ?";
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

    public StaffModel getStaffById(int staffId) {
        String sql = "SELECT * FROM staff WHERE staffId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(StaffModel.class), staffId);
        } catch (EmptyResultDataAccessException e) {
            logger.warn("No staff found with ID: {}", staffId);
            return null;
        }
    }

    public void deleteStaff(int staffId) {
        String sql = "DELETE FROM staff WHERE staffId = ?";
        jdbcTemplate.update(sql, staffId);
    }

    public StaffModel authenticateStaff(String email, String password) {
        String sql = "SELECT * FROM staff WHERE staffEmail = ? AND staffPassword = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(StaffModel.class), email, password);
        } catch (EmptyResultDataAccessException e) {
            logger.warn("Authentication failed for email: {}", email);
            return null;
        }
    }
}
