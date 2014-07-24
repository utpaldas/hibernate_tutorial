package com.sample.service.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sample.model.Customer;
import com.sample.service.PersistenceServiceImpl;

public class PersistenceServiceImplTest {
	private static PersistenceServiceImpl persistenceServiceImpl = null;

	@Before
	public void setUp() {
		persistenceServiceImpl = PersistenceServiceImpl.getInstance();
	}

	@Test
	public void testCreateInstance() {
		Customer cust = new Customer();
		cust.setFirstName("John");
		cust.setLastName("Doe");
		try {
			cust = (Customer) persistenceServiceImpl.createInstance(cust);
			System.out.println("Customer created with Id: " + cust.getId());
		} catch (Exception e) {
			Assert.fail("Creation failed");
		}
	}

	@Test
	public void testGetInstance() throws Exception {
		try {
			Object obj = persistenceServiceImpl.getInstance(Customer.class, 1);
			if (obj != null && obj instanceof Customer) {
				Customer cust = (Customer) obj;
				System.out.println(cust.getFirstName());
			}
		} catch (Exception e) {
			Assert.fail("Get failed");
		}
	}

}
