package com.heroku.java.controller;

import com.heroku.java.model.StaffModel;
import com.heroku.java.repository.StaffRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffRestController {

    private static final Logger logger = LoggerFactory.getLogger(StaffRestController.class);
    private final StaffRepository staffRepository;

    @Autowired
    public StaffRestController(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @GetMapping("/list")
    public ResponseEntity<?> listStaff() {
        try {
            List<StaffModel> staffList = staffRepository.getAllStaff();
            return ResponseEntity.ok(staffList);
        } catch (Exception e) {
            logger.error("Error retrieving staff list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving the staff list");
        }
    }

    @GetMapping("/view/{staffId}")
    public ResponseEntity<?> viewStaff(@PathVariable Long staffId) {
        try {
            StaffModel staff = staffRepository.getStaffById(staffId);
            if (staff == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Staff not found");
            }
            return ResponseEntity.ok(staff);
        } catch (Exception e) {
            logger.error("Error viewing staff", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving staff information");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerStaff(@RequestBody StaffModel staffModel) {
        logger.debug("Registering staff: {}", staffModel);
        try {
            StaffModel savedStaff = staffRepository.saveStaff(staffModel);
            logger.debug("Staff saved successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStaff);
        } catch (Exception e) {
            logger.error("Error registering staff", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed. Error: " + e.getMessage());
        }
    }

    @PutMapping("/update/{staffId}")
    public ResponseEntity<?> updateStaff(@PathVariable Long staffId, @RequestBody StaffModel updatedStaff) {
        try {
            StaffModel existingStaff = staffRepository.getStaffById(staffId);
            if (existingStaff == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Staff not found");
            }

            existingStaff.setStaffName(updatedStaff.getStaffName());
            existingStaff.setStaffEmail(updatedStaff.getStaffEmail());
            if (updatedStaff.getStaffPassword() != null && !updatedStaff.getStaffPassword().isEmpty()) {
                existingStaff.setStaffPassword(updatedStaff.getStaffPassword());
            }

            staffRepository.updateStaff(existingStaff);
            return ResponseEntity.ok("Staff updated successfully");
        } catch (Exception e) {
            logger.error("Error updating staff", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Update failed. Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{staffId}")
    public ResponseEntity<?> deleteStaff(@PathVariable Long staffId) {
        try {
            StaffModel staff = staffRepository.getStaffById(staffId);
            if (staff == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Staff not found");
            }

            staffRepository.deleteStaff(staffId);
            return ResponseEntity.ok("Staff deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting staff", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Delete failed. Error: " + e.getMessage());
        }
    }
}