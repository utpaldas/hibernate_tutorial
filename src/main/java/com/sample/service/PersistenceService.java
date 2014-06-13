package com.sample.service;

public interface PersistenceService {

	public void createInstance(Object obj) throws Exception;

	public Object getInstance(Class<?> entityName, long id) throws Exception;

}
