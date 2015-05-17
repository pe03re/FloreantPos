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
import com.floreantpos.model.Company;
import com.floreantpos.model.InventoryItem;
import com.floreantpos.model.InventoryTransaction;
import com.floreantpos.model.InventoryTransactionType;
import com.floreantpos.model.InventoryVendor;
import com.floreantpos.model.PackSize;

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
			c.add(Calendar.DATE, -2);
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

	public List<InventoryTransaction> findTransactions(Date from, Date to, List<InventoryItem> item, List<Company> comp, List<InventoryVendor> dist, List<InventoryTransactionType> type) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(InventoryTransaction.class);
			criteria.add(Restrictions.ge(InventoryTransaction.PROP_TRANSACTION_DATE, from));
			criteria.add(Restrictions.le(InventoryTransaction.PROP_TRANSACTION_DATE, to));
			if (item != null && !item.isEmpty()) {
				criteria.add(Restrictions.in(InventoryTransaction.PROP_INVENTORY_ITEM, item));
			}
			if (comp != null && !comp.isEmpty()) {
				criteria.add(Restrictions.in(InventoryTransaction.PROP_COMPANY, comp));
			}
			if (dist != null && !dist.isEmpty()) {
				criteria.add(Restrictions.in(InventoryTransaction.PROP_DISTRIBUTOR, dist));
			}
			if (type != null && !type.isEmpty()) {
				criteria.add(Restrictions.in(InventoryTransaction.PROP_TRANSACTION_TYPE, type));
			}
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

	public InventoryTransaction findByICVP(InventoryItem inventoryItem, Company company, InventoryVendor inventoryVendor, PackSize packSize, Integer id) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(InventoryTransaction.PROP_INVENTORY_ITEM, inventoryItem));
			criteria.add(Restrictions.eq(InventoryTransaction.PROP_COMPANY, company));
			criteria.add(Restrictions.eq(InventoryTransaction.PROP_DISTRIBUTOR, inventoryVendor));
			criteria.add(Restrictions.eq(InventoryTransaction.PROP_PACK_SIZE, packSize));
			criteria.add(Restrictions.lt(InventoryTransaction.PROP_ID, id));
			criteria.addOrder(Order.desc(InventoryTransaction.PROP_ID));
			List<InventoryTransaction> list = criteria.list();
			if (list != null && !list.isEmpty()) {
				return list.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new PosException("Error occured while finding inventory transaction data");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}