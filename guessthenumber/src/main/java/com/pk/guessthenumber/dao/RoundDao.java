/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.guessthenumber.dao;

import com.pk.guessthenumber.model.Round;
import java.util.List;

/**
 *
 * @author Pritpal
 */
public interface RoundDao {
    List<Round> getAllRoundsByGameId(int gameId);
    Round getRoundById(int roundId);
    Round addRound(Round round);
    
}
