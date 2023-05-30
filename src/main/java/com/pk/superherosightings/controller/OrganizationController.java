/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.superherosightings.controller;

import com.pk.superherosightings.model.Hero;
import com.pk.superherosightings.model.Organization;
import com.pk.superherosightings.service.HeroServiceLayer;
import com.pk.superherosightings.service.OrganizationServiceLayer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Pritpal
 */

@Controller
public class OrganizationController {
  
    @Autowired
    OrganizationServiceLayer organizationservice;
    
    @Autowired
    HeroServiceLayer heroservice;

    Set<ConstraintViolation<Organization>> violations = new HashSet<>();

    @GetMapping("organizations")
    public String displayOrganizations(Model model) {
        List<Organization> organizations = organizationservice.getAllOrganizations();
        model.addAttribute("organizations", organizations);
        model.addAttribute("errors", violations);
        return "organizations";
    }

    @PostMapping("addOrganization")
    public String addOrganization(Organization organization, HttpServletRequest request) {
        String[] heroIds = request.getParameterValues("heroId");

        List<Hero> heroes = new ArrayList<Hero>();
        if (heroIds != null && !Arrays.stream(heroIds).anyMatch("No Hero"::equals)) {
            for (String id : heroIds) {
                heroes.add(heroservice.getHeroById(Integer.parseInt(id)));
            }
        }
        organization.setMembers(heroes);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(organization);

        if (violations.isEmpty()) {
            organizationservice.addOrganization(organization);
        }

        return "redirect:/organizations";
    }

    @GetMapping("organizationDetails")
    public String organizationDetails(Integer id, Model model) {
        Organization organization = organizationservice.getOrganizationById(id);
        model.addAttribute("organization", organization);
        return "organizationDetails";
    }

    @GetMapping("deleteOrganization")
    public String deleteOrganization(Integer id, Model model) {
        organizationservice.deleteOrganizationById(id);
        return "redirect:/organizations";
    }

    @GetMapping("editOrganization")
    public String editOrganization(Integer id, Model model) {
        Organization organization = organizationservice.getOrganizationById(id);
        List<Hero> heroes = heroservice.getAllHeroes();
        model.addAttribute("organization", organization);
        model.addAttribute("heroes", heroes);
        return "editOrganization";
    }

    @PostMapping("editOrganization")
    public String performEditOrganization(@Valid Organization organization, BindingResult result, HttpServletRequest request, Model model) {
        if(result.hasErrors()){
            List<Organization> organizations = organizationservice.getAllOrganizations();
            model.addAttribute("organizations", organizations);
            List<Hero> heroes = heroservice.getAllHeroes();
            model.addAttribute("heroes", heroes);
            return "organization/editOrganization";
        }
        
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String address = request.getParameter("address");
        String contact = request.getParameter("contact");
        
        String[] heroIds = request.getParameterValues("heroId");
        List<Hero> heroes = new ArrayList<>();
        if(heroIds != null){
            for(String superheroId : heroIds){
                heroes.add(heroservice.getHeroById(Integer.parseInt(superheroId)));
            }
        }
        organizationservice.updateOrganization(organization);
        
        return "redirect:/organizations";
    }
}
