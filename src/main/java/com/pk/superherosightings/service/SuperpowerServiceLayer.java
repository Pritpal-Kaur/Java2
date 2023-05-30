/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.superherosightings.service;

import com.pk.superherosightings.model.Hero;
import com.pk.superherosightings.model.Superpower;
import java.util.List;

/**
 *
 * @author Pritpal
 */
public interface SuperpowerServiceLayer {
    
    Superpower getSuperpowerById(int id);
    List<Superpower> getAllSuperpowers();
    Superpower addSuperpower(Superpower superpower);
    void updateSuperpower(Superpower superpower);
    void deleteSuperpowerById(int id);
    Superpower getSuperpowerByHero(Hero hero);
    
}
