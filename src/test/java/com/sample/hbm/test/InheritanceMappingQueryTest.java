package com.sample.hbm.test;

import static org.junit.Assert.*;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import com.sample.hbm.util.HibernateUtil;

/**
 * The test cases are to evaluate and understand how the actual sql queries are
 * formed by hibernate based on the table inheritance mapping.
 * 
 * @author udas
 * 
 */
public class InheritanceMappingQueryTest {

	private static SessionFactory sessionFactory = null;
	private static Logger logger = null;

	@Before
	public void setUp() {
		sessionFactory = HibernateUtil.getSessionFactory();
		logger = HibernateUtil.getLogger();
	}

	@Test
	public void testGetQueryForTablePerConcreteClass() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertQueryForTablePerConcreteClass() {

	}

	@Test
	public void testUpdateQueryForTablePerConcreteClass() {

	}

	@Test
	public void testGetQueryForTablePerSubClass() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertQueryForTablePerSubClass() {

	}

	@Test
	public void testUpdateQueryForTablePerSubClass() {

	}

	@Test
	public void testGetQueryForTablePerSubClassWithDiscriminator() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertQueryForTablePerSubClassDiscriminator() {

	}

	@Test
	public void testUpdateQueryForTablePerSubClassDiscriminator() {

	}

}
