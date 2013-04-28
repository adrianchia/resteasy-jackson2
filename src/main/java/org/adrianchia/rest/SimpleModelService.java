/**
 * 
 */
package org.adrianchia.rest;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.adrianchia.model.SimpleModel;

/**
 * @author Adrian Chia
 *
 */
@Path("/simple")
@Stateless
public class SimpleModelService {

    @Inject
    private EntityManager em;
    
    @Inject
    private Logger log;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id:[0-9][0-9]*}")
    public SimpleModel findOne(@PathParam("id") final int id) {
        return em.find(SimpleModel.class, id);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SimpleModel> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SimpleModel> criteria = cb.createQuery(SimpleModel.class);
        return em.createQuery(criteria.select(criteria.from(SimpleModel.class))).getResultList();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SimpleModel createModel(SimpleModel newSimpleModel) {
        try {
            log.info(newSimpleModel.toString());
            em.persist(newSimpleModel);
            em.flush();
            return newSimpleModel;
        } catch (Exception e) {
            log.severe(e.getMessage());
            return null;
        }
    }
    
}
