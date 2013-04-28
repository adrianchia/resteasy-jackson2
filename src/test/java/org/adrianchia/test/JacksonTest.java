/**
 * 
 */
package org.adrianchia.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import org.adrianchia.model.SimpleModel;
import org.adrianchia.util.Resources;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.util.GenericType;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Adrian Chia
 *
 */
@RunWith(Arquillian.class)
public class JacksonTest {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() throws FileNotFoundException, IOException {
        Properties p = new Properties();
        p.load(ClassLoader.getSystemResourceAsStream("test.properties"));
        File[] libs = Maven.resolver()
                .loadPomFromFile("pom.xml")
                .resolve("com.fasterxml.jackson.jaxrs:jackson-jaxrs-json-provider:"+p.get("jackson.version"))
                .withTransitivity()
                .asFile();
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addAsLibraries(libs)
                .addClasses(SimpleModel.class, Resources.class)
                .addPackage("org.adrianchia.rest")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("META-INF/services/javax.ws.rs.ext.Providers")
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("import.sql")
                .addAsWebInfResource("jboss-deployment-structure.xml");
    }
    
    @ArquillianResource
    private URL url;
    
    @Test @RunAsClient @InSequence(1)
    public void testFindOne() throws Exception {
        ClientRequest req = new ClientRequest(url.toString() + "rest/simple/1");
        ClientResponse<SimpleModel> resp = req.accept(MediaType.APPLICATION_JSON).get(SimpleModel.class);
        SimpleModel result = resp.getEntity();
        assertEquals("Random Model 1", result.getName());
    }
    
    @Test @RunAsClient @InSequence(2)
    public void testFindAll() throws Exception {
        ClientRequest req = new ClientRequest(url.toString() + "rest/simple");
        ClientResponse<List<SimpleModel>> resp = req.accept(MediaType.APPLICATION_JSON).get(new GenericType<List<SimpleModel>>(){});
        List<SimpleModel> result = resp.getEntity();
        assertEquals(2, result.size());
    }
    
    @Test @RunAsClient @InSequence(3)
    public void testCreate() throws Exception {
        SimpleModel newSimpleModel = new SimpleModel(null, "Random Model 3");
        ClientRequest req = new ClientRequest(url.toString() + "rest/simple");
        req.accept(MediaType.APPLICATION_JSON).body(MediaType.APPLICATION_JSON, newSimpleModel);
        
        ClientResponse<SimpleModel> resp = req.post(SimpleModel.class);
        SimpleModel result = resp.getEntity();
        assertEquals("Random Model 3", result.getName());
        assertEquals(3, result.getId().intValue());
    }
}
