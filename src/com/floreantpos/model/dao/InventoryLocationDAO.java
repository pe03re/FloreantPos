package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.PosException;
import com.floreantpos.model.InventoryLocation;
import com.floreantpos.model.InventoryWarehouse;

public class InventoryLocationDAO extends BaseInventoryLocationDAO {

	/**
	 * Default constructor. Can be used in place of getInstance()
	 */
	public InventoryLocationDAO() {
	}

	public List<InventoryLocation> findByInventoryItem(InventoryWarehouse warehouse) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(InventoryLocation.PROP_WAREHOUSE, warehouse));
			return criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new PosException("Error occured while finding food items");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}