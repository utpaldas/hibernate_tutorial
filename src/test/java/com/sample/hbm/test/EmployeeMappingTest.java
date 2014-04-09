package com.sample.hbm.test;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import com.sample.hbm.util.HibernateUtil;
import com.sample.model.Address;
import com.sample.model.AddressTypeEnum;
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
		insertRecord();
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			Address addr = (Address) session.get(Address.class, new Long(1));
			Criteria criteria = session.createCriteria("BaseEmployee");
			criteria.add(Restrictions.eq("address", addr));
			//criteria.addOrder(Order.desc("role"));
			criteria.setProjection(Projections.rowCount());
			long rowCnt = (Long) criteria.uniqueResult();
			//List<Employee> empList = criteria.list();
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

}
