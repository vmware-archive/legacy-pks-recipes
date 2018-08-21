package com.pivotalservices.sample.dao;

import com.pivotalservices.sample.model.User;

import java.util.List;

public interface UserDAO {


    User create(String name, String pwd, String mail);

    List<User> list(int first, int max);

    User find(long id);

    void delete(long id);

    User update(long id, String name, String pwd, String mail);
}
