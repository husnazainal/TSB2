package com.heroku.java.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.heroku.java.model.StaffModel;

public class StaffDAO {
    private final DataSource dataSource;

    public StaffDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static StaffModel getStaffByStaffemail(DataSource dataSource, String staffemail) throws SQLException {
        String query = "SELECT * FROM staff WHERE staffemail = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, staffemail);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    StaffModel staffModel = new StaffModel();
                    staffModel.setStaffId(rs.getInt("staffid"));
                    staffModel.setStaffName(rs.getString("staffname"));
                    staffModel.setStaffEmail(rs.getString("staffemail"));
                    staffModel.setStaffPassword(rs.getString("staffpassword"));
                    return staffModel;
                }
            }
        }
        return null;
    }
}