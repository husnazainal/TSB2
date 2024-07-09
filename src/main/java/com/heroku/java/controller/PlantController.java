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
                    statement.setString(2, indoorPlant.getLightr());
                    statement.setString(3, indoorPlant.getHumidp());
                    statement.setString(4, indoorPlant.getWaterf());
                    statement.executeUpdate();
                }
            } else if ("Outdoor".equals(plant.getType())) {
                outdoorPlant.setPlantId(newPlantId);
                sql = "INSERT INTO outdoor_plant(plantid, sune, windr, soilt) VALUES (?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, plant.getPlantId());
                    statement.setString(2, outdoorPlant.getSune());
                    statement.setString(3, outdoorPlant.getWindr());
                    statement.setString(4, outdoorPlant.getSoilt());
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
                    indoorPlant.setLightr(resultSet.getString("lightr"));
                    indoorPlant.setHumidp(resultSet.getString("humidp"));
                    indoorPlant.setWaterf(resultSet.getString("waterf"));
                    plant = indoorPlant;
                } else if ("Outdoor".equals(type)) {
                    OutdoorPlant outdoorPlant = new OutdoorPlant();
                    outdoorPlant.setSune(resultSet.getString("sune"));
                    outdoorPlant.setWindr(resultSet.getString("windr"));
                    outdoorPlant.setSoilt(resultSet.getString("soilt"));
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Database error: " + e.getMessage());
            return "error";
        }
        model.addAttribute("plants", plants);
        return "plantlist";
    }

    @GetMapping("/updatePlant")
    public String showUpdatePlantForm(@RequestParam("plantId") int plantId, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT p.plantid, p.sciname, p.comname, p.type, p.habitat, p.species, p.description, "
                    + "i.lightr, i.humidp, i.waterf, "
                    + "o.sune, o.windr, o.soilt "
                    + "FROM plant p "
                    + "LEFT JOIN indoor_plant i ON p.plantid = i.plantid "
                    + "LEFT JOIN outdoor_plant o ON p.plantid = o.plantid "
                    + "WHERE p.plantid = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, plantId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        plant plant;
                        String type = resultSet.getString("type");

                        if ("Indoor".equals(type)) {
                            IndoorPlant indoorPlant = new IndoorPlant();
                            indoorPlant.setLightr(resultSet.getString("lightr"));
                            indoorPlant.setHumidp(resultSet.getString("humidp"));
                            indoorPlant.setWaterf(resultSet.getString("waterf"));
                            plant = indoorPlant;
                        } else if ("Outdoor".equals(type)) {
                            OutdoorPlant outdoorPlant = new OutdoorPlant();
                            outdoorPlant.setSune(resultSet.getString("sune"));
                            outdoorPlant.setWindr(resultSet.getString("windr"));
                            outdoorPlant.setSoilt(resultSet.getString("soilt"));
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
    @Transactional
    public String updatePlant(@ModelAttribute("plant") plant plant) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try {
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

                if (plant instanceof IndoorPlant) {
                    IndoorPlant indoorPlant = (IndoorPlant) plant;
                    sql = "UPDATE indoor_plant SET lightr = ?, humidp = ?, waterf = ? WHERE plantid = ?";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setString(1, indoorPlant.getLightr());
                        statement.setString(2, indoorPlant.getHumidp());
                        statement.setString(3, indoorPlant.getWaterf());
                        statement.setInt(4, plant.getPlantId());
                        statement.executeUpdate();
                    }
                } else if (plant instanceof OutdoorPlant) {
                    OutdoorPlant outdoorPlant = (OutdoorPlant) plant;
                    sql = "UPDATE outdoor_plant SET sune = ?, windr = ?, soilt = ? WHERE plantid = ?";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setString(1, outdoorPlant.getSune());
                        statement.setString(2, outdoorPlant.getWindr());
                        statement.setString(3, outdoorPlant.getSoilt());
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
    @Transactional
    public String deletePlant(@RequestParam("plantId") int plantId) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try {
                String sql = "DELETE FROM indoor_plant WHERE plantid = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, plantId);
                    statement.executeUpdate();
                }

                sql = "DELETE FROM outdoor_plant WHERE plantid = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, plantId);
                    statement.executeUpdate();
                }

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
