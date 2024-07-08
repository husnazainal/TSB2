package com.heroku.java.repository;

import com.heroku.java.model.StaffModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StaffRepository {

    private final JdbcTemplate jdbcTemplate;

    public StaffRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveStaff(StaffModel staffModel) {
        String sql = "INSERT INTO staff (staffname, staffemail, staffpassword) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                            staffModel.getStaffName(),
                            staffModel.getStaffEmail(),
                            staffModel.getStaffPassword());
    }
}
