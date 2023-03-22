/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.guessthenumber.service;

import com.pk.guessthenumber.dao.GameDao;
import com.pk.guessthenumber.dao.RoundDao;
import com.pk.guessthenumber.model.Game;
import com.pk.guessthenumber.model.Round;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pritpal
 */

@Service
public class ServiceLayer {
    
    @Autowired
    GameDao gameDao;
    
    @Autowired
    RoundDao roundDao;
    
    public List<Game> getAllGames() {
        List<Game> games = gameDao.getAllGames();
        for(Game game : games) {
            if(!game.isFinished()) {
                game.setAnswer("****");
            }
        }
        return games;
    }
    
    public List<Round> getAllRoundsByGameId(int gameId) {
        return roundDao.getAllRoundsByGameId(gameId);
    }
    
    public Round makeGuess(Round round) {
        String answer = gameDao.getGameById(round.getGameId()).getAnswer();
        String guess = round.getGuess();
        String result = determineResult(guess, answer);
        round.setResult(result);
        
        if(guess.equals(answer)) {
            Game game = getGameById(round.getGameId());
            game.setFinished(true);
            gameDao.updateGame(game);
        }
        return roundDao.addRound(round);
    }
    
    public Game getGameById(int gameId) {
        Game game = gameDao.getGameById(gameId);
        if(!game.isFinished()) {
            game.setAnswer("****");
        }
        return game;
    }
    
    public Game addGame(Game game) {
        return gameDao.addGame(game);
    }
    
    public int newGame() {
        Game game = new Game();
        game.setAnswer(generateAnswer());
        game = gameDao.addGame(game);
        return game.getGameId();
    }
    
    private String generateAnswer() {
        Random rd = new Random();
        int a1 = rd.nextInt(10);
        int a2 = rd.nextInt(10);
        while(a1==a2) {
            a2 = rd.nextInt(10);
        }
        int a3 = rd.nextInt(10);
        while(a3==a2 || a3==a1) {
            a3 = rd.nextInt(10);
        }
        
        int a4 = rd.nextInt(10);
        while(a4==a3 || a4==a2 || a4==a1) {
            a4 = rd.nextInt(10);
        }
        
        String answer = String.valueOf(a1) + String.valueOf(a2) + String.valueOf(a3) + String.valueOf(a4);
        
        return answer;
    }
    
    
    public String determineResult(String guess, String answer) {
        char[] guessChars = guess.toCharArray();
        char[] answerChars = answer.toCharArray();
        
        int exact = 0;
        int partial = 0;
        
        for(int i =0; i<guessChars.length; i++) {
            if(answer.indexOf(guessChars[i]) > -1) {
                if(guessChars[i] == answerChars[i]) {
                    exact++;
                } else {
                    partial++;
                }
            }
        }
        String result = "e:" + exact + ":p:" +partial;
        return result;
    }
}
