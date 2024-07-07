package com.heroku.java.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.model.IndoorPlant;
import com.heroku.java.model.OutdoorPlant;
import com.heroku.java.model.plant;

@Controller
public class PlantController {

    private final DataSource dataSource;

    @Autowired
    public PlantController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/addplant")
    public String showAddPlantForm(Model model) {
        model.addAttribute("plant", new plant());
        model.addAttribute("indoorPlant", new IndoorPlant());
        model.addAttribute("outdoorPlant", new OutdoorPlant());
        return "addplant";
    }


    @PostMapping("/addplant")
    @Transactional
    public String addPlant(@ModelAttribute("plant") plant plant) {
        try (Connection connection = dataSource.getConnection()) {
            int newPlantId = generatePlantID(connection);
            plant.setPlantId(newPlantId);

            // Insert into main plant table
            String sql = "INSERT INTO plant(plantid, sciname, comname, type, habitat, species, description) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, plant.getPlantId());
                statement.setString(2, plant.getSciName());
                statement.setString(3, plant.getComName());
                statement.setString(4, plant.getType());
                statement.setString(5, plant.getHabitat());
                statement.setString(6, plant.getSpecies());
                statement.setString(7, plant.getDescription());
                statement.executeUpdate();
            }

            // Insert into subclass table based on plant type
            if ("Indoor".equals(plant.getType())) {
                sql = "INSERT INTO indoor_plant(plantid, lightr, humidp, waterf) VALUES (?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, plant.getPlantId());
                    statement.setString(2, ((IndoorPlant) plant).getLightR());
                    statement.setString(3, ((IndoorPlant) plant).getHumidP());
                    statement.setString(4, ((IndoorPlant) plant).getWaterF());
                    statement.executeUpdate();
                }
            } else if ("Outdoor".equals(plant.getType())) {
                sql = "INSERT INTO outdoor_plant(plantid, sune, windr, soilt) VALUES (?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, plant.getPlantId());
                    statement.setString(2, ((OutdoorPlant) plant).getSunE());
                    statement.setString(3, ((OutdoorPlant) plant).getWindR());
                    statement.setString(4, ((OutdoorPlant) plant).getSoilT());
                    statement.executeUpdate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }

        return "redirect:/plantList";
    }

    private int generatePlantID(Connection connection) throws Exception {
        String query = "SELECT COALESCE(MAX(plantid), 0) + 1 FROM plant";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return 1; // Start with 1 if no records found
            }
        }
    }

    @GetMapping("/plantList")
    public String plantList(Model model) {
        List<plant> plants = new ArrayList<>();

        String sql = "SELECT p.plantid, p.sciname, p.comname, p.type, p.habitat, p.species, p.description, " +
                     "i.lightr, i.humidp, i.waterf, " +
                     "o.sune, o.windr, o.soilt " +
                     "FROM plant p " +
                     "LEFT JOIN indoor_plant i ON p.plantid = i.plantid " +
                     "LEFT JOIN outdoor_plant o ON p.plantid = o.plantid " +
                     "ORDER BY p.plantid";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                plant plant;
                String type = resultSet.getString("type");

                if ("Indoor".equals(type)) {
                    plant = new IndoorPlant();
                    ((IndoorPlant) plant).setLightR(resultSet.getString("lightr"));
                    ((IndoorPlant) plant).setHumidP(resultSet.getString("humidp"));
                    ((IndoorPlant) plant).setWaterF(resultSet.getString("waterf"));
                } else if ("Outdoor".equals(type)) {
                    plant = new OutdoorPlant();
                    ((OutdoorPlant) plant).setSunE(resultSet.getString("sune"));
                    ((OutdoorPlant) plant).setWindR(resultSet.getString("windr"));
                    ((OutdoorPlant) plant).setSoilT(resultSet.getString("soilt"));
                } else {
                    plant = new plant();
                }

                plant.setPlantId(resultSet.getInt("plantid"));
                plant.setSciName(resultSet.getString("sciname"));
                plant.setComName(resultSet.getString("comname"));
                plant.setType(type);
                plant.setHabitat(resultSet.getString("habitat"));
                plant.setSpecies(resultSet.getString("species"));
                plant.setDescription(resultSet.getString("description"));

                plants.add(plant);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }

        model.addAttribute("plants", plants);
        return "plantList";
    }

    @GetMapping("/updatePlant")
    public String showUpdatePlantForm(@RequestParam("plantId") int plantId, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT p.plantid, p.sciname, p.comname, p.type, p.habitat, p.species, p.description, " +
                         "i.lightr, i.humidp, i.waterf, " +
                         "o.sune, o.windr, o.soilt " +
                         "FROM plant p " +
                         "LEFT JOIN indoor_plant i ON p.plantid = i.plantid " +
                         "LEFT JOIN outdoor_plant o ON p.plantid = o.plantid " +
                         "WHERE p.plantid = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, plantId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        plant plant;
                        String type = resultSet.getString("type");

                        if ("Indoor".equals(type)) {
                            IndoorPlant indoorPlant = new IndoorPlant();
                            indoorPlant.setLightR(resultSet.getString("lightr"));
                            indoorPlant.setHumidP(resultSet.getString("humidp"));
                            indoorPlant.setWaterF(resultSet.getString("waterf"));
                            plant = indoorPlant;
                        } else if ("Outdoor".equals(type)) {
                            OutdoorPlant outdoorPlant = new OutdoorPlant();
                            outdoorPlant.setSunE(resultSet.getString("sune"));
                            outdoorPlant.setWindR(resultSet.getString("windr"));
                            outdoorPlant.setSoilT(resultSet.getString("soilt"));
                            plant = outdoorPlant;
                        } else {
                            plant = new plant();
                        }

                        plant.setPlantId(plantId);
                        plant.setSciName(resultSet.getString("sciname"));
                        plant.setComName(resultSet.getString("comname"));
                        plant.setType(type);
                        plant.setHabitat(resultSet.getString("habitat"));
                        plant.setSpecies(resultSet.getString("species"));
                        plant.setDescription(resultSet.getString("description"));

                        model.addAttribute("plant", plant);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
        return "updatePlant";
    }

    @PostMapping("/updatePlant")
    public String updatePlant(@ModelAttribute("plant") plant plant) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try {
                // Update main plant table
                String sql = "UPDATE plant SET sciname = ?, comname = ?, type = ?, habitat = ?, species = ?, description = ? WHERE plantid = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, plant.getSciName());
                    statement.setString(2, plant.getComName());
                    statement.setString(3, plant.getType());
                    statement.setString(4, plant.getHabitat());
                    statement.setString(5, plant.getSpecies());
                    statement.setString(6, plant.getDescription());
                    statement.setInt(7, plant.getPlantId());
                    statement.executeUpdate();
                }

                // Update subclass table based on plant type
                if (plant instanceof IndoorPlant) {
                    IndoorPlant indoorPlant = (IndoorPlant) plant;
                    sql = "UPDATE indoor_plant SET lightr = ?, humidp = ?, waterf = ? WHERE plantid = ?";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setString(1, indoorPlant.getLightR());
                        statement.setString(2, indoorPlant.getHumidP());
                        statement.setString(3, indoorPlant.getWaterF());
                        statement.setInt(4, plant.getPlantId());
                        statement.executeUpdate();
                    }
                } else if (plant instanceof OutdoorPlant) {
                    OutdoorPlant outdoorPlant = (OutdoorPlant) plant;
                    sql = "UPDATE outdoor_plant SET sune = ?, windr = ?, soilt = ? WHERE plantid = ?";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setString(1, outdoorPlant.getSunE());
                        statement.setString(2, outdoorPlant.getWindR());
                        statement.setString(3, outdoorPlant.getSoilT());
                        statement.setInt(4, plant.getPlantId());
                        statement.executeUpdate();
                    }
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "redirect:/error";
        }
        return "redirect:/plantList";
    }

    @PostMapping("/deletePlant")
    public String deletePlant(@RequestParam("plantId") int plantId) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try {
                // Delete from indoor_plant table
                String sql = "DELETE FROM indoor_plant WHERE plantid = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, plantId);
                    statement.executeUpdate();
                }

                // Delete from outdoor_plant table
                sql = "DELETE FROM outdoor_plant WHERE plantid = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, plantId);
                    statement.executeUpdate();
                }

                // Delete from main plant table
                sql = "DELETE FROM plant WHERE plantid = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, plantId);
                    statement.executeUpdate();
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "redirect:/error";
        }
        return "redirect:/plantList";
    }
}
