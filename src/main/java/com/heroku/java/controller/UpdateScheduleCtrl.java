/* 
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

import com.heroku.java.model.Schedule;

@Controller
public class UpdateScheduleController {
    private final DataSource dataSource;

    @Autowired
    public UpdateScheduleController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/updateSchedule")
    public String showUpdateScheduleForm(@RequestParam("scheduleid") int scheduleId, Model model) {
        Schedule schedule = new Schedule();

        try (Connection con = dataSource.getConnection()) {
            String query = "SELECT * FROM schedule WHERE scheduleid = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, scheduleId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        schedule.setId(rs.getInt("scheduleid"));
                        schedule.setDate(rs.getDate("scheduleDate"));
                        schedule.setTime(rs.getTime("scheduleTime"));
                        schedule.setTask(rs.getString("scheduleTask"));
                        schedule.setPlantId(rs.getString("plantId"));
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
    public String updateSchedule(@ModelAttribute("schedule") schedule schedule) {
        try (Connection con = dataSource.getConnection()) {
            String query = "UPDATE schedule SET scheduleDate = ?, scheduleTask = ?, scheduleTime = ?, plantId = ? WHERE scheduleid = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setDate(1, schedule.getDate());
                ps.setString(2, schedule.getTask());
                ps.setTime(3, schedule.getTime());
                ps.setString(4, schedule.getPlantId());
                ps.setInt(5, schedule.getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/viewSchedules";
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

import com.heroku.java.model.schedule;

@Controller
public class UpdateScheduleCtrl {
    private final DataSource dataSource;

    @Autowired
    public UpdateScheduleCtrl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/updateSchedule")
    public String showUpdateScheduleForm(@RequestParam("scheduleid") int scheduleId, Model model) {
        schedule schedule = new schedule();

        try (Connection con = dataSource.getConnection()) {
            String query = "SELECT * FROM schedule WHERE scheduleid = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, scheduleId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        schedule.setId(rs.getInt("scheduleid"));
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
    public String updateSchedule(@ModelAttribute("schedule") schedule schedule) {
        try (Connection con = dataSource.getConnection()) {
            String query = "UPDATE schedule SET scheduledate = ?, scheduletask = ?, scheduletime = ?, plantid = ? WHERE scheduleid = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setDate(1, Date.valueOf(schedule.getScheduleDate()));
                ps.setString(2, schedule.getScheduleTask());
                ps.setTime(3, Time.valueOf(schedule.getScheduleTime()));
                ps.setString(4, schedule.getPlantId());
                ps.setInt(5, schedule.getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/viewSchedules";
    }
}
