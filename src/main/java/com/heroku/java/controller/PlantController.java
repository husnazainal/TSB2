package com.heroku.java.controller;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.model.plant;


@Controller
public class PlantController {
    private final DataSource dataSource;

    @Autowired
    public PlantController(DataSource dataSource) {
        this.dataSource = dataSource;
    }
	
@PostMapping("/addPlant") 

public String addPlant(
	@RequestParam String plantSciname,
	@RequestParam String plantComname,
	@RequestParam String plantType,
	@RequestParam(required = false) String lightRequirements,
	@RequestParam(required = false) String humidityPreferences,
	@RequestParam(required = false) String wateringFrequency,
	@RequestParam(required = false) String sunlightExposure,
	@RequestParam(required = false) String windResistance,
	@RequestParam(required = false) String soilType,
	@RequestParam String plantHabitat,
	@RequestParam String plantSpecies,
	@RequestParam String plantDesc) 
{
try (Connection connection = dataSource.getConnection()) {
	String sql = "INSERT INTO public.plants (scientific_name, common_name, type, light_requirements, " +
                         "humidity_preferences, watering_frequency, sunlight_exposure, wind_resistance, " +
                         "soil_type, habitat, species, description) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	final var statement = connection.prepareStatement(sql);

	statement.setString(1, plantSciname);
	statement.setString(2, plantComname);
	statement.setString(3, plantType);
	statement.setString(4, lightRequirements);
	statement.setString(5, humidityPreferences);
	statement.setString(6, wateringFrequency);
	statement.setString(7, sunlightExposure);
	statement.setString(8, windResistance);
	statement.setString(9, soilType);
	statement.setString(10, plantHabitat);
	statement.setString(11, plantSpecies);
	statement.setString(12, plantDesc);

	statement.executeUpdate();
	connection.close();

} catch (Exception e) {
e.printStackTrace();
//return "redirect:/error";
} 

return "redirect:/index";
}

@GetMapping("/plantList")
public String plantList(Model model) {
	List<plant> plants = new ArrayList<>();

	try (Connection connection = dataSource.getConnection()) {
		String sql = "SELECT plantid, scientific_name, common_name, type, lightRequirements habitat, humidityPreferences, wateringFrequency, sunlightExposure, windResistance, soilType, species, description FROM plants ORDER BY plantid";
		final var statement = connection.prepareStatement(sql);
		final var resultSet = statement.executeQuery();

		while (resultSet.next()) {
			int plantId = resultSet.getInt("plantid");
			String plantSciname = resultSet.getString("scientific_name");
			String plantComname = resultSet.getString("common_name");
			String plantType = resultSet.getString("type");
			String lightRequirements = resultSet.getString("lightRequirements");
			String humidityPreferences = resultSet.getString("humidityPreferences");
			String wateringFrequency = resultSet.getString("wateringFrequency");
			String sunlightExposure = resultSet.getString("sunlightExposure");
			String windResistance = resultSet.getString("windResistance");
			String soilType = resultSet.getString("soilType");
			String plantHabitat = resultSet.getString("habitat");
			String plantSpecies = resultSet.getString("species");
			String plantDesc = resultSet.getString("description");

			plant plant = new plant();
                plant.setPlantId(plantId);
                plant.setPlantSciname(plantSciname);
                plant.setPlantComname(plantComname);
                plant.setPlantType(plantType);
                plant.setLightRequirements(lightRequirements);
                plant.setHumidityPreferences(humidityPreferences);
                plant.setWateringFrequency(wateringFrequency);
                plant.setSunlightExposure(sunlightExposure);
                plant.setWindResistance(windResistance);
                plant.setSoilType(soilType);
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

@GetMapping("/updatePlants")
public String updatePlant(@RequestParam("plantId") int plantId, Model model) {

    try {
        Connection connection = dataSource.getConnection();
		String sql = "SELECT plantid, scientific_name, common_name, type, habitat, species, description FROM plants WHERE plantid = ?";
        final var statement= connection.prepareStatement(sql);
		statement.setInt(1, plantId);
        final var resultSet = statement.executeQuery();

        if (resultSet.next()){
            
			String plantSciname = resultSet.getString("scientific_name");
                String plantComname = resultSet.getString("common_name");
                String plantType = resultSet.getString("type");
                String lightRequirements = resultSet.getString("lightRequirements");
                String humidityPreferences = resultSet.getString("humidityPreferences");
                String wateringFrequency = resultSet.getString("wateringFrequency");
                String sunlightExposure = resultSet.getString("sunlightExposure");
                String windResistance = resultSet.getString("windResistance");
                String soilType = resultSet.getString("soilType");
                String plantHabitat = resultSet.getString("habitat");
                String plantSpecies = resultSet.getString("species");
                String plantDesc = resultSet.getString("description");

			    plant plant = new plant();
                plant.setPlantId(plantId);
                plant.setPlantSciname(plantSciname);
                plant.setPlantComname(plantComname);
                plant.setPlantType(plantType);
                plant.setLightRequirements(lightRequirements);
                plant.setHumidityPreferences(humidityPreferences);
                plant.setWateringFrequency(wateringFrequency);
                plant.setSunlightExposure(sunlightExposure);
                plant.setWindResistance(windResistance);
                plant.setSoilType(soilType);
                plant.setPlantHabitat(plantHabitat);
                plant.setPlantSpecies(plantSpecies);
                plant.setPlantDesc(plantDesc);

                model.addAttribute("plant", plant);

            connection.close();
        }
    }
    catch(Exception e){
        e.printStackTrace();
    }
    return "Plant/updatePlant";
}

@PostMapping("/updatePlants")
public String updatePlant(@ModelAttribute("plant") plant plant) {
 
    try {
        Connection connection = dataSource.getConnection();
		String sql = "UPDATE plants SET scientific_name = ?, common_name = ?, type = ?,lightRequirements = ?, humidityPreferences= ?, wateringFrequency= ?, sunlightExposure= ?, windResistance=?, soilType= ?, habitat = ?, species = ?, description = ? WHERE plantid = ?";
        final var statement=connection.prepareStatement(sql);

        statement.setString(1, plant.getPlantSciname());
        statement.setString(2, plant.getPlantComname());
        statement.setString(3, plant.getPlantType());
        statement.setString(4, plant.getLightRequirements());
        statement.setString(5, plant.getHumidityPreferences());
        statement.setString(6, plant.getWateringFrequency());
        statement.setString(7, plant.getSunlightExposure());
        statement.setString(8, plant.getWindResistance());
        statement.setString(9, plant.getSoilType());
        statement.setString(10, plant.getPlantHabitat());
        statement.setString(11, plant.getPlantSpecies());
        statement.setString(12, plant.getPlantDesc());
        statement.setInt(13, plant.getPlantId());

        statement.executeUpdate();

        connection.close();
        

    } catch (Exception e) {
        e.printStackTrace();
        return "redirect:/error";
    }
    return "redirect:/plantList";
}

@PostMapping("/deletePlant")
public String deletePlant(@RequestParam("plantId") int plantId){
    try {
        Connection connection = dataSource.getConnection();
		String sql = "DELETE FROM plants WHERE plantid = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1,plantId);
        statement.executeUpdate();

        connection.close();

    } catch (Exception e) {
        e.printStackTrace();
        return "redirect:/errorDelete";
    }
    return "redirect:/plantList";
}
}