// package com.heroku.java.controller;

// import java.sql.Connection;
// import javax.sql.DataSource;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PostMapping;

// import com.heroku.java.model.schedule;

// @Controller
// public class AddScheduleController {
//     private final DataSource dataSource;

//     @Autowired
//     public AddScheduleController(DataSource dataSource) {
//         this.dataSource = dataSource;
//     }

//     @PostMapping("/addschedule")
//     public String addSchedule(@ModelAttribute("schedule") schedule schedule) {
//         try (Connection connection = dataSource.getConnection()) {
//             String sql = "INSERT INTO public.schedule(date, time, task, plant_id) VALUES (?, ?, ?, ?)";
//             final var statement = connection.prepareStatement(sql);

//             statement.setDate(1, java.sql.Date.valueOf(schedule.getScheduleDate()));
//             statement.setTime(2, java.sql.Time.valueOf(schedule.getScheduleTime()));
//             statement.setString(3, schedule.getScheduleTask());
//             statement.setString(4, schedule.getPlantId());

//             statement.executeUpdate();
//         } catch (Exception e) {
//             e.printStackTrace();
//             return "error";
//         }
//         return "redirect:/homepage";
//     }

//     @GetMapping("/schedulelist")
//     public String scheduleList(Model model) {
//         // Implementation for listing schedules
//         return "schedulelist";
//     }
// }
