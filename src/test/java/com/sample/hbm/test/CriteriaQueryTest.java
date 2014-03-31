package com.sample.hbm.test;

import java.util.List;

import org.hibernate.Criteria;
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

}
