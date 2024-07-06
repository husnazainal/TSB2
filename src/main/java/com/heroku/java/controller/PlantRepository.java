package com.heroku.java.controller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.heroku.java.model.Plant;


@Repository
public interface PlantRepository extends JpaRepository<Plant, Integer> {
}

