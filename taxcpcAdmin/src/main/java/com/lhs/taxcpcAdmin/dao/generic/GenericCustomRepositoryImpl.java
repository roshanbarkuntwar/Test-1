
package com.lhs.taxcpcAdmin.dao.generic;

import java.io.Serializable;

/**
 * @author ayushi.jain
 *
 */
public class GenericCustomRepositoryImpl<K extends Serializable, T> extends HibernateConfiguration  implements GenericCustomRepository<K, T> {

	
}
