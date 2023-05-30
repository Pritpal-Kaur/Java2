/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.superherosightings.service;
import com.pk.superherosightings.dao.SuperpowerDao;
import com.pk.superherosightings.model.Hero;
import com.pk.superherosightings.model.Superpower;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pritpal
 */
@Service
public class SuperpowerServiceLayerImpl implements SuperpowerServiceLayer {
    
    @Autowired
    SuperpowerDao superpowerDao;
    
    @Override
    public Superpower getSuperpowerById(int id) {

        return superpowerDao.getSuperpowerById(id);
    }

    @Override
    public List<Superpower> getAllSuperpowers() {

        return superpowerDao.getAllSuperpowers();
    }

    @Override
   public Superpower addSuperpower(Superpower superpower) {
   
       return superpowerDao.addSuperpower(superpower);
   }

    @Override
    public void updateSuperpower(Superpower superpower) {

        superpowerDao.updateSuperpower(superpower);
    }

    @Override
    public void deleteSuperpowerById(int id) {

        superpowerDao.deleteSuperpowerById(id);
    }

    @Override
    public Superpower getSuperpowerByHero(Hero hero) {

        return superpowerDao.getPowerByHero(hero);
    }
}
