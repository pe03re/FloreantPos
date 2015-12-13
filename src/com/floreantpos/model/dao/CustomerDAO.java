package com.floreantpos.model.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.PosException;
import com.floreantpos.model.Customer;
import com.floreantpos.model.MenuItem;

public class CustomerDAO extends BaseCustomerDAO {

	/**
	 * Default constructor. Can be used in place of getInstance()
	 */
	public CustomerDAO() {
	}

	public List<Customer> findBy(String phone, String loyalty, String name) {
		Session session = null;
		

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			Disjunction disjunction = Restrictions.disjunction();
			
			if(StringUtils.isNotEmpty(phone))
				disjunction.add(Restrictions.like(Customer.PROP_TELEPHONE_NO, "%" + phone + "%"));
			
			if(StringUtils.isNotEmpty(loyalty))
				disjunction.add(Restrictions.like(Customer.PROP_LOYALTY_NO, "%" + loyalty + "%"));
			
			if(StringUtils.isNotEmpty(name))
				disjunction.add(Restrictions.like(Customer.PROP_NAME, "%" + name + "%"));
			
			criteria.add(disjunction);

			return criteria.list();
			
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}

	}
	
	public boolean hasCustomerByPhone(String phone) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Customer.PROP_TELEPHONE_NO, phone));
			return criteria.list().size() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new PosException("Error occured while finding customer");
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

}