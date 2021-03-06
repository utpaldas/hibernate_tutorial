package com.sample.hbm.test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import com.sample.hbm.util.HibernateUtil;
import com.sample.model.Customer;
import com.sample.model.LineItem;
import com.sample.model.Order;

public class CriteriaQueryTest {

	private static SessionFactory sessionFactory = null;
	private static Logger logger = null;
	private OneToManyMappingTest order = new OneToManyMappingTest();
	private ManyToOneMappingTest customer = new ManyToOneMappingTest();

	@Before
	public void setUp() {
		sessionFactory = HibernateUtil.getSessionFactory();
		logger = HibernateUtil.getLogger();
		order.setSessionFactory(sessionFactory);
		order.setLogger(logger);
		customer.setSessionFactory(sessionFactory);
		customer.setLogger(logger);
	}

	@Test
	public void testDetachedQuery() {
		order.insertRecord();
		DetachedCriteria detachedCriteria = DetachedCriteria
				.forClass(LineItem.class);
		detachedCriteria.setProjection(Projections.property("id"));
		detachedCriteria.add(Restrictions.like("productName", "%switch%"));
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			List ids = detachedCriteria.getExecutableCriteria(session).list();
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
			order.deleteRecord();
		}

	}

	@Test
	public void testDetachedQueryWithSubQuery() {
		customer.insertRecord();
		DetachedCriteria detachedCriteria = DetachedCriteria
				.forClass(Customer.class);
		detachedCriteria.setProjection(Projections.property("id"));
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Order.class);
			criteria.add(Subqueries.propertyIn("customer.id", detachedCriteria));
			List<Order> orders = criteria.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
			customer.deleteRecord();
		}
	}

	/**
	 * Sample query where the column from a associated table is part of the
	 * Restriction.
	 */
	@Test
	public void testCriteriaQueryWithJoinColumn() {
		customer.insertRecord();
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Order.class);
			criteria.add(Restrictions.eq("customer.id", new Long(1)));
			List<Order> orders = criteria.list();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
			customer.deleteRecord();
		}

	}

	@Test
	public void testCriteriaQueryCountonEntity() {
		customer.insertRecord();
		Customer cust = customer.getCustomerById();
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Order.class);
			criteria.add(Restrictions.eq("customer", cust));
			criteria.setProjection(Projections.rowCount());
			long rowCnt = (Long) criteria.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
			customer.deleteRecord();
		}
	}

	@Test
	public void testCriteriaQueryCountUsingHQLonEntity() {
		customer.insertRecord();
		Customer cust = customer.getCustomerById();
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session
					.createQuery("select count(*) from Customer c where c.id=1");
			long rowCnt = query.list().size();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
			customer.deleteRecord();
		}
	}

	@Test
	public void testCriteriaQueryCountonEntityName() {
		customer.insertRecord();
		Customer cust = customer.getCustomerById();
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			Criteria criteria = session.createCriteria("CustomOrderEntity");
			criteria.add(Restrictions.ne("customer", null));
			criteria.setProjection(Projections.rowCount());
			long rowCnt = (Long) criteria.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
			customer.deleteRecord();
		}
	}

	@Test
	public void testGetEntityName() {
		customer.insertRecord();
		Customer cust = customer.getCustomerById();
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			Order ord = (Order) session.get("CustomOrderEntity", new Long(1));
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
			customer.deleteRecord();
		}
	}

	/*
	 * Sample query to get the list of orders with certain lineItems.
	 */
	@Test
	public void testListQuery() {
		order.insertRecord();
		Order ord = order.getOrderById();
		LineItem lt = ord.getLineItems().iterator().next();
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Order.class);
			criteria.createAlias("lineItems", "item");
			criteria.add(Restrictions.eq("item.id", lt.getId()));
			List<Order> ordList = criteria.list();
			System.out.println("Done");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
			order.deleteRecord();
		}
	}

}
