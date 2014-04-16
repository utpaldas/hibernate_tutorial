package com.sample.hbm.test;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import com.sample.hbm.util.HibernateUtil;
import com.sample.model.Address;
import com.sample.model.AddressTypeEnum;
import com.sample.model.Customer;
import com.sample.model.Employee;
import com.sample.model.EmployeeTypeEnum;

public class EmployeeMappingTest implements CRUD {

	private static SessionFactory sessionFactory = null;
	private static Logger logger = null;

	@Before
	public void setUp() {
		sessionFactory = HibernateUtil.getSessionFactory();
		logger = HibernateUtil.getLogger();
	}

	@Test
	public void insertRecord() {
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			Employee employee = new Employee();
			employee.setFirstName("John");
			employee.setLastName("Doe");
			employee.setRole(EmployeeTypeEnum.INTERN);
			Address addr = new Address();
			addr.setCity("Troy");
			addr.setCountryCode("USA");
			addr.setStreetName("Town Center");
			addr.setZipCode("48083");
			addr.setType(AddressTypeEnum.WORK);
			employee.setAddress(addr);
			session.save("BaseEmployee", employee);
			session.getTransaction().commit();

			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			employee = new Employee();
			employee.setFirstName("Peter");
			employee.setLastName("Doe");
			employee.setRole(EmployeeTypeEnum.INTERN);
			addr = (Address) session.get(Address.class, new Long(1));
			employee.setAddress(addr);
			session.save("BaseEmployee", employee);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

	/**
	 * Get All employees with same work address.
	 */
	@Test
	public void getRecord() {
		insertCustomerRecord();
		insertRecord();		
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			Employee employee = (Employee)session.get("BaseEmployee", new Long(3));
			EmployeeTypeEnum type = employee.getRole();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@Test
	public void updateRecord() {
		insertRecord();
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			Employee customEmp = (Employee) session.get("CustomEmployeeEntity",
					new Long(1));
			customEmp.setDept("CSMTG");
			session.save("CustomEmployeeEntity", customEmp);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@Test
	public void deleteRecord() {
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			Employee employee = (Employee) session.get(Employee.class,
					new Long(1));
			session.delete(employee);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}
	
	private void insertCustomerRecord() {
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			Customer customer = new Customer();
			customer.setCustomerId(System.currentTimeMillis());
			customer.setFirstName("John");
			customer.setLastName("doe");

			Address addr = new Address();
			addr.setStreetName("1234 Heavenly Rd");
			addr.setCity("Troy");
			addr.setCountryCode("USA");
			addr.setZipCode("48083");
			customer.setAddress(addr);

			Customer customer1 = new Customer();
			customer1.setCustomerId(System.currentTimeMillis());
			customer1.setFirstName("Peter");
			customer1.setLastName("Butler");

			Address addr1 = new Address();
			addr1.setStreetName("1234 Heavenly Rd");
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

}
