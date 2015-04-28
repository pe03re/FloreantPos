package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.PosException;
import com.floreantpos.model.InventoryVendor;

public class InventoryVendorDAO extends BaseInventoryVendorDAO {

	/**
	 * Default constructor. Can be used in place of getInstance()
	 */
	public InventoryVendorDAO() {
	}

	public List<InventoryVendor> findAllExpenseVendors(boolean expVendors) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(InventoryVendor.PROP_EXP_TYPE_VENDOR, expVendors));
			List<InventoryVendor> list = criteria.list();
			if (list != null && !list.isEmpty()) {
				return list;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new PosException("Error occured while finding Inventory Vendors");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}