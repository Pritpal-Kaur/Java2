/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.guessthenumber.dao;

import com.pk.guessthenumber.model.Round;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
public class RoundDaoDB implements RoundDao {
    
    private final JdbcTemplate jdbcTemplate;
    
    @Autowired
            public RoundDaoDB(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
            }

    @Override
    public List<Round> getAllRoundsByGameId(int gameId) {
    try {
        final String SELECT_ROUNDS_BY_GAMEID = "SELECT * FROM round "
                + "WHERE game_id = ? ORDER BY guess_time";
        
        List<Round> rounds = jdbcTemplate.query(SELECT_ROUNDS_BY_GAMEID, new RoundMapper(), gameId);
        return rounds;
    }    catch(DataAccessException ex) {
        return null;
    }
    }

    @Override
    public Round getRoundById(int roundId) {
    try {
        final String SELECT_ROUND_BY_ID = "SELECT * FROM round WHERE round_id = ?";
        return jdbcTemplate.queryForObject(SELECT_ROUND_BY_ID, new RoundMapper(), roundId);
    }    catch(DataAccessException ex) {
        return null;
    }
    }

    @Override
    @Transactional
    public Round addRound(Round round) {
     final String INSERT_ROUND = "INSERT INTO round(game_id, guess, result) VALUES(?,?,?)";
        jdbcTemplate.update(INSERT_ROUND, round.getGameId(), round.getGuess(), round.getResult());
        
        int newRoundId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        round.setRoundId(newRoundId);
        return getRoundById(newRoundId);
    }
    
    public static final class RoundMapper implements RowMapper<Round> {
        
        
        @Override
        public Round mapRow(ResultSet rs, int index) throws SQLException {
           Round round = new Round();
           round.setRoundId(rs.getInt("round_id"));
           round.setGameId(rs.getInt("game_id"));
           round.setGuess(rs.getString("guess"));
           Timestamp timestamp = rs.getTimestamp("guess_time");
           round.setGuessTime(timestamp.toLocalDateTime());
           round.setResult(rs.getString("result"));
           return round;
        }
     }
}
