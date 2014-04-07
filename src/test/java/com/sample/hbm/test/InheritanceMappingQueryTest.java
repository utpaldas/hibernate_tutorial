package com.sample.hbm.test;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;


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
		
	}

	@Test
	public void testGetQueryForTablePerConcreteClass() {
	}

	@Test
	public void testInsertQueryForTablePerConcreteClass() {

	}

	@Test
	public void testUpdateQueryForTablePerConcreteClass() {

	}

	@Test
	public void testGetQueryForTablePerSubClass() {
	}

	@Test
	public void testInsertQueryForTablePerSubClass() {

	}

	@Test
	public void testUpdateQueryForTablePerSubClass() {

	}

	@Test
	public void testGetQueryForTablePerSubClassWithDiscriminator() {
	}

	@Test
	public void testInsertQueryForTablePerSubClassDiscriminator() {

	}

	@Test
	public void testUpdateQueryForTablePerSubClassDiscriminator() {

	}

}
