/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.superherosightings.dao;

import com.pk.superherosightings.model.Hero;
import com.pk.superherosightings.model.Superpower;
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
public class SuperpowerDaoDB implements SuperpowerDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Superpower getSuperpowerById(int id) {
        try {
            final String sql = "SELECT * FROM superpower WHERE id = ?";
            return jdbc.queryForObject(sql, new SuperpowerMapper(), id);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Superpower> getAllSuperpowers() {
        final String sql = "SELECT * FROM superpower";
        return jdbc.query(sql, new SuperpowerMapper());
    }

    @Override
    @Transactional
    public Superpower addSuperpower(Superpower superpower) {
        final String sql = "INSERT INTO superpower(name) VALUES (?)";
        jdbc.update(sql, superpower.getName());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        superpower.setId(newId);
        return superpower;
    }

    @Override
    @Transactional
    public void updateSuperpower(Superpower superpower) {
        final String sql = "UPDATE superpower SET name = ? WHERE id = ?";
        jdbc.update(sql, superpower.getName(), superpower.getId());
    }

    @Override
    @Transactional
    public void deleteSuperpowerById(int id) {
        final String UPDATE_HEROSUPERPOWERS = "UPDATE hero SET superpowerId = null WHERE superpowerId = ?";
        jdbc.update(UPDATE_HEROSUPERPOWERS, id);

        final String DELETE_SUPERPOWER = "DELETE FROM superpower WHERE id = ?";
        jdbc.update(DELETE_SUPERPOWER, id);
    }

    @Override
    public Superpower getPowerByHero(Hero hero) {
        try {
            final String sql = "SELECT * FROM superpower WHERE id = ?";
            return jdbc.queryForObject(sql, new SuperpowerMapper(), hero.getSuperpower().getId());
        } catch (DataAccessException ex) {
            return null;
        }
    }

    public static final class SuperpowerMapper implements RowMapper<Superpower> {

        @Override
        public Superpower mapRow(ResultSet rs, int num) throws SQLException {
            Superpower power = new Superpower();
            power.setId(rs.getInt("id"));
            power.setName(rs.getString("name"));
            return power;
        }

    }
}
