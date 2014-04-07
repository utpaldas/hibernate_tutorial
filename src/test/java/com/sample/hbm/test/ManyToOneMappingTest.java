package com.sample.hbm.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import com.sample.hbm.util.HibernateUtil;
import com.sample.model.Address;
import com.sample.model.Customer;
import com.sample.model.LineItem;
import com.sample.model.Order;
import com.sample.model.OrderStatusEnum;

/**
 * Implementation of Many-To-One Association between Order -> Customer.
 * 
 * @author udas
 * 
 */
public class ManyToOneMappingTest implements CRUD {

	private static SessionFactory sessionFactory = null;
	private static Logger logger = null;

	@Before
	public void setUp() {
		sessionFactory = HibernateUtil.getSessionFactory();
		logger = HibernateUtil.getLogger();
	}

	@Test
	public void insertRecord() {
		insertCustomerRecord();
		insertOrderRecord1();
		insertOrderRecord2();
	}

	private void insertCustomerRecord() {
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			Customer customer = new Customer();
			customer.setCustomerId(System.currentTimeMillis());
			customer.setFirstName("Utpal");
			customer.setLastName("Das");

			Address addr = new Address();
			addr.setStreetName("2366 John R Rd");
			addr.setCity("Troy");
			addr.setCountryCode("USA");
			addr.setZipCode("48083");
			customer.setAddress(addr);

			Customer customer1 = new Customer();
			customer1.setCustomerId(System.currentTimeMillis());
			customer1.setFirstName("Preeti");
			customer1.setLastName("Borgohain");

			Address addr1 = new Address();
			addr1.setStreetName("2366 John R Rd Apt 103");
			addr1.setCity("Troy");
			addr1.setCountryCode("USA");
			addr1.setZipCode("48083");
			customer1.setAddress(addr1);

			session.save(customer);
			session.save(customer1);
			session.getTransaction().commit();

		} catch (Exception ex) {
			logger.error(this.getClass().getCanonicalName()
					+ ": insertCustomerRecord" + ex.getLocalizedMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

	private void insertOrderRecord1() {
		Session session = null;
		// If we invoke this method inside try catch block,
		// it would close the current session and save would fail.
		Customer customer = this.getCustomerById();
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();

			Order ord = new Order();
			ord.setOrderDate(new java.util.Date(System.currentTimeMillis()));

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

			ord.setCustomer(customer);

			session.save("Order",ord);
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

	private void insertOrderRecord2() {
		Session session = null;
		// If we invoke this method inside try catch block,
		// it would close the current session and save would fail.
		Customer customer = this.getCustomerById();
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();

			Order ord = new Order();
			ord.setOrderDate(new java.util.Date(System.currentTimeMillis()));

			// Set LineItems
			Set<LineItem> lineItems = new HashSet<LineItem>();
			LineItem l1 = new LineItem();
			l1.setProductName("Laptop");
			l1.setQuantity(10);
			l1.setUnitPrice(12.99);
			lineItems.add(l1);

			LineItem l2 = new LineItem();
			l2.setProductName("Laptop Bag");
			l2.setQuantity(10);
			l2.setUnitPrice(14.99);
			lineItems.add(l2);

			ord.setLineItems(lineItems);

			ord.setCustomer(customer);

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

	public Customer getCustomerById() {
		Session session = null;
		Customer cust = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			String queryStr = "from Customer where id = :id";
			Query query = session.createQuery(queryStr);
			query.setParameter("id", new Long(1));
			List<Customer> customer = query.list();
			cust = customer.get(0);
			logger.info(cust.toString());
			session.getTransaction().commit();
		} catch (Exception ex) {
			logger.error(this.getClass().getCanonicalName()
					+ ": getCustomerById: " + ex.getLocalizedMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return cust;

	}

	@Test
	public void getRecord() {
		this.getCustomerById();
	}

	@Test
	public void updateRecord() {
		insertCustomerRecord();
		insertOrderRecord1();
		insertOrderRecord2();

		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			Order ord = (Order)session.get("CustomOrderEntity", new Long(1));
			ord.setOrderStatus(OrderStatusEnum.IN_PROGRESS);
			session.update("CustomOrderEntity", ord);
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

	@Test
	public void deleteRecord() {
		// Since cascade=none. deleting order record would not
		// delete the customer record in DB.
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			List<Long> ids = new ArrayList();
			ids.add(new Long("1"));
			ids.add(new Long("2"));
			Criteria criteria = session.createCriteria(Order.class);
			criteria.add(Restrictions.in("customer.id", ids));
			List<Order> orders = criteria.list();
			if (!orders.isEmpty()) {
				Order order = orders.get(0);
				session.delete(order);
				order = orders.get(1);
				session.delete(order);

			}
			criteria = session.createCriteria(Customer.class);
			criteria.add(Restrictions.in("id", ids));
			List<Customer> customers = criteria.list();
			if (!customers.isEmpty()) {
				Customer customer = customers.get(0);
				session.delete(customer);
				customer = customers.get(1);
				session.delete(customer);

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

	public static void setLogger(Logger logger) {
		ManyToOneMappingTest.logger = logger;
	}

	public static void setSessionFactory(SessionFactory sessionFactory) {
		ManyToOneMappingTest.sessionFactory = sessionFactory;
	}
}
