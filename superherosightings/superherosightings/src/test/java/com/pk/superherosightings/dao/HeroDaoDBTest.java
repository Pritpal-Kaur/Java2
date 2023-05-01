/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.superherosightings.dao;


import com.pk.superherosightings.model.Hero;
import com.pk.superherosightings.model.Location;
import com.pk.superherosightings.model.Organization;
import com.pk.superherosightings.model.Sightings;
import com.pk.superherosightings.model.Superpower;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author Pritpal
 */

@SpringBootTest
public class HeroDaoDBTest {
   
    @Autowired
    HeroDao heroDao;
    
    @Autowired
    SuperpowerDao superpowerDao;
    
    @Autowired
    OrganizationDao organizationDao;
    
    @Autowired
    LocationDao locationDao;
    
    @Autowired
    SightingsDao sightingsDao;
    
    public HeroDaoDBTest() {
        
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    
    @BeforeEach
    public void setUp() {
        
        
        List<Location> locations = locationDao.getAllLocations();
        for (Location location : locations) {
            locationDao.deleteLocationById(location.getId());
        }

        List<Hero> supers = heroDao.getAllHeroes();
        for (Hero hero : supers) {
            heroDao.deleteHeroById(hero.getId());
        }

        List<Superpower> powers = superpowerDao.getAllSuperpowers();
        for (Superpower power : powers) {
            superpowerDao.deleteSuperpowerById(power.getId());
        }
        
        List<Sightings> sightings = sightingsDao.getAllSightings();
        for (Sightings sighting : sightings) {
            sightingsDao.deleteSightingById(sighting.getId());
        }
    }
    
    @Test
    public void testAddGetHero() {
        Superpower power = new Superpower();
        power.setName("Super strength");
        power = superpowerDao.addSuperpower(power);

        Hero hero = new Hero();
        hero.setName("Superman");
        hero.setDescription("Kal-El");
        hero.setSuperpower(power);
        hero.setOrganizations(new ArrayList<Organization>());
        hero = heroDao.addHero(hero);

        Hero fromDao = heroDao.getHeroById(hero.getId());
        assertEquals(hero, fromDao);
    }
    
    @Test
    public void testGetAllSupers() {
        Superpower power = new Superpower();
        power.setName("Super strength");
        power = superpowerDao.addSuperpower(power);

        Superpower power2 = new Superpower();
        power2.setName("Super speed");
        power2 = superpowerDao.addSuperpower(power2);

        Hero hero = new Hero();
        hero.setName("Superman");
        hero.setDescription("Kal-El");
        hero.setSuperpower(power);
        hero.setOrganizations(new ArrayList<Organization>());
        hero = heroDao.addHero(hero);

        Hero hero2 = new Hero();
        hero2.setName("Captain America");
        hero2.setDescription("America's sweetheart");
        hero2.setSuperpower(power);
        hero2.setOrganizations(new ArrayList<Organization>());
        hero2 = heroDao.addHero(hero2);

        Hero hero3 = new Hero();
        hero3.setName("Flash");
        hero3.setDescription("Fast guy in DC");
        hero3.setSuperpower(power2);
        hero3.setOrganizations(new ArrayList<Organization>());
        hero3 = heroDao.addHero(hero3);

        List<Hero> heroes = heroDao.getAllHeroes();
        assertEquals(3, heroes.size());
        assertTrue(heroes.contains(hero));
        assertTrue(heroes.contains(hero2));
        assertTrue(heroes.contains(hero3));
    }

    @Test
    public void testUpdateSuper() {
        Superpower power = new Superpower();
        power.setName("Super strength");
        power = superpowerDao.addSuperpower(power);

        Hero hero = new Hero();
        hero.setName("Superman");
        hero.setDescription("Kal-El");
        hero.setSuperpower(power);
        hero.setOrganizations(new ArrayList<Organization>());
        hero = heroDao.addHero(hero);

        Hero fromDao = heroDao.getHeroById(hero.getId());
        assertEquals(hero, fromDao);

        hero.setName("Superboy");
        heroDao.updateHero(hero);

        assertNotEquals(hero, fromDao);

        fromDao = heroDao.getHeroById(hero.getId());
        assertEquals(hero, fromDao);
    }
    
    @Test
    public void testDeleteSuper() {
        Superpower power = new Superpower();
        power.setName("Super strength");
        power = superpowerDao.addSuperpower(power);

        Hero hero = new Hero();
        hero.setName("Superman");
        hero.setDescription("Kal-El");
        hero.setSuperpower(power);
        hero.setOrganizations(new ArrayList<Organization>());
        hero = heroDao.addHero(hero);

        Hero fromDao = heroDao.getHeroById(hero.getId());
        assertEquals(hero, fromDao);

        heroDao.deleteHeroById(hero.getId());
        fromDao = heroDao.getHeroById(hero.getId());
        assertNull(fromDao);

    }
    
    @Test
    public void testGetSupersByLocation() {
        Superpower power = new Superpower();
        power.setName("Super strength");
        power = superpowerDao.addSuperpower(power);

        Superpower power2 = new Superpower();
        power2.setName("Super speed");
        power2 = superpowerDao.addSuperpower(power2);

        Hero hero = new Hero();
        hero.setName("Superman");
        hero.setDescription("Kal-El");
        hero.setSuperpower(power);
        hero.setOrganizations(new ArrayList<Organization>());
        hero = heroDao.addHero(hero);

        Hero hero2 = new Hero();
        hero2.setName("Captain America");
        hero2.setDescription("America's sweetheart");
        hero2.setSuperpower(power);
        hero2.setOrganizations(new ArrayList<Organization>());
        hero2 = heroDao.addHero(hero2);

        Hero hero3 = new Hero();
        hero3.setName("Flash");
        hero3.setDescription("Fast guy in DC");
        hero3.setSuperpower(power2);
        hero3.setOrganizations(new ArrayList<Organization>());
        hero3 = heroDao.addHero(hero3);

        Location location = new Location();
        location.setAddressInfo("Daily Planet address");
        location = locationDao.addLocation(location);

        Location location2 = new Location();
        location2.setAddressInfo("Flash address");
        location2 = locationDao.addLocation(location2);

        Sightings sighting = new Sightings();
        sighting.setHero(hero);
        sighting.setLocation(location);
        sighting.setSightingDate(LocalDate.now());
        sighting = sightingsDao.addSighting(sighting);

        Sightings sighting2 = new Sightings();
        sighting2.setHero(hero2);
        sighting2.setLocation(location);
        sighting2.setSightingDate(LocalDate.now());
        sighting2 = sightingsDao.addSighting(sighting2);

        Sightings sighting3 = new Sightings();
        sighting3.setHero(hero3);
        sighting3.setLocation(location2);
        sighting3.setSightingDate(LocalDate.now());
        sighting3 = sightingsDao.addSighting(sighting3);

        List<Hero> heroes = heroDao.getHeroesByLocation(location);
        assertEquals(2, heroes.size());
        assertTrue(heroes.contains(hero));
        assertTrue(heroes.contains(hero2));
        assertFalse(heroes.contains(hero3));

    }

}

