package com.exxeta.arquilliantutorial;

import javax.faces.bean.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class CarDao {

    @PersistenceContext(unitName = "MYSQL")
    private EntityManager entityManager;

    public CarEntity getById(long id){
        return entityManager.find(CarEntity.class,id);
    }
}
