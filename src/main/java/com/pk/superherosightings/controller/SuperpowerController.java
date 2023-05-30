/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.superherosightings.controller;

import com.pk.superherosightings.model.Superpower;
import com.pk.superherosightings.service.HeroServiceLayer;
import com.pk.superherosightings.service.LocationServiceLayer;
import com.pk.superherosightings.service.OrganizationServiceLayer;
import com.pk.superherosightings.service.SightingsServiceLayer;
import com.pk.superherosightings.service.SuperpowerServiceLayer;
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
public class SuperpowerController {
    
@Autowired
HeroServiceLayer heroservice;

@Autowired
SuperpowerServiceLayer superpowerservice;

@Autowired
LocationServiceLayer locationservice;

@Autowired
OrganizationServiceLayer organizationservice;

@Autowired
SightingsServiceLayer sightingsservice;

Set<ConstraintViolation<Superpower>> violations = new HashSet<>();
Validator validate = Validation.buildDefaultValidatorFactory().getValidator();

@GetMapping("superpowers")   
public String displaySuperpowers(Model model) {
  List<Superpower> superpowers = superpowerservice.getAllSuperpowers();
  model.addAttribute("superpowers", superpowers);
  model.addAttribute("errors", violations);
  return "superpowers";
}

@PostMapping("addSuperpower")
public String addSuperpower(HttpServletRequest request) {
 String name = request.getParameter("name");
 Superpower superpower = new Superpower();
 superpower.setName(name);
 violations = validate.validate(superpower);
 if (violations.isEmpty()) {
            superpowerservice.addSuperpower(superpower);
        }
 return "redirect:/superpowers";
}

@GetMapping("deleteSuperpower")
public String deleteSuperpower(int id) {
    superpowerservice.deleteSuperpowerById(id);
    return "redirect:/superpowers";
}

@GetMapping("editSuperpower")
public String editSuperpower(HttpServletRequest request, Model model) {
    int id = Integer.parseInt(request.getParameter("id"));
    Superpower superpower = superpowerservice.getSuperpowerById(id);
    model.addAttribute("superpower", superpower);
    return "editSuperpower";
}

@PostMapping("editSuperpower")
public String performEditSuperpower(HttpServletRequest request) {
    int id = Integer.parseInt(request.getParameter("id"));
    Superpower superpower = superpowerservice.getSuperpowerById(id);
    superpower.setName(request.getParameter("name"));
    superpowerservice.updateSuperpower(superpower);
    return "redirect:/superpowers";
}
}
