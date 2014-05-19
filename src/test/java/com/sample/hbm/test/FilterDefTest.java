package com.sample.hbm.test;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import com.sample.hbm.util.HibernateUtil;
import com.sample.model.Prisoner;

public class FilterDefTest implements CRUD{

	private static SessionFactory sessionFactory = null;
	private static Logger logger = null;

	@Before
	public void setUp() {
		sessionFactory = HibernateUtil.getSessionFactory();
		logger = HibernateUtil.getLogger();
	}

	
	public void insertRecord() {
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			Prisoner prisoner = new Prisoner();
			prisoner.setFirstName("John");
			prisoner.setLastName("Doe");
			prisoner.setCellNumber(1);
			session.save(prisoner);
			session.getTransaction().commit();
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			prisoner = new Prisoner();
			prisoner.setCellNumber(10);
			prisoner.setFirstName("Perter");
			prisoner.setLastName("Parker");
			session.save(prisoner);
			session.getTransaction().commit();
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			prisoner = new Prisoner();
			prisoner.setCellNumber(5);
			prisoner.setFirstName("Bruce");
			prisoner.setLastName("Wayne");
			session.save(prisoner);
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
	public void getRecord() {
		insertRecord();
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			session.enableFilter("filter1").setParameter("cnumber", new Long(1));
			session.enableFilter("filter2").setParameter("pnumber", new Long(10));
			List<Prisoner> prisoner = session.createCriteria(Prisoner.class).list();
			System.out.println("Done");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}	
			
		
	}

	public void updateRecord() {
		// TODO Auto-generated method stub
		
	}

	public void deleteRecord() {
		// TODO Auto-generated method stub
		
	}

}
