package com.pivotalservices.sample.dao;

import com.pivotalservices.sample.dao.CommonDao;
import com.pivotalservices.sample.dao.UserDAO;
import com.pivotalservices.sample.model.User;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Singleton(name="UserDAO")
@Lock(LockType.READ)
public class UserDAOImpl implements UserDAO {

    @Inject
    private CommonDao commonDao;

    @PersistenceContext(unitName = "blog")
    protected EntityManager em;

    public User create(String name, String pwd, String mail) {
        User user = new User();
        user.setFullname(name);
        user.setPassword(pwd);
        user.setEmail(mail);
        return commonDao.create(user);
    }

    @Override
    public User findByEmail(String mail) {
        return this.em.createNamedQuery("user.find.by.email", User.class)
                .setParameter("email", mail)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public List<User> list(int first, int max) {
        return commonDao.namedFind(User.class, "user.list", first, max);
    }

    public User find(long id) {
        return commonDao.find(User.class, id);
    }

    public void delete(long id) {
        commonDao.delete(User.class, id);
    }

    public User update(long id, String name, String pwd, String mail) {
        User user = commonDao.find(User.class, id);
        if (user == null) {
            throw new IllegalArgumentException("setUser id " + id + " not found");
        }

        user.setFullname(name);
        user.setPassword(pwd);
        user.setEmail(mail);
        return commonDao.update(user);
    }
}
