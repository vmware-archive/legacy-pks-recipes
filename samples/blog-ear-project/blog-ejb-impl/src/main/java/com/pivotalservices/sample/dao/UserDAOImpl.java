package com.pivotalservices.sample.dao;

import com.pivotalservices.sample.dao.CommonDao;
import com.pivotalservices.sample.dao.UserDAO;
import com.pivotalservices.sample.model.User;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.List;

@Singleton(name="UserDAO")
@Lock(LockType.READ)
public class UserDAOImpl implements UserDAO {

    @Inject
    private CommonDao commonDao;

    public User create(String name, String pwd, String mail) {
        User user = new User();
        user.setFullname(name);
        user.setPassword(pwd);
        user.setEmail(mail);
        return commonDao.create(user);
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
