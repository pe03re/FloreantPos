package com.floreantpos.model.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.PosException;
import com.floreantpos.model.InventoryTransaction;

public class InventoryTransactionDAO extends BaseInventoryTransactionDAO {

	/**
	 * Default constructor. Can be used in place of getInstance()
	 */
	public InventoryTransactionDAO() {
	}

	public List<InventoryTransaction> findByCurrentMonth() {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, -15);
			criteria.add(Restrictions.ge(InventoryTransaction.PROP_TRANSACTION_DATE, c.getTime()));
			List<InventoryTransaction> list = criteria.list();
			if (list != null && !list.isEmpty()) {
				return list;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new PosException("Error occured while finding inventory transaction");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}