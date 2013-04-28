/**
 * 
 */
package org.adrianchia.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

/**
 * Custom RestEasy Jackson Jaxb Provider class
 * 
 * @author Adrian Chia
 *
 */
@Provider
@Consumes({"application/*+json", "text/json"})
@Produces({"application/*+json", "text/json"})
public class ResteasyJackson2JaxbProvider extends JacksonJaxbJsonProvider {

}
