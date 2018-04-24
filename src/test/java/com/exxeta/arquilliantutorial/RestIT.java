package com.exxeta.arquilliantutorial;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

import static org.junit.Assert.assertEquals;


@RunAsClient
@RunWith(Arquillian.class)
public class RestIT {

    // Database access from inside test
    @ClassRule
    public static DatabaseRule databaseRule = new DatabaseRule("MYSQL");

    // Container description to be deployed by arquillian
    @Deployment(name = "arquillian-tutorial")
    public static WebArchive createDeployment() throws URISyntaxException {

        return ShrinkWrap.create(WebArchive.class, "arquillian-tutorial.war")

                .addPackages(true, Filters.exclude(".*(Test|IT).class"),
                        "com.exxeta.arquilliantutorial")

                .addAsResource(getResourceFile("META-INF/persistence.xml"),
                        "META-INF/persistence.xml")

                .addAsResource(getResourceFile("META-INF/beans.xml"),
                        "META-INF/beans.xml");
    }

    @Test
    @InSequence(1)
    public void testGetInfo(@ArquillianResource @OperateOnDeployment("arquillian-tutorial") URL url){

        // Request /info from service
        Client httpClient = ClientBuilder.newClient();
        String uri = url.toString() + "info";

        String result = httpClient.target(uri)
                .request()
                .get()
                .readEntity(String.class);

        // Check result
        assertEquals("Hello World",result);
    }


    @Test
    @InSequence(2)
    public void testGetCar(@ArquillianResource @OperateOnDeployment("arquillian-tutorial") URL url){

        String carName = "Mercedes";

        // Create car in database
        EntityManager em = databaseRule.getEntityManager();
        em.getTransaction().begin();
        CarEntity car = new CarEntity();
        car.setName(carName);
        em.persist(car);
        em.getTransaction().commit();

        int carId = car.getId().intValue();

        // Request car from service
        Client httpClient = ClientBuilder.newClient();

        String uri = url.toString() + "car/" + carId;
        String resultName = httpClient.target(uri)
                .request()
                .get()
                .readEntity(String.class);

        // Check result
        assertEquals(carName,resultName);

    }


    @Test
    @InSequence(3)
    public void testGetNotExistingCar(@ArquillianResource @OperateOnDeployment("arquillian-tutorial") URL url) {

        // Request not existing car from service
        Client httpClient = ClientBuilder.newClient();
        String uri = url.toString() + "car/0";
        int status = httpClient.target(uri)
                .request()
                .get()
                .getStatus();

        // Check result
        assertEquals(404, status);

    }


    private static File getResourceFile(String resourceFile) throws URISyntaxException {
        URL resourceUrl = RestIT.class.getClassLoader().getResource(resourceFile);
        return new File(Objects.requireNonNull(resourceUrl).toURI());
    }

}
