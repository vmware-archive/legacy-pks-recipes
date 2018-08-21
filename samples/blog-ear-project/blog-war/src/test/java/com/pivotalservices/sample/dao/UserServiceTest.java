//package com.pivotalservices.sample.dao;
//
//import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
//import org.apache.tomee.embedded.EmbeddedTomEEContainer;
//import org.apache.ziplock.Archive;
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import com.pivotalservices.sample.model.User;
//
//import javax.ejb.embeddable.EJBContainer;
//import javax.naming.NamingException;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import java.io.File;
//import java.io.IOException;
//import java.util.Properties;
//
//import static org.apache.openejb.loader.JarLocation.jarLocation;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//public class UserServiceTest {
//
//    private static EJBContainer container;
//
//    @BeforeClass
//    public static void start() throws IOException {
//        final File webApp = Archive.archive().copyTo("WEB-INF/classes", jarLocation(UserDAOImpl.class)).asDir();
//        final Properties p = new Properties();
//        p.setProperty(EJBContainer.APP_NAME, "blog-war");
//        p.setProperty(EJBContainer.PROVIDER, "tomee-embedded"); // need web feature
//        p.setProperty(EJBContainer.MODULES, webApp.getAbsolutePath());
//        p.setProperty(EmbeddedTomEEContainer.TOMEE_EJBCONTAINER_HTTP_PORT, "-1"); // random port
//        container = EJBContainer.createEJBContainer(p);
//    }
//
//    @AfterClass
//    public static void stop() {
//        if (container != null) {
//            container.close();
//        }
//    }
//
//    @Test
//    public void create() throws NamingException {
//        final UserDAO dao = (UserDAO) container.getContext().lookup("java:global/blog-war/UserDAO");
//        final User user = dao.create("foo", "dummy", "foo@dummy.org");
//        assertNotNull(dao.find(user.getId()));
//
//        final String uri = "http://127.0.0.1:" + System.getProperty(EmbeddedTomEEContainer.TOMEE_EJBCONTAINER_HTTP_PORT) + "/blog-war";
//        final UserServiceClientAPI client = JAXRSClientFactory.create(uri, UserServiceClientAPI.class);
//        final User retrievedUser = client.show(user.getId());
//        assertNotNull(retrievedUser);
//        assertEquals("foo", retrievedUser.getFullname());
//        assertEquals("dummy", retrievedUser.getPassword());
//        assertEquals("foo@dummy.org", retrievedUser.getEmail());
//    }
//
//    /**
//     * a simple copy of the unique method i want to use from my service.
//     * It allows to use cxf proxy to call remotely our rest service.
//     * Any other way to do it is good.
//     */
//    @Path("/api/user")
//    @Produces({"text/xml", "application/json"})
//    public static interface UserServiceClientAPI {
//
//        @Path("/show/{id}")
//        @GET
//        User show(@PathParam("id") long id);
//    }
//}
