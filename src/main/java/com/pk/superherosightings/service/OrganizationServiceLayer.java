/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.superherosightings.service;

import com.pk.superherosightings.model.Hero;
import com.pk.superherosightings.model.Organization;
import java.util.List;

/**
 *
 * @author Pritpal
 */
public interface OrganizationServiceLayer {
    
    Organization getOrganizationById(int id);
    List<Organization> getAllOrganizations();
    Organization addOrganization(Organization organization);
    void updateOrganization(Organization organization);
    void deleteOrganizationById(int id);
    List<Organization> getOrganizationByHero(Hero hero);
}
