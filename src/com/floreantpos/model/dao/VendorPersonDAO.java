package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.PosException;
import com.floreantpos.model.InventoryVendor;
import com.floreantpos.model.VendorPerson;

public class VendorPersonDAO extends BaseVendorPersonDAO {

	/**
	 * Default constructor. Can be used in place of getInstance()
	 */
	public VendorPersonDAO() {
	}

	public List<VendorPerson> findAllByVendor(InventoryVendor vend) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(VendorPerson.PROP_VENDOR, vend));
			List<VendorPerson> list = criteria.list();
			if (list != null && !list.isEmpty()) {
				return list;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new PosException("Error occured while finding vendor data");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}