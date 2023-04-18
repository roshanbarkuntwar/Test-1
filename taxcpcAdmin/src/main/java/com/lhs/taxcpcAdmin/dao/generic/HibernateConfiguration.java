/**
 * 
 */
package com.lhs.taxcpcAdmin.dao.generic;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;

/**
 * @author ayushi.jain
 *
 */
public class HibernateConfiguration {

	@PersistenceContext
	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return this.entityManager;
	}// End Method

	public Session getSession() {
		return entityManager.unwrap(Session.class);
	}// End Method

}// End Class
