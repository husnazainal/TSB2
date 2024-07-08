package com.heroku.java.repository;

import com.heroku.java.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Staff, Long> {
    Staff findByStaffEmail(String email);
}