package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.PosException;
import com.floreantpos.model.InventoryItem;
import com.floreantpos.model.ItemCompVendPack;

public class ItemCompVendPackDAO extends BaseItemCompVendPackDAO {

	/**
	 * Default constructor. Can be used in place of getInstance()
	 */
	public ItemCompVendPackDAO() {
	}

	public List<ItemCompVendPack> findAllByInventoryItem(InventoryItem item) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(ItemCompVendPack.PROP_INV_ITEM, item));
			List<ItemCompVendPack> list = criteria.list();
			if (list != null && !list.isEmpty()) {
				return list;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new PosException("Error occured while finding inventory item data");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}