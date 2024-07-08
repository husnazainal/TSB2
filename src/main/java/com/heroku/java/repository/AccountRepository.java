package com.heroku.java.repository;

import com.heroku.java.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Staff, Long> {
    @Query("SELECT s FROM Staff s WHERE s.staffEmail = :email")
    Staff findByStaffEmail(@Param("email") String email);
}