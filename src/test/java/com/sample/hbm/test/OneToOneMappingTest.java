package com.sample.hbm.test;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import com.sample.hbm.util.HibernateUtil;
import com.sample.model.Address;
import com.sample.model.Store;

/**
 * Implementation for testing One-to-One Association.
 * Refer to Store and Address model and hbm file
 * 
 * @author udas
 * 
 */
public class OneToOneMappingTest implements CRUD {

	private static SessionFactory sessionFactory = null;
	private static Logger logger = null;

	@Before
	public void setUp() {
		sessionFactory = HibernateUtil.getSessionFactory();
		logger = HibernateUtil.getLogger();

	}

	/**
	 * Test the cascade save option
	 */
	@Test
	public void insertRecord() {
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();

			Address addr = new Address();
			addr.setStreetName("John R Rd");
			addr.setCity("Troy");
			addr.setCountryCode("USA");
			addr.setZipCode("48083");

			Store store = new Store();
			store.setName("Costco");
			store.setAddress(addr);

			session.save(store);
			session.getTransaction().commit();
		} catch (Exception ex) {
			logger.error(this.getClass().getCanonicalName() + ": insertRecord"
					+ ex.getLocalizedMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

	/**
	 * Test the one to one join association query.
	 */
	@Test
	public void queryRecordUsingHQL() {
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();

			String queryString = "from Store s where s.id = :storeId";
			Query query = session.createQuery(queryString);
			query.setParameter("storeId", new Long("1"));
			List<Store> stores = query.list();
			if (stores != null) {
				Store store = stores.get(0);
				Address addr = store.getAddress();
				System.out.println("Store Name: " + store.getName()
						+ "Store Address: " + addr.toString());
			}
			session.getTransaction().commit();
		} catch (Exception ex) {
			logger.error(this.getClass().getCanonicalName()
					+ ": queryRecordUsingHQL" + ex.getLocalizedMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**
	 * Tests the cascade update option.
	 */
	@Test
	public void updateRecord() {
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();

			String queryString = "from Store s where s.id = :storeId";
			Query query = session.createQuery(queryString);
			query.setParameter("storeId", new Long("1"));
			List<Store> stores = query.list();
			if (stores != null) {
				Store store = stores.get(0);
				Address addr = store.getAddress();
				System.out.println("Store Name: " + store.getName()
						+ "Store Address: " + addr.toString());
				addr.setCity("Detroit");
				session.save(store);
			}
			session.getTransaction().commit();
		} catch (Exception ex) {
			logger.error(this.getClass().getCanonicalName() + ": updateRecord"
					+ ex.getLocalizedMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**
	 * Tests the cascade delete option. cascade="delete"
	 */
	@Test
	public void deleteRecord() {
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();

			String queryString = "from Store s where s.id = :storeId";
			Query query = session.createQuery(queryString);
			query.setParameter("storeId", new Long("1"));
			List<Store> stores = query.list();
			if (stores != null) {
				Store store = stores.get(0);
				Address addr = store.getAddress();
				System.out.println("Store Name: " + store.getName()
						+ "Store Address: " + addr.toString());
				session.delete(store);
			}
			session.getTransaction().commit();
		} catch (Exception ex) {
			logger.error(this.getClass().getCanonicalName() + ": deleteRecord"
					+ ex.getLocalizedMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}
}
