/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.superherosightings.service;

import com.pk.superherosightings.model.Hero;
import com.pk.superherosightings.model.Location;
import com.pk.superherosightings.model.Organization;
import java.util.List;

/**
 *
 * @author Pritpal
 */
public interface HeroServiceLayer {
    
    Hero getHeroById(int id);
    List<Hero> getAllHeroes();
    Hero addHero(Hero hero);
    void updateHero(Hero hero);
    void deleteHeroById(int id);
    List<Hero> getHeroByLocation(Location location);
    List<Hero> getHeroByOrganization(Organization organization);
    
}
