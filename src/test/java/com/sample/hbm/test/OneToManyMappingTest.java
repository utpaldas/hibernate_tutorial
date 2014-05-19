package com.sample.hbm.test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import com.sample.hbm.util.HibernateUtil;
import com.sample.model.LineItem;
import com.sample.model.Order;
import com.sample.model.OrderStatusEnum;

/**
 * Implementation for testing one to many association. Refer to Order and
 * LineItems model and hbm file.
 * 
 * @author udas
 * 
 */
public class OneToManyMappingTest implements CRUD {

	private static SessionFactory sessionFactory = null;
	private static Logger logger = null;

	@Before
	public void setUp() {
		sessionFactory = HibernateUtil.getSessionFactory();
		logger = HibernateUtil.getLogger();
	}

	// Customer not populated. This is illustrated in Many-To-One Test case.
	@Test
	public void insertRecord() {
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();

			Order ord = new Order();
			ord.setOrderDate(new java.util.Date(System.currentTimeMillis()));
			ord.setOrderStatus(OrderStatusEnum.SUMBITTED);

			// Set LineItems
			Set<LineItem> lineItems = new HashSet<LineItem>();
			LineItem l1 = new LineItem();
			l1.setProductName("Router");
			l1.setQuantity(10);
			l1.setUnitPrice(12.99);
			lineItems.add(l1);

			LineItem l2 = new LineItem();
			l2.setProductName("Switch");
			l2.setQuantity(10);
			l2.setUnitPrice(14.99);
			lineItems.add(l2);

			ord.setLineItems(lineItems);

			session.save(ord);
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

	public Order getOrderById() {
		Session session = null;
		Order order = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			String queryStr = "from Order where orderId = :id";
			Query query = session.createQuery(queryStr);
			query.setParameter("id", new Long(1));
			List<Order> ord = query.list();
			order = ord.get(0);
			// toString() method implementation explicitly calls the lazily
			// initialized lineItems. If we put the toString() call outside
			// session then we would get the following error.
			// "failed to lazily initialize a collection of role: com.sample.model.Order.lineItems, no session or session was closed".
			System.out.println(order.toString());
			session.getTransaction().commit();
		} catch (Exception ex) {
			logger.error(this.getClass().getCanonicalName() + ": getOrderById"
					+ ex.getLocalizedMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return order;

	}

	@Test
	public void getRecord() {
		this.getOrderById();
	}

	@Test
	public void updateRecord() {
		insertRecord();
		Order ord = this.getOrderById();
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			ord.setOrderStatus(OrderStatusEnum.IN_PROGRESS);
			Set<LineItem> lineItems = ord.getLineItems();
			Iterator<LineItem> itr = lineItems.iterator();
			while (itr.hasNext()) {
				LineItem lineItem = itr.next();
				lineItem.setProductName(lineItem.getProductName() + "-Updated");
			}
			ord.setLineItems(lineItems);
			session.update(ord);
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

	@Test
	public void updateOrderEntityRecord() {
		Order ord = getOrderById();
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			ord.setOrderStatus(OrderStatusEnum.COMPLETED);
			Set<LineItem> lineItems = ord.getLineItems();
			Iterator<LineItem> itr = lineItems.iterator();
			while (itr.hasNext()) {
				LineItem lineItem = itr.next();
				lineItem.setProductName(lineItem.getProductName() + "Done 1");
			}
			ord.setLineItems(lineItems);
			session.update("CustomOrderEntity", ord);
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

	@Test
	public void deleteRecord() {
		Order ord = this.getOrderById();
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			session.delete(ord);
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

	public static void setLogger(Logger logger) {
		OneToManyMappingTest.logger = logger;
	}

	public static void setSessionFactory(SessionFactory sessionFactory) {
		OneToManyMappingTest.sessionFactory = sessionFactory;
	}
}
