package com.pivotalservices.sample.batcher;

import com.pivotalservices.sample.dao.CommentDAO;
import com.pivotalservices.sample.dao.PostDAO;
import com.pivotalservices.sample.dao.UserDAO;
import com.pivotalservices.sample.model.Post;
import com.pivotalservices.sample.model.User;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;

@Startup
@DependsOn({"CommentDAO", "PostDAO", "UserDAO"})
@Singleton
@Lock(LockType.READ)
public class SampleDataManagerImpl implements SampleDataManager {

    private static final Logger LOGGER = Logger.getLogger(SampleDataManager.class.getName());

    @PersistenceContext(unitName = "blog")
    private EntityManager em;

    @Inject
    private CommentDAO comments;

    @Inject
    private PostDAO posts;

    @Inject
    private UserDAO users;

    @PostConstruct
    public void createSomeData() {
        User tomee = createUserIfNotPresent("tomee", "tomee", "tomee@apache.org");
        User liberty = createUserIfNotPresent("liberty", "liberty", "liberty@liberty.org");
        createPostAndCommentIfNotPresent("TomEE", "TomEE is a cool JEE App Server", tomee);
        createPostAndCommentIfNotPresent("Liberty", "Liberty is a cool JEE application server", liberty);
        createPostAndCommentIfNotPresent("Intro to Liberty", "WebSphere Liberty is a fast, dynamic, and easy-to-use Java application server, built on the open source Open Liberty project", liberty);
        createPostAndCommentIfNotPresent("Liberty JEE Profiles", "The latest stable release of WebSphere Liberty supports Java EE 8 Full Platform in both development and production. Liberty also continues to support Java EE 6 Web Profile, Java EE 7 Full Profile, and Java EE 7 Web Profile.", liberty);
    }

    private Post createPostAndCommentIfNotPresent(String title, String content, User user) {
        Post post = this.posts.findPostByTitle(title);
        if (post != null) {
            return post;
        }
        Post newPost =  posts.create(title, content, user.getId());
        comments.create("visitor", "nice post on '" + title + "'", newPost.getId());

        return newPost;
    }


    private User createUserIfNotPresent(String name, String pwd, String email) {
        User user = this.users.findByEmail(email);
        
        if (user != null) {
            return user;
        }
        
        return users.create(name, pwd, email);
    }

//    @Schedule(second = "0", minute = "30", hour = "*", persistent = false)
//    private void cleanData() {
//        LOGGER.info("Cleaning data");
//        deleteAll();
//        createSomeData();
//        LOGGER.info("Data reset");
//    }
//
//    private void deleteAll() {
//        em.createQuery("delete From Comment").executeUpdate();
//        em.createQuery("delete From Post").executeUpdate();
//        em.createQuery("delete From User").executeUpdate();
//    }
}
