package com.pivotalservices.sample.dao;

import com.pivotalservices.sample.dao.CommonDao;
import com.pivotalservices.sample.dao.PostDAO;
import com.pivotalservices.sample.model.Post;
import com.pivotalservices.sample.model.User;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Singleton(name = "PostDAO")
@Lock(LockType.READ)
public class PostDAOImpl implements PostDAO {

    @Inject
    private CommonDao commonDao;

    @PersistenceContext(unitName = "blog")
    protected EntityManager em;

    public Post create(String title, String content, long userId) {
        User user = commonDao.find(User.class, userId);
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUser(user);
        return commonDao.create(post);
    }

    public Post find(long id) {
        return commonDao.find(Post.class, id);
    }

    @Override
    public Post findPostByTitle(String title) {
        return this.em.createNamedQuery("find.post.by.title", Post.class)
                .setParameter("title", title)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public List<Post> list(int first, int max) {
        return commonDao.namedFind(Post.class, "post.list", first, max);
    }

    public void delete(long id) {
        commonDao.delete(Post.class, id);
    }

    public Post update(long id, long userId, String title, String content) {
        User user = commonDao.find(User.class, userId);
        if (user == null) {
            throw new IllegalArgumentException("user id " + id + " not found");
        }

        Post post = commonDao.find(Post.class, id);
        if (post == null) {
            throw new IllegalArgumentException("post id " + id + " not found");
        }

        post.setTitle(title);
        post.setContent(content);
        post.setUser(user);
        return commonDao.update(post);
    }
}
