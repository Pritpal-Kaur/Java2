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
public class LocationDaoDBTest {

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

    public LocationDaoDBTest() {
    }

    @BeforeEach
    public void setUp() {
        List<Sightings> sightings = sightingsDao.getAllSightings();
        for (Sightings sighting : sightings) {
            sightingsDao.deleteSightingById(sighting.getId());
        }

        List<Hero> heroes = heroDao.getAllHeroes();
        for (Hero hero : heroes) {
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
    public void testAddGetLocation() {
        Location location = new Location();
        location.setName("Somewhere");
        location.setDescription("");
        location.setAddressInfo("");
        location.setLatitude("");
        location.setLongitude("");
        location = locationDao.addLocation(location);

        Location fromDao = locationDao.getLocationById(location.getId());
        assertEquals(location, fromDao);
    }

    @Test
    public void testGetAllLocations() {
        Location location = new Location();
        location.setName("Somewhere");
        location.setDescription("");
        location.setAddressInfo("");
        location.setLatitude("");
        location.setLongitude("");
        location = locationDao.addLocation(location);

        Location location2 = new Location();
        location2.setName("Another Place");
        location2.setDescription("");
        location2.setAddressInfo("");
        location2.setLatitude("");
        location2.setLongitude("");
        location2 = locationDao.addLocation(location2);

        List<Location> locations = locationDao.getAllLocations();
        assertEquals(2, locations.size());
        assertTrue(locations.contains(location));
        assertTrue(locations.contains(location2));
    }

    @Test
    public void testUpdateLocation() {
        Location location = new Location();
        location.setName("Somewhere");
        location.setDescription("");
        location.setAddressInfo("");
        location.setLatitude("");
        location.setLongitude("");
        location = locationDao.addLocation(location);

        Location fromDao = locationDao.getLocationById(location.getId());
        assertEquals(location, fromDao);

        location.setName("Somewhere Else");
        locationDao.updateLocation(location);
        assertNotEquals(location, fromDao);

        fromDao = locationDao.getLocationById(location.getId());
        assertEquals(location, fromDao);
    }

    @Test
    public void testDeleteLocation() {
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
        sighting.setSightingDate(LocalDate.now());
        sighting = sightingsDao.addSighting(sighting);

        locationDao.deleteLocationById(location.getId());

        Sightings sightingFromDao = sightingsDao.getSightingById(sighting.getId());
        assertNull(sightingFromDao);

        Location locationFromDao = locationDao.getLocationById(location.getId());
        assertNull(locationFromDao);
    }

    @Test
    public void testGetLocationBySuper() {
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
        sighting.setSightingDate(LocalDate.now());
        sighting = sightingsDao.addSighting(sighting);

        List<Location> locations = locationDao.getLocationsByHero(hero);
        assertTrue(locations.contains(location));
    }

}
