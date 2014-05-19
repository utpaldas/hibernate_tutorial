package com.sample.hbm.util;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Util class to create hibernate session factory and enable hibernate logging.
 * 
 * @author udas
 * 
 */
public class HibernateUtil {

	private static SessionFactory sessionFactory = null;

	private static org.slf4j.Logger logger = null;

	public static SessionFactory createSessionFactory() throws RuntimeException {
		try {
			Configuration config = new Configuration();
			config = config.configure();
			config.addResource("com/sample/model/Store.hbm.xml");
			config.addResource("com/sample/model/Address.hbm.xml");
			config.addResource("com/sample/model/Order.hbm.xml");
			config.addResource("com/sample/model/LineItem.hbm.xml");
			config.addResource("com/sample/model/Customer.hbm.xml");
			config.addResource("com/sample/model/Product.hbm.xml");
			config.addResource("com/sample/model/Person.hbm.xml");
			config.addResource("com/sample/model/Employee.hbm.xml");
			config.addResource("com/sample/model/BillingDetails.hbm.xml");
			config.addResource("com/sample/model/CustomOrderEntity.hbm.xml");
			config.addResource("com/sample/model/CustomLineItemEntity.hbm.xml");
			config.addResource("com/sample/model/CustomEmployeeEntity.hbm.xml");
			config.addResource("com/sample/model/Prisoner.hbm.xml");

			// config.addClass(com.sample.model.Store.class);
			// config.addClass(com.sample.model.Address.class);
			sessionFactory = config.buildSessionFactory();
		} catch (Exception ex) {
			getLogger().error(ex.getLocalizedMessage());
			throw new RuntimeException(ex);
		}
		return sessionFactory;
	}

	public static void initLogger() {
		if (logger == null) {
			try {
				InputStream is = HibernateUtil.class.getClassLoader()
						.getResourceAsStream("log4j.properties");
				Properties props = new Properties();
				props.load(is);
				PropertyConfigurator.configure(props);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		logger = org.slf4j.LoggerFactory.getLogger("org.hibernate");
	}

	public static synchronized SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			createSessionFactory();
		}
		return sessionFactory;
	}

	public static synchronized org.slf4j.Logger getLogger() {
		if (logger == null) {
			initLogger();
		}
		return logger;
	}

	public static void main(String args[]) {
		SessionFactory sf = getSessionFactory();
		if (sf == null)
			getLogger().error("Session Factory is null");
		else
			getLogger().info("SessionFactory Initialized");
	}
}
