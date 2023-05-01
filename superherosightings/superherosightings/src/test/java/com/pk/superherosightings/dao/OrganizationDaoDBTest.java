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
public class OrganizationDaoDBTest {

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

    public OrganizationDaoDBTest() {
    }

    @BeforeEach
    public void setUp() {
        List<Organization> orgs = orgDao.getAllOrganizations();
        for (Organization org : orgs) {
            orgDao.deleteOrganizationById(org.getId());
        }

        List<Hero> heroes = heroDao.getAllHeroes();
        for (Hero hero : heroes) {
            heroDao.deleteHeroById(hero.getId());
        }

        List<Superpower> superpowers = superpowerDao.getAllSuperpowers();
        for (Superpower power : superpowers) {
            superpowerDao.deleteSuperpowerById(power.getId());
        }
    }

    @Test
    public void testAddGetOrg() {
        Organization org = new Organization();
        org.setName("The Avengers");
        org.setDescription("Best group, no doubt.");
        org.setAddress("");
        org.setContact("");
        org.setMembers(new ArrayList<Hero>());
        org = orgDao.addOrganization(org);

        Organization fromDao = orgDao.getOrganizationById(org.getId());
        assertEquals(org, fromDao);
    }

    @Test
    public void testGetAllOrgs() {
        Organization org = new Organization();
        org.setName("The Avengers");
        org.setDescription("Best group, no doubt.");
        org.setAddress("");
        org.setContact("");
        org.setMembers(new ArrayList<Hero>());
        org = orgDao.addOrganization(org);

        Organization org2 = new Organization();
        org2.setName("Justice League");
        org2.setDescription("They're okay.");
        org2.setAddress("");
        org2.setContact("");
        org2.setMembers(new ArrayList<Hero>());
        org2 = orgDao.addOrganization(org2);

        List<Organization> orgs = orgDao.getAllOrganizations();
        assertEquals(2, orgs.size());
        assertTrue(orgs.contains(org));
        assertTrue(orgs.contains(org2));
    }

    @Test
    public void testUpdateOrg() {
        Organization org = new Organization();
        org.setName("The Avengers");
        org.setDescription("Best group, no doubt.");
        org.setAddress("");
        org.setContact("");
        org.setMembers(new ArrayList<Hero>());
        org = orgDao.addOrganization(org);

        Organization fromDao = orgDao.getOrganizationById(org.getId());
        assertEquals(org, fromDao);

        org.setDescription("My favorite group ever");
        orgDao.updateorganization(org);
        assertNotEquals(org, fromDao);

        fromDao = orgDao.getOrganizationById(org.getId());
        assertEquals(org, fromDao);
    }

    @Test
    public void testDeleteOrg() {
       
    Organization org = new Organization();
    org.setName("The Avengers");
    org.setDescription("Best group, no doubt.");
    org.setAddress("");
    org.setContact("");
    org.setMembers(new ArrayList<Hero>());
    org = orgDao.addOrganization(org);

    Superpower power = new Superpower();
    power.setName("Super strength");
    power = superpowerDao.addSuperpower(power);

    Hero hero = new Hero();
    hero.setName("Captain America");
    hero.setDescription("America's sweetheart");
    hero.setSuperpower(power);
    hero.setOrganizations(new ArrayList<Organization>());
    hero = heroDao.addHero(hero);

    org.getMembers().add(hero);
    hero.getOrganizations().add(org);
    orgDao.updateorganization(org);
    heroDao.updateHero(hero);

    Organization fromDao = orgDao.getOrganizationById(org.getId());
    assertEquals(org, fromDao);

    orgDao.deleteOrganizationById(org.getId());
    heroDao.deleteHeroById(hero.getId()); // remove the hero from the organization
    orgDao.updateorganization(org); // update the organization in the database
    
    fromDao = orgDao.getOrganizationById(org.getId());
    assertNull(fromDao);

    List<Organization> orgs = orgDao.getOrganizationByHero(hero);
    assertEquals(0, orgs.size());
    }

    @Test
    public void testGetOrgByHero() {
    
    Organization org = new Organization();
        org.setName("The Avengers");
        org.setDescription("Best group, no doubt.");
        org.setAddress("");
        org.setContact("");
        org.setMembers(new ArrayList<Hero>());
        org = orgDao.addOrganization(org);

        Superpower power = new Superpower();
        power.setName("Super strength");
        power = superpowerDao.addSuperpower(power);

        Hero hero = new Hero();
        hero.setName("Captain America");
        hero.setDescription("America's sweetheart");
        hero.setSuperpower(power);
        hero.setOrganizations(new ArrayList<Organization>());
        hero = heroDao.addHero(hero);

        hero.getOrganizations().add(org);
        heroDao.updateHero(hero);

        org.getMembers().add(hero);
        orgDao.updateorganization(org);

        List<Organization> orgs = orgDao.getOrganizationByHero(hero);
        assertEquals(1, orgs.size());

    }
}
