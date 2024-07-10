package com.heroku.java.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String staffName;
    private String staffEmail;
    private String staffPassword;

    // Constructors, getters, and setters
    public Staff() {
    }

    public Staff(String staffName, String staffEmail, String staffPassword) {
        this.staffName = staffName;
        this.staffEmail = staffEmail;
        this.staffPassword = staffPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return "Staff{" +
                "id=" + id +
                ", staffName='" + staffName + '\'' +
                ", staffEmail='" + staffEmail + '\'' +
                ", staffPassword='" + staffPassword + '\'' +
                '}';
    }
}
