package com.heroku.java.controller;

import com.heroku.java.model.PetFood;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
public class PetFoodController {

    private final String PET_FOOD_API_URL = "https://arcane-bayou-40259-cef3a9e1cac6.herokuapp.com/api/petfoods";

    @GetMapping("/petfoodlistapi")
    public String getPetFoodList(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        PetFood[] petFoodArray = restTemplate.getForObject(PET_FOOD_API_URL, PetFood[].class);
        List<PetFood> petFoods = Arrays.asList(petFoodArray);
        model.addAttribute("petFoods", petFoods);
        return "petfoodlist"; // Name of the Thymeleaf template
    }
}
