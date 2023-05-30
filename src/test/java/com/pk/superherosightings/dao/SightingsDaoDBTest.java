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
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author Pritpal
 */

@SpringBootTest
public class SightingsDaoDBTest {
    
    @Autowired
    SuperpowerDao superpowerDao;

    @Autowired
    HeroDao heroDao;

    @Autowired
    OrganizationDao orgDao;

    @Autowired
    LocationDao locationDao;

    @Autowired
    SightingsDao sightingsDao;

    public SightingsDaoDBTest() {
    }

    @BeforeEach
    public void setUp() {
        List<Sightings> sightings = sightingsDao.getAllSightings();
        for (Sightings sighting : sightings) {
            sightingsDao.deleteSightingById(sighting.getId());
        }

        List<Hero> supers = heroDao.getAllHeroes();
        for (Hero hero : supers) {
            heroDao.deleteHeroById(hero.getId());
        }

        List<Superpower> powers = superpowerDao.getAllSuperpowers();
        for (Superpower power : powers) {
            superpowerDao.deleteSuperpowerById(power.getId());
        }

        List<Location> locations = locationDao.getAllLocations();
        for (Location location : locations) {
            locationDao.deleteLocationById(location.getId());
        }
    }
    
    
    @Test
    public void testAddGetSighting() {
        Superpower power = new Superpower();
        power.setName("Super strength");
        power = superpowerDao.addSuperpower(power);

        Hero hero = new Hero();
        hero.setName("Superman");
        hero.setDescription("Kal-El");
        hero.setSuperpower(power);
        hero.setOrganizations(new ArrayList<Organization>());
        hero = heroDao.addHero(hero);

        Location location = new Location();
        location.setName("Somewhere");
        location.setDescription("");
        location.setAddressInfo("");
        location.setLatitude("");
        location.setLongitude("");
        location = locationDao.addLocation(location);

        Sightings sighting = new Sightings();
        sighting.setHero(hero);
        sighting.setLocation(location);
        sighting.setSightingDate(LocalDate.of(2020, Month.SEPTEMBER, 3));
        sighting = sightingsDao.addSighting(sighting);

        Sightings fromDao = sightingsDao.getSightingById(sighting.getId());
        assertEquals(sighting, fromDao);
    }

    @Test
    public void testGetAllSightings() {
        Superpower power = new Superpower();
        power.setName("Super strength");
        power = superpowerDao.addSuperpower(power);

        Hero hero = new Hero();
        hero.setName("Superman");
        hero.setDescription("Kal-El");
        hero.setSuperpower(power);
        hero.setOrganizations(new ArrayList<Organization>());
        hero = heroDao.addHero(hero);

        Location location = new Location();
        location.setName("Somewhere");
        location.setDescription("");
        location.setAddressInfo("");
        location.setLatitude("");
        location.setLongitude("");
        location = locationDao.addLocation(location);

        Sightings sighting = new Sightings();
        sighting.setHero(hero);
        sighting.setLocation(location);
        sighting.setSightingDate(LocalDate.of(2020, Month.SEPTEMBER, 3));
        sighting = sightingsDao.addSighting(sighting);

        Sightings sighting2 = new Sightings();
        sighting2.setHero(hero);
        sighting2.setLocation(location);
        sighting2.setSightingDate(LocalDate.of(2020, Month.SEPTEMBER, 4));
        sighting2 = sightingsDao.addSighting(sighting2);

        List<Sightings> sightings = sightingsDao.getAllSightings();
        assertEquals(2, sightings.size());
        assertTrue(sightings.contains(sighting));
        assertTrue(sightings.contains(sighting2));
    }

    @Test
public void testUpdateSightings() {
    Superpower power = new Superpower();
    power.setName("Super strength");
    power = superpowerDao.addSuperpower(power);

    Hero hero = new Hero();
    hero.setName("Superman");
    hero.setDescription("Kal-El");
    hero.setSuperpower(power);
    hero.setOrganizations(new ArrayList<Organization>());
    hero = heroDao.addHero(hero);

    Location location = new Location();
    location.setName("Somewhere");
    location.setDescription("");
    location.setAddressInfo("");
    location.setLatitude("");
    location.setLongitude("");
    location = locationDao.addLocation(location);

    Sightings sighting = new Sightings();
    sighting.setHero(hero);
    sighting.setLocation(location);
    sighting.setSightingDate(LocalDate.of(2020, Month.SEPTEMBER, 3));
    sighting = sightingsDao.addSighting(sighting);

    Sightings fromDao = sightingsDao.getSightingById(sighting.getId());
    assertEquals(sighting, fromDao);

    sighting.setSightingDate(LocalDate.of(2020, Month.SEPTEMBER, 5));
    sightingsDao.updateSighting(sighting);
    assertNotEquals(sighting, fromDao);

    fromDao = sightingsDao.getSightingById(sighting.getId());
    assertEquals(sighting, fromDao);
}


    @Test
    public void testDeleteSighting() {
        Superpower power = new Superpower();
        power.setName("Super strength");
        power = superpowerDao.addSuperpower(power);

        Hero hero = new Hero();
        hero.setName("Superman");
        hero.setDescription("Kal-El");
        hero.setSuperpower(power);
        hero.setOrganizations(new ArrayList<Organization>());
        hero = heroDao.addHero(hero);

        Location location = new Location();
        location.setName("Somewhere");
        location.setDescription("");
        location.setAddressInfo("");
        location.setLatitude("");
        location.setLongitude("");
        location = locationDao.addLocation(location);

        Sightings sighting = new Sightings();
        sighting.setHero(hero);
        sighting.setLocation(location);
        sighting.setSightingDate(LocalDate.of(2020, Month.SEPTEMBER, 3));
        sighting = sightingsDao.addSighting(sighting);

        sightingsDao.deleteSightingById(sighting.getId());

        Sightings fromDao = sightingsDao.getSightingById(sighting.getId());
        assertNull(fromDao);
    }

    @Test
    public void testGetSightingByDate() {
        Superpower power = new Superpower();
        power.setName("Super strength");
        power = superpowerDao.addSuperpower(power);

        Hero hero = new Hero();
        hero.setName("Superman");
        hero.setDescription("Kal-El");
        hero.setSuperpower(power);
        hero.setOrganizations(new ArrayList<Organization>());
        hero = heroDao.addHero(hero);

        Hero hero2 = new Hero();
        hero2.setName("Batman");
        hero2.setDescription("The Dark Knight");
        hero2.setOrganizations(new ArrayList<Organization>());
        hero2 = heroDao.addHero(hero2);

        Location location = new Location();
        location.setName("Somewhere");
        location.setDescription("");
        location.setAddressInfo("");
        location.setLatitude("");
        location.setLongitude("");
        location = locationDao.addLocation(location);

        Sightings sighting = new Sightings();
        sighting.setHero(hero);
        sighting.setLocation(location);
        sighting.setSightingDate(LocalDate.of(2020, Month.SEPTEMBER, 3));
        sighting = sightingsDao.addSighting(sighting);

        Sightings sighting2 = new Sightings();
        sighting2.setHero(hero2);
        sighting2.setLocation(location);
        sighting2.setSightingDate(LocalDate.of(2020, Month.SEPTEMBER, 3));
        sighting2 = sightingsDao.addSighting(sighting2);

        List<Sightings> sightings = sightingsDao.getSightingsByDate(LocalDate.of(2020, Month.SEPTEMBER, 3));

        assertTrue(sightings.contains(sighting));
        assertTrue(sightings.contains(sighting2));
    }
}

