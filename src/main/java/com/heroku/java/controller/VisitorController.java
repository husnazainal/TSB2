package com.heroku.java.controller;

import com.heroku.java.model.plant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class VisitorController {
    private final DataSource dataSource;

    @Autowired
    public VisitorController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/plantList")
    public String plantList(Model model) {
        List<plant> plants = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT plantid, scientific_name, common_name, type, habitat, species, description FROM plants ORDER BY plantid";
            var statement = connection.prepareStatement(sql);
            var resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int plantId = resultSet.getInt("plantid");
                String plantSciname = resultSet.getString("scientific_name");
                String plantComname = resultSet.getString("common_name");
                String plantType = resultSet.getString("type");
                String plantHabitat = resultSet.getString("habitat");
                String plantSpecies = resultSet.getString("species");
                String plantDesc = resultSet.getString("description");

                plant plant = new plant();
                plant.setPlantId(plantId);
                plant.setPlantSciname(plantSciname);
                plant.setPlantComname(plantComname);
                plant.setPlantType(plantType);
                plant.setPlantHabitat(plantHabitat);
                plant.setPlantSpecies(plantSpecies);
                plant.setPlantDesc(plantDesc);

                plants.add(plant);
            }

            model.addAttribute("plants", plants);

        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }

        return "PlantList";
    }

    @GetMapping("/plantDetail")
    public String plantDetail(@RequestParam("plantId") int plantId, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT plantid, scientific_name, common_name, type, habitat, species, description FROM plants WHERE plantid = ?";
            var statement = connection.prepareStatement(sql);
            statement.setInt(1, plantId);
            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String plantSciname = resultSet.getString("scientific_name");
                String plantComname = resultSet.getString("common_name");
                String plantType = resultSet.getString("type");
                String plantHabitat = resultSet.getString("habitat");
                String plantSpecies = resultSet.getString("species");
                String plantDesc = resultSet.getString("description");

                plant plant = new plant();
                plant.setPlantId(plantId);
                plant.setPlantSciname(plantSciname);
                plant.setPlantComname(plantComname);
                plant.setPlantType(plantType);
                plant.setPlantHabitat(plantHabitat);
                plant.setPlantSpecies(plantSpecies);
                plant.setPlantDesc(plantDesc);

                model.addAttribute("plant", plant);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }

        return "PlantDetail";
    }
}
