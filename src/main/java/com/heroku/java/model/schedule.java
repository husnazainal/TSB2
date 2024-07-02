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


package com.heroku.java.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Schedule {
    private int id;
    private LocalDate scheduleDate;
    private LocalTime scheduleTime;
    private String scheduleTask;
    private String plantId;

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
