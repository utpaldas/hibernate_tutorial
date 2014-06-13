package com.sample.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;

import com.sample.hbm.util.HibernateUtil;

public class PersistenceServiceImpl {

	private static PersistenceServiceImpl persistenceServiceImpl = null;
	private static SessionFactory sessionFactory = null;
	private static Logger logger;

	private PersistenceServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	public synchronized static PersistenceServiceImpl getInstance() {
		if (persistenceServiceImpl == null) {
			persistenceServiceImpl = new PersistenceServiceImpl();
			init();
		}
		return persistenceServiceImpl;
	}

	private static void init() {
		sessionFactory = HibernateUtil.getSessionFactory();
		logger = HibernateUtil.getLogger();
	}

	public void createInstance(Object obj) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			session.save(obj);
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error("Exception saving object of type: "
					+ obj.getClass().getCanonicalName());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	public Object getInstance(Class<?> entityName, long id) throws Exception {
		Object output = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			output = session.get(entityName, id);
			session.getTransaction().commit();
		} catch (Exception ex) {
			logger.error("Exception retrieving object of type: "
					+ entityName.getCanonicalName() + ", with id: " + id);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return output;
	}

}
