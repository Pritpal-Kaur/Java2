/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.superherosightings.dao;

import com.pk.superherosightings.model.Hero;
import com.pk.superherosightings.model.Location;
import com.pk.superherosightings.model.Sightings;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Pritpal
 */
public interface SightingsDao {
    
    Sightings getSightingById(int id);
    List<Sightings> getAllSightings();
    Sightings addSighting(Sightings sighting);
    void updateSighting(Sightings sighting);
    void deleteSightingById(int id);
    List<Sightings> getSightingsByDate(LocalDate date);
    List<Sightings> getMostRecentSightings();
}
