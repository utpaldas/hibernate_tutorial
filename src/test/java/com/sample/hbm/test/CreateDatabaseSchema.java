package com.sample.hbm.test;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.slf4j.Logger;

import com.sample.hbm.util.HibernateUtil;

public class CreateDatabaseSchema {

	private static SessionFactory sessionFactory = null;
	private static Logger logger = null;

	@Test
	public void testCreateDBSchema() {

		sessionFactory = HibernateUtil.createSessionFactory();
		logger = HibernateUtil.getLogger();
		logger.info("DB Creation Successful");

	}

}
