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
import org.springframework.web.bind.annotation.PathVariable;
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
        model.addAttribute("IndoorPlant", new IndoorPlant());
        model.addAttribute("OutdoorPlant", new OutdoorPlant());
        return "addplant";
    }

    @PostMapping("/addplant")
    @Transactional
    public String addPlant(@ModelAttribute("plant") plant plant,
            @ModelAttribute("IndoorPlant") IndoorPlant indoorPlant,
            @ModelAttribute("OutdoorPlant") OutdoorPlant outdoorPlant) {
        try (Connection connection = dataSource.getConnection()) {
            int newPlantId = generatePlantID(connection);
            plant.setPlantId(newPlantId);

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

            if ("Indoor".equals(plant.getType())) {
                indoorPlant.setPlantId(newPlantId);
                sql = "INSERT INTO indoor_plant(plantid, lightr, humidp, waterf) VALUES (?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, plant.getPlantId());
                    statement.setString(2, indoorPlant.getLightR());
                    statement.setString(3, indoorPlant.getHumidP());
                    statement.setString(4, indoorPlant.getWaterF());
                    statement.executeUpdate();
                }
            } else if ("Outdoor".equals(plant.getType())) {
                outdoorPlant.setPlantId(newPlantId);
                sql = "INSERT INTO outdoor_plant(plantid, sune, windr, soilt) VALUES (?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, plant.getPlantId());
                    statement.setString(2, outdoorPlant.getSunE());
                    statement.setString(3, outdoorPlant.getWindR());
                    statement.setString(4, outdoorPlant.getSoilT());
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "redirect:/error";
        }

        return "redirect:/plantlist";
    }

    private int generatePlantID(Connection connection) throws SQLException {
        String query = "SELECT COALESCE(MAX(plantid), 0) + 1 FROM plant";
        try (PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return 1;
            }
        }
    }

    @GetMapping("/plantlist")
    public String plantList(Model model) {
        List<plant> plants = new ArrayList<>();
        String sql = "SELECT p.plantid, p.sciname, p.comname, p.type, p.habitat, p.species, p.description, "
                + "i.lightr, i.humidp, i.waterf, "
                + "o.sune, o.windr, o.soilt "
                + "FROM plant p "
                + "LEFT JOIN indoor_plant i ON p.plantid = i.plantid "
                + "LEFT JOIN outdoor_plant o ON p.plantid = o.plantid "
                + "ORDER BY p.plantid";

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
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
                plant.setPlantId(resultSet.getInt("plantid"));
                plant.setSciName(resultSet.getString("sciname"));
                plant.setComName(resultSet.getString("comname"));
                plant.setType(type);
                plant.setHabitat(resultSet.getString("habitat"));
                plant.setSpecies(resultSet.getString("species"));
                plant.setDescription(resultSet.getString("description"));
                plants.add(plant);
                System.out.println("Added plant: " + plant.getSciName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Database error: " + e.getMessage());
            return "error";
        }

        model.addAttribute("plants", plants);
        System.out.println("Number of plants: " + plants.size());
        return "plantlist";
    }

    @GetMapping("/updatePlant/{plantId}")
    public String getUpdatePlantForm(@PathVariable("plantId") int plantId, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM plant WHERE plantid = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, plantId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        plant plant = new plant();
                        plant.setPlantId(resultSet.getInt("plantid"));
                        plant.setSciName(resultSet.getString("sciname"));
                        plant.setComName(resultSet.getString("comname"));
                        plant.setType(resultSet.getString("type"));
                        plant.setHabitat(resultSet.getString("habitat"));
                        plant.setSpecies(resultSet.getString("species"));
                        plant.setDescription(resultSet.getString("description"));

                        if ("Indoor".equals(plant.getType())) {
                            sql = "SELECT * FROM indoor_plant WHERE plantid = ?";
                            try (PreparedStatement indoorStatement = connection.prepareStatement(sql)) {
                                indoorStatement.setInt(1, plantId);
                                try (ResultSet indoorResultSet = indoorStatement.executeQuery()) {
                                    if (indoorResultSet.next()) {
                                        IndoorPlant indoorPlant = new IndoorPlant();
                                        indoorPlant.setLightR(indoorResultSet.getString("lightr"));
                                        indoorPlant.setHumidP(indoorResultSet.getString("humidp"));
                                        indoorPlant.setWaterF(indoorResultSet.getString("waterf"));
                                        model.addAttribute("IndoorPlant", indoorPlant);
                                    }
                                }
                            }
                        } else if ("Outdoor".equals(plant.getType())) {
                            sql = "SELECT * FROM outdoor_plant WHERE plantid = ?";
                            try (PreparedStatement outdoorStatement = connection.prepareStatement(sql)) {
                                outdoorStatement.setInt(1, plantId);
                                try (ResultSet outdoorResultSet = outdoorStatement.executeQuery()) {
                                    if (outdoorResultSet.next()) {
                                        OutdoorPlant outdoorPlant = new OutdoorPlant();
                                        outdoorPlant.setSunE(outdoorResultSet.getString("sune"));
                                        outdoorPlant.setWindR(outdoorResultSet.getString("windr"));
                                        outdoorPlant.setSoilT(outdoorResultSet.getString("soilt"));
                                        model.addAttribute("OutdoorPlant", outdoorPlant);
                                    }
                                }
                            }
                        }

                        model.addAttribute("plant", plant);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception appropriately, maybe redirect to an error page
            return "redirect:/error";
        }

        return "updatePlant"; // Return the Thymeleaf template name
    }

    @PostMapping("/updatePlant")
    @Transactional
    public String updatePlant(@ModelAttribute("plant") plant plant,
            @ModelAttribute("IndoorPlant") IndoorPlant indoorPlant,
            @ModelAttribute("OutdoorPlant") OutdoorPlant outdoorPlant) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try {
                // Update plant table
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

                // Update indoor_plant or outdoor_plant table
                if ("Indoor".equals(plant.getType())) {
                    sql = "UPDATE indoor_plant SET lightr = ?, humidp = ?, waterf = ? WHERE plantid = ?";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setString(1, indoorPlant.getLightR());
                        statement.setString(2, indoorPlant.getHumidP());
                        statement.setString(3, indoorPlant.getWaterF());
                        statement.setInt(4, plant.getPlantId());
                        statement.executeUpdate();
                    }
                    // Delete any existing outdoor plant data for this plant
                    sql = "DELETE FROM outdoor_plant WHERE plantid = ?";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setInt(1, plant.getPlantId());
                        statement.executeUpdate();
                    }
                } else if ("Outdoor".equals(plant.getType())) {
                    sql = "UPDATE outdoor_plant SET sune = ?, windr = ?, soilt = ? WHERE plantid = ?";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setString(1, outdoorPlant.getSunE());
                        statement.setString(2, outdoorPlant.getWindR());
                        statement.setString(3, outdoorPlant.getSoilT());
                        statement.setInt(4, plant.getPlantId());
                        statement.executeUpdate();
                    }
                    // Delete any existing indoor plant data for this plant
                    sql = "DELETE FROM indoor_plant WHERE plantid = ?";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setInt(1, plant.getPlantId());
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
        return "redirect:/plantlist";
    }

    @PostMapping("/deletePlant/{plantId}")
    @Transactional
    public String deletePlant(@PathVariable("plantId") int plantId) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try {
                // Delete from indoor_plant
                String sql = "DELETE FROM indoor_plant WHERE plantid = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, plantId);
                    statement.executeUpdate();
                }

                // Delete from outdoor_plant
                sql = "DELETE FROM outdoor_plant WHERE plantid = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, plantId);
                    statement.executeUpdate();
                }

                // Delete from plant
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
