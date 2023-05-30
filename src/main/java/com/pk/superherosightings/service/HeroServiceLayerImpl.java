/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.superherosightings.service;

import com.pk.superherosightings.dao.HeroDao;
import com.pk.superherosightings.model.Hero;
import com.pk.superherosightings.model.Location;
import com.pk.superherosightings.model.Organization;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pritpal
 */

@Service
public class HeroServiceLayerImpl implements HeroServiceLayer {
    
    @Autowired
    HeroDao heroDao;
    
    @Override
    public Hero getHeroById(int id) {

        return heroDao.getHeroById(id);
    }

    @Override
    public List<Hero> getAllHeroes() {

        return heroDao.getAllHeroes();
    }

    @Override
    public Hero addHero(Hero hero) {

        return heroDao.addHero(hero);
    }

    @Override
    public void updateHero(Hero hero) {

        heroDao.updateHero(hero);
    }

    @Override
    public void deleteHeroById(int id) {

        heroDao.deleteHeroById(id);
    }

    @Override
    public List<Hero> getHeroByLocation(Location location) {

        return heroDao.getHeroesByLocation(location);
    }

    @Override
    public List<Hero> getHeroByOrganization(Organization organization) {

        return heroDao.getHeroesByOrganization(organization);
    }
    
}
