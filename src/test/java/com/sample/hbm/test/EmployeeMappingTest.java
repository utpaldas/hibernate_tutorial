package com.sample.hbm.test;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import com.sample.hbm.util.HibernateUtil;
import com.sample.model.Address;
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
			employee.setFirstName("Utpal");
			employee.setLastName("Das");
			employee.setRole(EmployeeTypeEnum.INTERN);
			Address addr = new Address();
			addr.setCity("Troy");
			addr.setCountryCode("USA");
			addr.setStreetName("Town Center");
			addr.setZipCode("48083");
			employee.setAddress(addr);
			session.save(employee);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

	public void getRecord() {
		// TODO Auto-generated method stub

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
			session.update("CustomEmployeeEntity", customEmp);
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
			Employee employee = (Employee) session.get(Employee.class, new Long(1));
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

}
