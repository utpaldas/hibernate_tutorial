package com.sample.hbm.test;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import com.sample.hbm.util.HibernateUtil;
import com.sample.model.Address;
import com.sample.model.Product;
import com.sample.model.Store;

/**
 * Implementation for Many-To-Many Association between Product and Store. This
 * mapping was written with inverse=true on Product.hbm.xml, which means that
 * Product is the owner of the relationship. Hence we'd perform CRUD on product
 * and corresponding associations should get filled up in Store.
 * 
 * @author udas
 * 
 */
public class ManyToManyMappingTest implements CRUD {

	private static SessionFactory sessionFactory = null;
	private static Logger logger = null;

	@Before
	public void setUp() {
		sessionFactory = HibernateUtil.getSessionFactory();
		logger = HibernateUtil.getLogger();
	}

	@Test
	public void insertRecord() {
		insertNewStore();
		insertNewProduct();
	}

	private void insertNewStore() {
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();

			Store store1 = new Store();
			Address add = new Address();
			add.setCity("Royal Oak");
			add.setZipCode("48075");
			add.setCountryCode("USA");
			store1.setAddress(add);
			store1.setName("Costco");
			session.save(store1);

			Store store2 = new Store();
			Address add1 = new Address();
			add1.setCity("Royal Oak");
			add1.setZipCode("48075");
			add1.setCountryCode("USA");
			store2.setAddress(add1);
			store2.setName("Meiher");
			session.save(store2);

			session.getTransaction().commit();
		} catch (Exception ex) {
			logger.error(this.getClass().getCanonicalName()
					+ ": insertNewStore" + ex.getLocalizedMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	private void insertNewProduct() {
		Session session = null;
		Store st1 = (Store) this.getInstance(1, Store.class);
		Store st2 = (Store) this.getInstance(2, Store.class);
		
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			Product prd = new Product();
			prd.setProductName("Bose");
			
//			Store st1 = new Store();
//			Address add = new Address();
//			add.setCity("Royal Oak");
//			add.setZipCode("48075");
//			add.setCountryCode("USA");
//			st1.setAddress(add);
//			st1.setName("Costco");
//
//			Store st2 = new Store();
//			Address add1 = new Address();
//			add1.setCity("Royal Oak");
//			add1.setZipCode("48075");
//			add1.setCountryCode("USA");
//			st2.setAddress(add1);
//			st2.setName("Meiher");
			
			List<com.sample.model.Store> storeAvailability = new ArrayList<com.sample.model.Store>();
			storeAvailability.add(st1);
			storeAvailability.add(st2);
			
			prd.setStoreAvailability(storeAvailability);
			session.save(prd);
			session.getTransaction().commit();
		} catch (Exception ex) {
			logger.error(this.getClass().getCanonicalName()
					+ ": insertNewProduct" + ex.getLocalizedMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	private Object getInstance(long id, Class cls) {
		Session session = null;
		Object obj = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			String queryStr = "from " + cls.getName() + " where id = :id";
			Query query = session.createQuery(queryStr);
			query.setParameter("id", new Long(id));
			List result = query.list();
			if (result != null) {
				obj = result.get(0);
			}
			session.getTransaction().commit();
		} catch (Exception ex) {

		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return obj;
	}

	public void getRecord() {

	}

	public void updateRecord() {
		// TODO Auto-generated method stub

	}

	public void deleteRecord() {
		// TODO Auto-generated method stub

	}

}
