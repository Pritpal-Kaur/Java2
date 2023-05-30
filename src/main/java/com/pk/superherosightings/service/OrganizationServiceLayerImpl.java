/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.superherosightings.service;

import com.pk.superherosightings.dao.OrganizationDao;
import com.pk.superherosightings.model.Hero;
import com.pk.superherosightings.model.Organization;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pritpal
 */
@Service
public class OrganizationServiceLayerImpl implements OrganizationServiceLayer{

    @Autowired
    OrganizationDao organizationDao;
    
    @Override
    public Organization getOrganizationById(int id) {

        return organizationDao.getOrganizationById(id);
    }

    @Override
    public List<Organization> getAllOrganizations() {

        return organizationDao.getAllOrganizations();
    }

    @Override
    public Organization addOrganization(Organization organization) {

        return organizationDao.addOrganization(organization);
    }

    @Override
    public void updateOrganization(Organization organization) {

        organizationDao.updateorganization(organization);
    }

    @Override
    public void deleteOrganizationById(int id) {

        organizationDao.deleteOrganizationById(id);
    }

    @Override
    public List<Organization> getOrganizationByHero(Hero hero) {

        return organizationDao.getOrganizationByHero(hero);
    }
    
    
}
