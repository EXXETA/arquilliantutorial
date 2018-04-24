package com.exxeta.arquilliantutorial;

import org.junit.rules.ExternalResource;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

class DatabaseRule extends ExternalResource {

    private final String persistenceUnitName;
    private EntityManagerFactory entityManagerFactory;

    DatabaseRule(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    @Override
    protected void before() {
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
    }

    @Override
    protected void after() {
        if(entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
