/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.superherosightings.dao;

import com.pk.superherosightings.dao.OrganizationDaoDB.OrganizationMapper;
import com.pk.superherosightings.dao.SuperpowerDaoDB.SuperpowerMapper;
import com.pk.superherosightings.model.Hero;
import com.pk.superherosightings.model.Location;
import com.pk.superherosightings.model.Superpower;
import com.pk.superherosightings.model.Organization;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Pritpal
 */

@Repository
public class HeroDaoDB implements HeroDao {
    
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Hero getHeroById(int id) {

      try {
            final String GET_HERO_BY_ID = "SELECT * FROM hero WHERE id = ?";
            Hero hero = jdbc.queryForObject(GET_HERO_BY_ID, new HeroMapper(), id);
            hero.setSuperpower(getPowerForHero(id));
            hero.setOrganizations(getOrganizationsForHero(id));
            return hero;
        } catch (DataAccessException ex) {
            return null;
        }  
    }

    @Override
    public List<Hero> getAllHeroes() {
final String GET_ALL_HEROES = "SELECT * FROM hero";
        List<Hero> heroes = jdbc.query(GET_ALL_HEROES, new HeroMapper());
        for (Hero hero : heroes) {
            hero.setSuperpower(getPowerForHero(hero.getId()));
            hero.setOrganizations(getOrganizationsForHero(hero.getId()));
        }
        return heroes;
    }

    @Override
    @Transactional
    public Hero addHero(Hero hero) {
final String ADD_HERO = "INSERT INTO hero(name, description, superpowerId) VALUES(?,?,?)";
        if (hero.getSuperpower() != null) {
            jdbc.update(ADD_HERO,
                    hero.getName(),
                    hero.getDescription(),
                    hero.getSuperpower().getId());
        } else {
            jdbc.update(ADD_HERO,
                    hero.getName(),
                    hero.getDescription(),
                    null);
        }

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        hero.setId(newId);
        insertOrganizationMember(hero);
        return hero;
    }

    @Override
    @Transactional
    public void updateHero(Hero hero) {
 final String UPDATE_HERO = "UPDATE hero SET name = ?, description = ?, superpowerId = ? WHERE id = ?";
        if (hero.getSuperpower() != null) {
            jdbc.update(UPDATE_HERO,
                    hero.getName(),
                    hero.getDescription(),
                    hero.getSuperpower().getId(),
                    hero.getId());
        } else {
            jdbc.update(UPDATE_HERO,
                    hero.getName(),
                    hero.getDescription(),
                    null,
                    hero.getId());
        }
        final String DELETE_ORGANIZATION_MEMBER = "DELETE FROM OrganizationMember WHERE heroId = ?";
        jdbc.update(DELETE_ORGANIZATION_MEMBER, hero.getId());
        insertOrganizationMember(hero);
    }

    @Override
    @Transactional
    public void deleteHeroById(int id) {
final String DELETE_SIGHTINGS = "DELETE FROM sighting WHERE heroId = ?";
        jdbc.update(DELETE_SIGHTINGS, id);

        final String DELETE_ORGANIZATION_MEMBER = "DELETE FROM OrganizationMember WHERE heroId = ?";
        jdbc.update(DELETE_ORGANIZATION_MEMBER, id);

        final String DELETE_SUPER = "DELETE FROM hero WHERE id = ?";
        jdbc.update(DELETE_SUPER, id);
    }

    @Override
    public List<Hero> getHeroesByLocation(Location location) {
final String GET_HEROES_BY_LOCATION = "SELECT DISTINCT h.id, h.name, h.description, h.superpowerId "
                + "FROM sighting s "
                + "JOIN hero h ON s.heroId = h.id "
                + "WHERE s.locationId = ?";
        List<Hero> heroes = jdbc.query(GET_HEROES_BY_LOCATION, new HeroMapper(), location.getId());
        for (Hero hero : heroes) {
            hero.setSuperpower(getPowerForHero(hero.getId()));
            hero.setOrganizations(getOrganizationsForHero(hero.getId()));
        }
        return heroes;
    }

    @Override
    public List<Hero> getHeroesByOrganization(Organization organization) {
final String GET_HEROES_BY_ORG = "SELECT h.id, h.name, h.description, h.superpowerId "
                + "FROM OrganizationMember om"
                + "JOIN hero h ON om.heroId = h.id "
                + "WHERE om.organizationId = ?";
        List<Hero> heroes = jdbc.query(GET_HEROES_BY_ORG, new HeroMapper(), organization.getId());
        for (Hero hero : heroes) {
            hero.setSuperpower(getPowerForHero(hero.getId()));
            hero.setOrganizations(getOrganizationsForHero(hero.getId()));
        }
        return heroes;
    }
    
    private Superpower getPowerForHero(int id) {
        try {
            final String sql = "SELECT sp.id, sp.name FROM superpower sp "
                    + "JOIN hero h ON sp.id = h.superpowerId WHERE h.id = ?";
            return jdbc.queryForObject(sql, new SuperpowerMapper(), id);
        } catch (DataAccessException ex) {
            return null;
        }
    }
    
    private List<Organization> getOrganizationsForHero(int id) {
        final String sql = "SELECT o.id, o.name, o.description, o.address, o.contact "
                + "FROM OrganizationMember om "
                + "JOIN Organization o ON om.organizationId = o.id "
                + "WHERE om.heroId = ?";
        List<Organization> organizations = jdbc.query(sql, new OrganizationMapper(), id);
        for (Organization organization : organizations) {
            organization.setMembers(getHeroesForOrganization(organization));
        }
        return organizations;
    }
    
    private List<Hero> getHeroesForOrganization(Organization organization) {
        final String GET_Heroes = "SELECT h.id, h.name, h.description, h.powerId "
                + "FROM OrganizationMember om "
                + "JOIN hero h ON om.heroId = h.id "
                + "WHERE om.organizationId = ?";
        List<Hero> heroes = jdbc.query(GET_Heroes, new HeroMapper(), organization.getId());
        for (Hero hero : heroes) {
            hero.setSuperpower(getPowerForHero(hero.getId()));
        }
        return heroes;
    }
    
     private void insertOrganizationMember(Hero hero) {
        final String INSERT_ORGANIZATION_MEMBER = "INSERT INTO OrganizationMember VALUES(?,?)";
        for (Organization organization : hero.getOrganizations()) {
            jdbc.update(INSERT_ORGANIZATION_MEMBER, organization.getId(), hero.getId());
        }
    }
    
    public final static class HeroMapper implements RowMapper<Hero> {

        @Override
        public Hero mapRow(ResultSet rs, int num) throws SQLException {
            Hero hero = new Hero();
            hero.setId(rs.getInt("id"));
            hero.setName(rs.getString("name"));
            hero.setDescription(rs.getString("description"));
            return hero;
        }

    }
}
