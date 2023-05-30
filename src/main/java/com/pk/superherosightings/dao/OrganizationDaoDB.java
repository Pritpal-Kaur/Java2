/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.superherosightings.dao;

import com.pk.superherosightings.model.Hero;
import com.pk.superherosightings.model.Organization;
import com.pk.superherosightings.dao.HeroDaoDB.HeroMapper;
import com.pk.superherosightings.dao.SuperpowerDaoDB.SuperpowerMapper;
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
public class OrganizationDaoDB implements OrganizationDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Organization getOrganizationById(int id) {
        try {
            final String GET_ORG_BY_ID = "SELECT * FROM Organization WHERE id = ?";
            Organization organization = jdbc.queryForObject(GET_ORG_BY_ID, new OrganizationMapper(), id);
            organization.setMembers(getHeroesForOrganization(organization.getId()));
            return organization;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Organization> getAllOrganizations() {
        final String GET_ALL_ORGS = "SELECT * FROM Organization";
        List<Organization> organizations = jdbc.query(GET_ALL_ORGS, new OrganizationMapper());
        for (Organization org : organizations) {
            org.setMembers(getHeroesForOrganization(org.getId()));
        }
        return organizations;
    }

    @Override
    @Transactional
    public Organization addOrganization(Organization organization) {
        final String ADD_ORG = "INSERT INTO Organization(name, description, address, contact) VALUES(?,?,?,?)";
        jdbc.update(ADD_ORG,
                organization.getName(),
                organization.getDescription(),
                organization.getAddress(),
                organization.getContact());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        organization.setId(newId);
        insertOrganizationMembers(organization);
        return organization;
    }

    @Override
    @Transactional
    public void updateorganization(Organization organization) {
        final String UPDATE_ORG = "UPDATE Organization SET name = ?, description = ?, "
                + "address = ?, contact = ? WHERE id = ?";
        jdbc.update(UPDATE_ORG,
                organization.getName(),
                organization.getDescription(),
                organization.getAddress(),
                organization.getContact(),
                organization.getId());

        final String DELETE_ORG_MEMBERS = "DELETE FROM OrganizationMember WHERE organizationId = ?";
        jdbc.update(DELETE_ORG_MEMBERS, organization.getId());
        if(organization.getMembers() != null) {
        insertOrganizationMembers(organization);
        }
    }

    @Override
    @Transactional
    public void deleteOrganizationById(int id) {
        final String DELETE_ORG_MEMBERS = "DELETE FROM OrganizationMember WHERE organizationId = ?";
        jdbc.update(DELETE_ORG_MEMBERS, id);

        final String DELETE_ORG = "DELETE FROM Organization WHERE id = ?";
        jdbc.update(DELETE_ORG, id);
    }

    @Override
    public List<Organization> getOrganizationByHero(Hero hero) {
        final String GET_ORG_BY_HERO = "SELECT o.* From organization o "
                + "JOIN OrganizationMember  om ON om.organizationId = o.id WHERE om.heroId = ?";
        List<Organization> orgs = jdbc.query(GET_ORG_BY_HERO, new OrganizationMapper(), hero.getId());
        for (Organization org : orgs) {
            org.setMembers(getHeroesForOrganization(org.getId()));
        }
        return orgs;
    }

    public List<Hero> getHeroesForOrganization(int id) {
        final String GET_HEROES_FOR_ORGANIZATION = "SELECT * FROM hero h "
                + "JOIN OrganizationMember om ON h.id = om.heroId WHERE om.organizationId = ?";
        List<Hero> heroes = jdbc.query(GET_HEROES_FOR_ORGANIZATION, new HeroMapper(), id);
        for (Hero hero : heroes) {
            hero.setSuperpower(getPowerForHero(hero.getId()));
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

    private void insertOrganizationMembers(Organization organization) {
        final String INSERT_ORG_MEMBERS = "INSERT INTO OrganizationMember(organizationId, heroId) VALUES(?,?)";
        for (Hero hero : organization.getMembers()) {
            jdbc.update(INSERT_ORG_MEMBERS, organization.getId(), hero.getId());
        }
    }

    public static final class OrganizationMapper implements RowMapper<Organization> {

        @Override
        public Organization mapRow(ResultSet rs, int i) throws SQLException {
            Organization organization = new Organization();
            organization.setId(rs.getInt("id"));
            organization.setName(rs.getString("name"));
            organization.setDescription(rs.getString("description"));
            organization.setAddress(rs.getString("address"));
            organization.setContact(rs.getString("contact"));
            return organization;
        }
    }
}
