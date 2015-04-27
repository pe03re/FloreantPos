package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.PosException;
import com.floreantpos.model.Company;
import com.floreantpos.model.CompanyPerson;

public class CompanyPersonDAO extends BaseCompanyPersonDAO {

	/**
	 * Default constructor. Can be used in place of getInstance()
	 */
	public CompanyPersonDAO() {
	}

	public List<CompanyPerson> findAllByCompany(Company comp) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(CompanyPerson.PROP_COMPANY, comp));
			List<CompanyPerson> list = criteria.list();
			if (list != null && !list.isEmpty()) {
				return list;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new PosException("Error occured while finding company data");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}