package com.heroku.java.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "staff")
public class StaffModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staffid")
    private Long staffId;
    
    @Column(name = "staffname")
    private String staffName;

    @Column(name = "staffemail")
    private String staffEmail;

    @Column(name = "staffpassword")
    private String staffPassword;

    // Default constructor
    public StaffModel() {
    }

    // Parameterized constructor
    public StaffModel(Long staffId, String staffName, String staffEmail, String staffPassword) {
        this.staffId = staffId;
        this.staffName = staffName;
        this.staffEmail = staffEmail;
        this.staffPassword = staffPassword;
    }

    // Getters and setters
    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffEmail() {
        return staffEmail;
    }

    public void setStaffEmail(String staffEmail) {
        this.staffEmail = staffEmail;
    }

    public String getStaffPassword() {
        return staffPassword;
    }

    public void setStaffPassword(String staffPassword) {
        this.staffPassword = staffPassword;
    }

    @Override
    public String toString() {
        return "StaffModel{" +
                "staffId=" + staffId +
                ", staffName='" + staffName + '\'' +
                ", staffEmail='" + staffEmail + '\'' +
                ", staffPassword='" + staffPassword + '\'' +
                '}';
    }
}
