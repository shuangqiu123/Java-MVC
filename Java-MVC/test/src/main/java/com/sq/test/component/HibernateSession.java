package com.sq.test.component;

import com.sq.annotation.Component;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.annotation.PostConstruct;

@Component
public class HibernateSession {
    private SessionFactory sessionFactory = null;

    @PostConstruct
    public void init() {
        Configuration configuration = new Configuration();
        configuration.configure();
        this.sessionFactory = configuration.buildSessionFactory();
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

    public void closeSession(Session session) {
        session.close();
    }
}
