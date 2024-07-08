package com.heroku.java.controller;

import javax.sql.DataSource;

import com.heroku.java.model.Staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffDAO {

    private final DataSource dataSource;

    public StaffDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Staff getStaffByStaffemail(String staffemail) throws SQLException {
        String query = "SELECT * FROM staff WHERE staffemail = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, staffemail);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Staff staff = new Staff();
                    staff.setId(rs.getLong("staffid"));
                    staff.setStaffName(rs.getString("staffname"));
                    staff.setStaffEmail(rs.getString("staffemail"));
                    staff.setStaffPassword(rs.getString("staffpassword"));
                    return staff;
                }
            }
        }
        return null;
    }
}
