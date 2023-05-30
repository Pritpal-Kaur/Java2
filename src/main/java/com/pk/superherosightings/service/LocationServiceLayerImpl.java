/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.superherosightings.service;

import com.pk.superherosightings.dao.LocationDao;
import com.pk.superherosightings.model.Hero;
import com.pk.superherosightings.model.Location;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pritpal
 */
@Service
public class LocationServiceLayerImpl implements LocationServiceLayer{
    

    @Autowired
    LocationDao locationDao;
    
    @Override
    public Location getLocationById(int id) {

        return locationDao.getLocationById(id);
    }

    @Override
    public List<Location> getAllLocations() {

        return locationDao.getAllLocations();
    }

    @Override
    public Location addLocation(Location location) {

        return locationDao.addLocation(location);
    }

    @Override
   public void updateLocation(Location location) {
       
       locationDao.updateLocation(location);
    }

    @Override
    public void deleteLocationById(int id) {

        locationDao.deleteLocationById(id);
    }

    @Override
    public List<Location> getLocationByHero(Hero hero) {

        return locationDao.getLocationsByHero(hero);
    }
    
}
