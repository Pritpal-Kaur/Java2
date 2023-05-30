/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.superherosightings.service;

import com.pk.superherosightings.model.Sightings;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Pritpal
 */
public interface SightingsServiceLayer {
    
    Sightings getSightingById(int id);
    List<Sightings> getAllSightings();
    Sightings addSighting(Sightings sightings);
    void updateSighting(Sightings sightings);
    void deleteSightingById(int id);
    List<Sightings> getSightingsByDate(LocalDate date);
    List<Sightings> getLastTenSightings();
}
