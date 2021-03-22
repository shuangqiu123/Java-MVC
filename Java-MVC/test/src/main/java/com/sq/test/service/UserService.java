package com.sq.test.service;

import com.sq.annotation.Autowired;
import com.sq.annotation.Service;
import com.sq.test.component.HibernateSession;
import com.sq.test.domain.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Service
public class UserService {
    
    @Autowired
    private HibernateSession hibernateSession;
    
    public void helloWorld() {
        System.out.println("hello world!");
    }

    public User registerUser(String username) {
        User user = new User();
        user.setUsername(username);

        Session session = hibernateSession.getSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        session.save(user);
        transaction.commit();
        hibernateSession.closeSession(session);
        return user;
    }

    public User getUserById(int id) {

        Session session = hibernateSession.getSession();

        User user = session.get(User.class, id);

        hibernateSession.closeSession(session);

        return user;
    }
}
