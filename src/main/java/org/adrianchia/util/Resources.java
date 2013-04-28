/**
 * 
 */
package org.adrianchia.util;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Adrian Chia
 *
 */
public class Resources {

    @Produces
    @PersistenceContext
    private EntityManager em;
    
    @Produces
    private Logger getLogger(InjectionPoint ip) {
        return Logger.getLogger(ip.getMember().getDeclaringClass().getName());
    }
}
