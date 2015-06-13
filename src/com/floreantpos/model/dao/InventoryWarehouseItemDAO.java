package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.PosException;
import com.floreantpos.model.InventoryItem;
import com.floreantpos.model.InventoryLocation;
import com.floreantpos.model.InventoryWarehouseItem;
import com.floreantpos.model.MenuItem;

public class InventoryWarehouseItemDAO extends BaseInventoryWarehouseItemDAO {

	/**
	 * Default constructor. Can be used in place of getInstance()
	 */
	public InventoryWarehouseItemDAO() {
	}

	public boolean hasInventoryItemByName(String name) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuItem.PROP_NAME, name));
			return criteria.list().size() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new PosException("Error occured while finding food items");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public List<InventoryWarehouseItem> findByInventoryItem(InventoryItem item) {
		Session session = null;
		try {
			session = getSession();
			return findByInventoryItem(session, item);
		} catch (Exception e) {
			e.printStackTrace();
			throw new PosException("Error occured while finding food items");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public List<InventoryWarehouseItem> findByInventoryItem(Session session, InventoryItem item) {
		try {
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(InventoryWarehouseItem.PROP_INVENTORY_ITEM, item));
			criteria.addOrder(Order.asc(InventoryWarehouseItem.PROP_ITEM_LOCATION));
			return criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new PosException("Error occured while finding food items");
		}
	}

	public InventoryWarehouseItem findByInventoryItemAndInventoryLocation(InventoryItem item, InventoryLocation loc) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(InventoryWarehouseItem.PROP_INVENTORY_ITEM, item));
			criteria.add(Restrictions.eq(InventoryWarehouseItem.PROP_ITEM_LOCATION, loc));
			List<InventoryWarehouseItem> list = criteria.list();
			if (list != null && !list.isEmpty()) {
				return list.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new PosException("Error occured while finding food items");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}