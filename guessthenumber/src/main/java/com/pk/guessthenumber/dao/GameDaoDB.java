/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.guessthenumber.dao;

import com.pk.guessthenumber.model.Game;
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
public class GameDaoDB implements GameDao {
    
    private final JdbcTemplate jdbcTemplate;
    
    @Autowired
            public GameDaoDB(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
            }
    
   @Override
   public List<Game> getAllGames() {
     final String SELECT_ALL_GAMES = "SELECT * FROM game";
     return jdbcTemplate.query(SELECT_ALL_GAMES, new GameMapper());
   }

    @Override
    public Game getGameById(int gameId) {
    try {
        final String SELECT_GAME_BY_ID = "SELECT * FROM game WHERE game_id = ?";
        return jdbcTemplate.queryForObject(SELECT_GAME_BY_ID, new GameMapper(), gameId);
    } catch(DataAccessException ex) {
        return null;
    }
    }

    @Override
    @Transactional
    public Game addGame(Game game) {
    final String INSERT_GAME = "INSERT INTO game(answer) VALUES(?)";
    jdbcTemplate.update(INSERT_GAME, game.getAnswer());
    
    int newGameId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    game.setGameId(newGameId);
    return game;
    }

    @Override
    public void updateGame(Game game) {
    final String UPDATE_GAME = "UPDATE game SET finished = ? WHERE game_id = ?";
    jdbcTemplate.update(UPDATE_GAME, game.isFinished(), game.getGameId());
    }
    
    public static final class GameMapper implements RowMapper<Game> {
        
       @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game game = new Game();
            game.setGameId(rs.getInt("game_id"));
            game.setAnswer(rs.getString("answer"));
            game.setFinished(rs.getBoolean("finished"));
            return game;
        }
    }
    
}
