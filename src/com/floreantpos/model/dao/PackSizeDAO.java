package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.PackSize;

public class PackSizeDAO extends BasePackSizeDAO {

	/**
	 * Default constructor. Can be used in place of getInstance()
	 */
	public PackSizeDAO() {
	}

	public PackSize findPackSize(int size) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(PackSize.PROP_SIZE, size));
			List<PackSize> list = criteria.list();
			if (list != null && !list.isEmpty()) {
				return list.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}