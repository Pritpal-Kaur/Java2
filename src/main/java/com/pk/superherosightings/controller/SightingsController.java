/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.superherosightings.controller;

import com.pk.superherosightings.model.Hero;
import com.pk.superherosightings.model.Location;
import com.pk.superherosightings.model.Sightings;
import com.pk.superherosightings.service.HeroServiceLayer;
import com.pk.superherosightings.service.LocationServiceLayer;
import com.pk.superherosightings.service.SightingsServiceLayer;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Pritpal
 */

@Controller
public class SightingsController {
    
  @Autowired
  SightingsServiceLayer sightingsservice;

  @Autowired
  HeroServiceLayer heroservice;
  
  @Autowired
  LocationServiceLayer locationservice;


    Set<ConstraintViolation<Sightings>> violations = new HashSet<>();

    @GetMapping("/")
    public String displayLastTenSightings(Model model) {
        List<Sightings> sightings = sightingsservice.getLastTenSightings();
        model.addAttribute("sightings", sightings);
        return "index";
    }

    @GetMapping("sightings")
    public String displaySightings(Model model) {
        List<Sightings> sightings = sightingsservice.getAllSightings();
        List<Hero> heroes = heroservice.getAllHeroes();
        List<Location> locations = locationservice.getAllLocations();
        LocalDate now = LocalDate.now();
        model.addAttribute("sightings", sightings);
        model.addAttribute("heroes", heroes);
        model.addAttribute("locations", locations);
        model.addAttribute("now", now);
        model.addAttribute("errors", violations);
        return "sightings";
    }
    

    @GetMapping("sightingsByDate")
    public String displaySightingsByDate(String date, Model model) {
        LocalDate sightingDate = LocalDate.parse(date);
        List<Sightings> sightings = sightingsservice.getSightingsByDate(sightingDate);
        List<Hero> heroes = heroservice.getAllHeroes();
        List<Location> locations = locationservice.getAllLocations();
        model.addAttribute("sightings", sightings);
        model.addAttribute("heroes", heroes);
        model.addAttribute("locations", locations);
        model.addAttribute("now", date);
        model.addAttribute("errors", violations);
        return "sightings";
    }

     @PostMapping("addSighting")
    public String addSighting(String heroId, String locationId, String date, HttpServletRequest request) {
        
        Hero hero = heroservice.getHeroById(Integer.parseInt(heroId));
        Location location = locationservice.getLocationById(Integer.parseInt(locationId));
        LocalDate sightingDate = LocalDate.parse(date);
        
        Sightings sighting = new Sightings();
        sighting.setHero(hero);
        sighting.setLocation(location);
        sighting.setSightingDate(sightingDate);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(sighting);

        if (violations.isEmpty()) {
            sightingsservice.addSighting(sighting);
        }

        return "redirect:/sightings";
    }

    @GetMapping("deleteSighting")
    public String deleteSighting(Integer id) {
        sightingsservice.deleteSightingById(id);
        return "redirect:/sightings";
    }

    @GetMapping("editSighting")
    public String editSighting(HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getParameter("id"));
        Sightings sighting = sightingsservice.getSightingById(id);
        List<Hero> heroes = heroservice.getAllHeroes();
        List<Location> locations = locationservice.getAllLocations();
        LocalDate now = LocalDate.now();
        model.addAttribute("sighting", sighting);
        model.addAttribute("heroes", heroes);
        model.addAttribute("locations", locations);
        model.addAttribute("now", now);
        return "editSighting";
    }

    @PostMapping("editSighting")
    public String performEditSighting(String id, String heroId, String locationId, String date, HttpServletRequest request) {
        Hero hero = heroservice.getHeroById(Integer.parseInt(heroId));
        Location location = locationservice.getLocationById(Integer.parseInt(locationId));
        LocalDate sightingDate = LocalDate.parse(date);

        Sightings sighting = new Sightings();
        sighting.setId(Integer.parseInt(id));
        sighting.setHero(hero);
        sighting.setLocation(location);
        sighting.setSightingDate(sightingDate);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(sighting);

        if (violations.isEmpty()) {
            sightingsservice.updateSighting(sighting);
        }

        return "redirect:/sightings";
    }
}
