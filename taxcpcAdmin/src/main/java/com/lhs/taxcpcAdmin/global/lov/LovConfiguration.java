/**
 * 
 */
package com.lhs.taxcpcAdmin.global.lov;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

import com.lhs.taxcpcAdmin.service.lov.ReqTranLov;


/**
 * @author sakshi.bandhate
 *
 */
public class LovConfiguration {

	/**
	 * This method returns all the classes which have lov object configured
	 * 
	 * @return
	 */
	public Set<Class<?>> getLovConfiguredClasses() {
		Set<Class<?>> set = new LinkedHashSet<>();

		set.add(LovConfiguration.class);
		set.add(ReqTranLov.class);
		

		return set;
	}// End Method
	
	/**
	 * This method iterates all the classes which have lov object configured and returns the lov object
	 * 
	 * @return
	 */
	public LovDataObject getLovDataObjectById(String id) {
		LovDataObject object = null;
		try {
			Set<Class<?>> list = getLovConfiguredClasses();
			if (list != null && list.size() > 0) {
				for (Class<?> clsObj : list) {
					try {
						Method method = clsObj.getMethod(id, null);
						object = (LovDataObject) method.invoke(clsObj.newInstance(), null);
						if (object != null) {
							break;
						}
					} catch (Exception e) {
						System.out.println("LovConfiguration Exception..." + e.getClass());
					}
				}
			}
		} catch (Exception e) {
			System.out.println("LovConfiguration Exception..." + e.getClass());
		}
		return object;
	}// End Method

}// End Class
