/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.superherosightings.dao;

import com.pk.superherosightings.dao.HeroDaoDB.HeroMapper;
import com.pk.superherosightings.model.Hero;
import com.pk.superherosightings.model.Location;
import com.pk.superherosightings.model.Sightings;
import com.pk.superherosightings.dao.LocationDaoDB.LocationMapper;
import com.pk.superherosightings.dao.OrganizationDaoDB.OrganizationMapper;
import com.pk.superherosightings.dao.SuperpowerDaoDB.SuperpowerMapper;
import com.pk.superherosightings.model.Organization;
import com.pk.superherosightings.model.Superpower;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
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
public class SightingsDaoDB implements SightingsDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Sightings getSightingById(int id) {
        try {
            final String GET_SIGHTING_BY_ID = "SELECT * FROM sighting WHERE id = ?";
            Sightings sighting = jdbc.queryForObject(GET_SIGHTING_BY_ID, new SightingMapper(), id);
            sighting.setHero(getHeroForSighting(sighting));
            sighting.setLocation(getLocationForSighting(sighting));
            return sighting;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Sightings> getAllSightings() {
        final String GET_ALL_SIGHTINGS = "SELECT * FROM sighting";
        List<Sightings> sightings = jdbc.query(GET_ALL_SIGHTINGS, new SightingMapper());
        for (Sightings sighting : sightings) {
            sighting.setHero(getHeroForSighting(sighting));
            sighting.setLocation(getLocationForSighting(sighting));
        }
        return sightings;
    }

    @Override
    @Transactional
    public Sightings addSighting(Sightings sighting) {
        final String ADD_SIGHTING = "INSERT INTO sighting(heroId, locationId, date) VALUES (?,?,?)";
        jdbc.update(ADD_SIGHTING,
                sighting.getHero().getId(),
                sighting.getLocation().getId(),
                Timestamp.valueOf(sighting.getSightingDate().atTime(12, 0)));
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        sighting.setId(newId);
        return sighting;
    }

    @Override
    @Transactional
    public void updateSighting(Sightings sighting) {
        final String UPDATE_SIGHTING = "UPDATE sighting SET heroId = ?, locationId = ?, date = ? WHERE id = ?";
        jdbc.update(UPDATE_SIGHTING,
                sighting.getHero().getId(),
                sighting.getLocation().getId(),
                Timestamp.valueOf(sighting.getSightingDate().atTime(12, 0)),
                sighting.getId());
    }

    @Override
    @Transactional
    public void deleteSightingById(int id) {
        final String DELETE_SIGHTING = "DELETE FROM sighting WHERE id = ?";
        jdbc.update(DELETE_SIGHTING, id);
    }

    @Override
    public List<Sightings> getSightingsByDate(LocalDate date) {
        final String GET_SIGHTINGS_BY_DATE = "SELECT * FROM sighting WHERE date = ?";
        List<Sightings> sightings = jdbc.query(GET_SIGHTINGS_BY_DATE, new Object[]{java.sql.Date.valueOf(date)}, new SightingMapper());
        for (Sightings sighting : sightings) {
            sighting.setHero(getHeroForSighting(sighting));
            sighting.setLocation(getLocationForSighting(sighting));
        }
        return sightings;
    }

    @Override
    public List<Sightings> getMostRecentSightings() {
        final String GET_LAST_TEN_SIGHTINGS = "SELECT * FROM sighting ORDER BY date DESC, id DESC LIMIT 10";
        List<Sightings> sightings = jdbc.query(GET_LAST_TEN_SIGHTINGS, new SightingMapper());
        for (Sightings sighting : sightings) {
            sighting.setHero(getHeroForSighting(sighting));
            sighting.setLocation(getLocationForSighting(sighting));
        }
        return sightings;
    }

    public static final class SightingMapper implements RowMapper<Sightings> {

        @Override
        public Sightings mapRow(ResultSet rs, int i) throws SQLException {
            Sightings sighting = new Sightings();
            sighting.setId(rs.getInt("id"));
            sighting.setSightingDate(rs.getDate("date").toLocalDate());
            return sighting;
        }

    }

    private Hero getHeroForSighting(Sightings sighting) {
        final String GET_HERO_FOR_SIGHTING = "SELECT h.id, h.name, h.description, h.superpowerId "
                + "FROM sighting s "
                + "JOIN hero h ON s.heroId = h.id "
                + "WHERE s.id = ?";
        Hero hero = jdbc.queryForObject(GET_HERO_FOR_SIGHTING, new HeroMapper(), sighting.getId());
        hero.setSuperpower(getPowerForHero(hero.getId()));
        hero.setOrganizations(getOrganizationsForHero(hero.getId()));
        return hero;
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
        return jdbc.query(sql, new OrganizationMapper(), id);
    }

    private Location getLocationForSighting(Sightings sighting) {
        final String GET_LOCATION_FOR_SIGHTING = "SELECT l.id, l.name, l.description, l.addressInfo, l.latitude, l.longitude "
                + "FROM sighting s "
                + "JOIN Location l ON s.locationId = l.id "
                + "WHERE s.id = ?";
        return jdbc.queryForObject(GET_LOCATION_FOR_SIGHTING, new LocationMapper(), sighting.getId());
    }
}
