/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.superherosightings.controller;

import com.pk.superherosightings.model.Hero;
import com.pk.superherosightings.model.Location;
import com.pk.superherosightings.service.HeroServiceLayer;
import com.pk.superherosightings.service.LocationServiceLayer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
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
public class LocationController {
    
    @Autowired
    LocationServiceLayer locationservice;
    
    @Autowired
    HeroServiceLayer heroservice;

    Set<ConstraintViolation<Location>> violations = new HashSet<>();

    @GetMapping("locations")
    public String displayLocations(Model model) {
        List<Location> locations = locationservice.getAllLocations();
        model.addAttribute("locations", locations);
        model.addAttribute("errors", violations);
        return "locations";
    }

    @PostMapping("addLocation")
    public String addLocation(Location location) {
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(location);

        if (violations.isEmpty()) {
            locationservice.addLocation(location);
        }

        return "redirect:/locations";
    }

    @GetMapping("locationDetails")
    public String locationDetails(Integer id, Model model) {
        Location location = locationservice.getLocationById(id);
        List<Hero> heroes = heroservice.getHeroByLocation(location);
        model.addAttribute("location", location);
        model.addAttribute("heroes", heroes);
        return "locationDetails";
    }

    @GetMapping("deleteLocation")
    public String deleteLocation(Integer id, Model model) {
        locationservice.deleteLocationById(id);
        return "redirect:/locations";
    }

    @GetMapping("editLocation")
    public String editLocation(Integer id, Model model) {
        Location location = locationservice.getLocationById(id);
        model.addAttribute("location", location);
        return "editLocation";
    }

    @PostMapping("editLocation")
    public String performEditLocation(@Valid Location location, Model model) {
        locationservice.updateLocation(location);
        return "redirect:/locations";
    }
}
