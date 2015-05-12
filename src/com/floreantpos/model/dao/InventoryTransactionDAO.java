package com.floreantpos.model.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
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
			c.add(Calendar.DATE, -60);
			criteria.add(Restrictions.ge(InventoryTransaction.PROP_TRANSACTION_DATE, c.getTime()));
			criteria.addOrder(Order.desc(InventoryTransaction.PROP_TRANSACTION_DATE));
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

	public List<InventoryTransaction> findTransactions(Date from, Date to) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(InventoryTransaction.class);
			criteria.add(Restrictions.ge(InventoryTransaction.PROP_TRANSACTION_DATE, from));
			criteria.add(Restrictions.le(InventoryTransaction.PROP_TRANSACTION_DATE, to));
			criteria.addOrder(Order.asc(InventoryTransaction.PROP_TRANSACTION_DATE));
			ArrayList<InventoryTransaction> list = (ArrayList<InventoryTransaction>) criteria.list();
			if (list != null && !list.isEmpty()) {
				return list;
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new PosException("Error occured while finding inventory transactions", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}