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
import com.floreantpos.model.ExpenseTransaction;

public class ExpenseTransactionDAO extends BaseExpenseTransactionDAO {

	/**
	 * Default constructor. Can be used in place of getInstance()
	 */
	public ExpenseTransactionDAO() {
	}

	public List<ExpenseTransaction> findByCurrentMonth() {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, -2);
			criteria.add(Restrictions.ge(ExpenseTransaction.PROP_TRANSACTION_DATE, c.getTime()));
			criteria.addOrder(Order.desc(ExpenseTransaction.PROP_TRANSACTION_DATE));
			List<ExpenseTransaction> list = criteria.list();
			if (list != null && !list.isEmpty()) {
				return list;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new PosException("Error occured while finding expense transaction");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public List<ExpenseTransaction> findTransactions(Date from, Date to) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(ExpenseTransaction.class);
			criteria.add(Restrictions.ge(ExpenseTransaction.PROP_TRANSACTION_DATE, from));
			criteria.add(Restrictions.le(ExpenseTransaction.PROP_TRANSACTION_DATE, to));
			criteria.addOrder(Order.asc(ExpenseTransaction.PROP_TRANSACTION_DATE));
			ArrayList<ExpenseTransaction> list = (ArrayList<ExpenseTransaction>) criteria.list();
			if (list != null && !list.isEmpty()) {
				return list;
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new PosException("Error occured while finding expense transactions", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}