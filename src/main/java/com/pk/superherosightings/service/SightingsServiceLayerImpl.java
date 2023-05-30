/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.superherosightings.service;

import com.pk.superherosightings.dao.SightingsDao;
import com.pk.superherosightings.model.Sightings;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pritpal
 */
@Service
public class SightingsServiceLayerImpl implements SightingsServiceLayer{

    @Autowired
    SightingsDao sightingsDao;
    
    @Override
    public Sightings getSightingById(int id) {

        return sightingsDao.getSightingById(id);
    }

    @Override
    public List<Sightings> getAllSightings() {

        return sightingsDao.getAllSightings();
    }

    @Override
    public Sightings addSighting(Sightings sightings) {

        return sightingsDao.addSighting(sightings);
    }

    @Override
    public void updateSighting(Sightings sightings) {

        sightingsDao.updateSighting(sightings);
    }

    @Override
    public void deleteSightingById(int id) {

        sightingsDao.deleteSightingById(id);
    }

    @Override
    public List<Sightings> getSightingsByDate(LocalDate date) {

        return sightingsDao.getSightingsByDate(date);
    }

    @Override
    public List<Sightings> getLastTenSightings() {

        return sightingsDao.getMostRecentSightings();
    }
    
}
