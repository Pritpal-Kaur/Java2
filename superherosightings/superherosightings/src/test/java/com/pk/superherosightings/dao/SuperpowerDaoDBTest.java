/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.superherosightings.dao;

import com.pk.superherosightings.model.Hero;
import com.pk.superherosightings.model.Organization;
import com.pk.superherosightings.model.Superpower;
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
public class SuperpowerDaoDBTest {
    
    @Autowired
    SuperpowerDao superpowerDao;

    @Autowired
    HeroDao heroDao;

    @Autowired
    OrganizationDao orgDao;

    public SuperpowerDaoDBTest() {
    }

    @BeforeEach
    public void setUp() {
        List<Hero> heroes = heroDao.getAllHeroes();
        for (Hero hero : heroes) {
            heroDao.deleteHeroById(hero.getId());
        }

        List<Superpower> powers = superpowerDao.getAllSuperpowers();
        for (Superpower power : powers) {
            superpowerDao.deleteSuperpowerById(power.getId());
        }
    }

    @Test
    public void testAddGetPower() {
        Superpower power = new Superpower();
        power.setName("Super strength");
        power = superpowerDao.addSuperpower(power);

        Superpower fromDao = superpowerDao.getSuperpowerById(power.getId());
        assertEquals(power, fromDao);
    }

    @Test
    public void testGetAllPowers() {
        Superpower power = new Superpower();
        power.setName("Super strength");
        power = superpowerDao.addSuperpower(power);

        Superpower power2 = new Superpower();
        power2.setName("Flying");
        power2 = superpowerDao.addSuperpower(power2);

        Superpower power3 = new Superpower();
        power3.setName("Super speed");
        power3 = superpowerDao.addSuperpower(power3);

        List<Superpower> superpowers = superpowerDao.getAllSuperpowers();

        assertEquals(3, superpowers.size());
        assertTrue(superpowers.contains(power));
        assertTrue(superpowers.contains(power2));
        assertTrue(superpowers.contains(power3));
    }

    @Test
    public void testUpdatePower() {
        Superpower power = new Superpower();
        power.setName("Super strength");
        power = superpowerDao.addSuperpower(power);

        Superpower fromDao = superpowerDao.getSuperpowerById(power.getId());
        assertEquals(power, fromDao);

        power.setName("Super speed");
        superpowerDao.updateSuperpower(power);

        assertNotEquals(power, fromDao);

        fromDao = superpowerDao.getSuperpowerById(power.getId());
        assertEquals(power, fromDao);
    }

    @Test
    public void testDeletePower() {
        Superpower power = new Superpower();
        power.setName("Super strength");
        power = superpowerDao.addSuperpower(power);

        Hero hero = new Hero();
        hero.setName("Superman");
        hero.setDescription("The face of superheros (not really!)");
        hero.setSuperpower(power);
        hero.setOrganizations(new ArrayList<Organization>());
        hero = heroDao.addHero(hero);

        Superpower fromDao = superpowerDao.getSuperpowerById(power.getId());
        assertEquals(power, fromDao);

        superpowerDao.deleteSuperpowerById(power.getId());

        fromDao = superpowerDao.getSuperpowerById(power.getId());
        assertNull(fromDao);

        hero = heroDao.getHeroById(hero.getId());
        assertNull(hero.getSuperpower());

    }

}
