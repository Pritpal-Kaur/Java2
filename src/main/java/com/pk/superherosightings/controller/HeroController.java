/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.superherosightings.controller;

import com.pk.superherosightings.model.Hero;
import com.pk.superherosightings.model.Location;
import com.pk.superherosightings.model.Organization;
import com.pk.superherosightings.model.Superpower;
import com.pk.superherosightings.service.HeroServiceLayer;
import com.pk.superherosightings.service.LocationServiceLayer;
import com.pk.superherosightings.service.OrganizationServiceLayer;
import com.pk.superherosightings.service.SightingsServiceLayer;
import com.pk.superherosightings.service.SuperpowerServiceLayer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Pritpal
 */

@Controller
public class HeroController {
  
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

Set<ConstraintViolation<Hero>> violations = new HashSet<>();

@GetMapping("heroes")
public String displayHeroes(Model model) {
  List<Hero> heroes = heroservice.getAllHeroes();
  List<Superpower> superpowers = superpowerservice.getAllSuperpowers();
  List<Organization> organizations = organizationservice.getAllOrganizations();
  model.addAttribute("heroes", heroes);
  model.addAttribute("superpowers", superpowers);
  model.addAttribute("organizations", organizations);
  model.addAttribute("errors", violations);
  return "heroes";
}

@PostMapping("addHero")
public String addHero(Hero hero,HttpServletRequest request, @RequestParam("picture") MultipartFile pictureFile) {
    String superpowerId = request.getParameter("superpowerId");
    String[] organizationsId = request.getParameterValues("organizationId");
    
    
    if(superpowerId != null && superpowerId.equals("No Power")) {
        hero.setSuperpower(null);
    } else if (superpowerId != null){
        hero.setSuperpower(superpowerservice.getSuperpowerById(Integer.parseInt(superpowerId)));
    }
    
    List<Organization> organizations = new ArrayList<Organization>();
    if(organizationsId != null && !Arrays.stream(organizationsId).anyMatch("No Organization"::equals)) {
        for(String id : organizationsId) {
            organizations.add(organizationservice.getOrganizationById(Integer.parseInt(id)));
        }
    }
    hero.setOrganizations(organizations);
    
    Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
    violations = validate.validate(hero);
    
    if(violations.isEmpty()) {
        heroservice.addHero(hero);
    }
    return"redirect:/heroes";
}

@GetMapping("deleteHero")
public String deletehero(HttpServletRequest request) {
    int id = Integer.parseInt(request.getParameter("id"));
    heroservice.deleteHeroById(id);
    return "redirect:/heroes";
}

@GetMapping("heroDetails")
public String heroDetails(Integer id, Model model) {
    Hero hero = heroservice.getHeroById(id);
    model.addAttribute("Hero", hero);
    return "heroDetails";
}

@GetMapping("editHero")
public String editHero(HttpServletRequest request, Model model) {
    int id = Integer.parseInt(request.getParameter("id"));
    Hero hero = heroservice.getHeroById(id);
    List<Superpower> superpowers  = superpowerservice.getAllSuperpowers();
    List<Organization> organizations = organizationservice.getAllOrganizations();
    model.addAttribute("hero", hero);
    model.addAttribute("superpowers", superpowers);
    model.addAttribute("organizations", organizations);
    return "editHero";
}

@PostMapping("editHero")
public String performEditHero(Hero hero, HttpServletRequest request) {
    int id = Integer.parseInt(request.getParameter("id"));
    hero.setName(request.getParameter("name"));
    hero.setDescription(request.getParameter("description"));
    
    String superpowerId = request.getParameter("superpowerId");
    String[] orgIds = request.getParameterValues("organizationId");

        if (superpowerId != null && superpowerId.equals("No Power")) {
            hero.setSuperpower(null);
        } else {
            hero.setSuperpower(superpowerservice.getSuperpowerById(Integer.parseInt(superpowerId)));
        }

        List<Organization> orgs = new ArrayList<Organization>();
        if (orgIds != null && !Arrays.stream(orgIds).anyMatch("No Organization"::equals)) {
            for (String organizationId : orgIds) {
                orgs.add(organizationservice.getOrganizationById(Integer.parseInt(organizationId)));
            }
        }
        hero.setOrganizations(orgs);

        heroservice.updateHero(hero);
      return "redirect:/heroes";
}
}
