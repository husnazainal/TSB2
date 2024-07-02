/*package com.heroku.java.model;

import java.sql.Date;
import java.sql.Time;

public class schedule {
    private int id;
    private Date date;
    private Time time;
    private String task;
    private String plantId;

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }
} */

/* 
package com.heroku.java.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Schedule {
    private LocalDate scheduleDate;
    private LocalTime scheduleTime;
    private String scheduleTask;
    private String plantId;

    // Getters and setters
    public LocalDate getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(LocalDate scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public LocalTime getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(LocalTime scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getScheduleTask() {
        return scheduleTask;
    }

    public void setScheduleTask(String scheduleTask) {
        this.scheduleTask = scheduleTask;
    }

    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }
}

*/

package com.heroku.java.controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.model.Schedule; // Corrected import and class name

@Controller
public class UpdateScheduleController { // Changed class name to start with uppercase
    private final DataSource dataSource;

    @Autowired
    public UpdateScheduleController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/updateSchedule")
    public String showUpdateScheduleForm(@RequestParam("scheduleid") int scheduleId, Model model) {
        Schedule schedule = new Schedule(); // Corrected class name

        try (Connection con = dataSource.getConnection()) {
            String query = "SELECT * FROM schedule WHERE scheduleid = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, scheduleId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Ensure Schedule class has these setter methods
                        schedule.setScheduleDate(rs.getDate("scheduledate").toLocalDate());
                        schedule.setScheduleTime(rs.getTime("scheduletime").toLocalTime());
                        schedule.setScheduleTask(rs.getString("scheduletask"));
                        schedule.setPlantId(rs.getString("plantid"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }

        model.addAttribute("schedule", schedule);
        return "updateSchedule";
    }

    @PostMapping("/updateSchedule")
    public String updateSchedule(@ModelAttribute("schedule") Schedule schedule) {
        try (Connection con = dataSource.getConnection()) {
            String query = "UPDATE schedule SET scheduledate = ?, scheduletask = ?, scheduletime = ?, plantid = ? WHERE scheduleid = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setDate(1, Date.valueOf(schedule.getScheduleDate()));
                ps.setString(2, schedule.getScheduleTask());
                ps.setTime(3, Time.valueOf(schedule.getScheduleTime()));
                ps.setString(4, schedule.getPlantId());
                ps.setInt(5, schedule.getId()); // Ensure you have a getId() method in Schedule class
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/viewSchedules";
    }
}
