
package com.lhs.taxcpcAdmin.dao.generic;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.hibernate.Session;

/**
 * @author ayushi.jain
 *
 */
public interface GenericCustomRepository<K extends Serializable, T>  {
	
    public Session getSession();
    
    public EntityManager getEntityManager();
    
}
