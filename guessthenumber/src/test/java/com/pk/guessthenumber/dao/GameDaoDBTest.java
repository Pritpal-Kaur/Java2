/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pk.guessthenumber.dao;

import com.pk.guessthenumber.TestApplicationConfiguration;
import com.pk.guessthenumber.model.Game;
import com.pk.guessthenumber.model.Round;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author Pritpal
 */


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class GameDaoDBTest {
   
    @Autowired
    GameDao gameDao;
    
    @Autowired
    RoundDao roundDao;
    
    private Game game;
    private Round round;
    private Game testGame;
    
   
    public GameDaoDBTest(GameDao _gameDao) {
        this.gameDao = _gameDao;
    }
    
    @BeforeEach
    public void setUp() {
        
       
    }
    
    @AfterEach
    public void tearDown() {
    }


    @Test
    public void testAddGetGame() {
         
        Game game = new Game();
        game.setAnswer("1234");
        game.setFinished(true);
        game = gameDao.addGame(game);
        
        Game fromDao = gameDao.getGameById(game.getGameId());
        
        assertEquals(game, fromDao);
    }
     
}


