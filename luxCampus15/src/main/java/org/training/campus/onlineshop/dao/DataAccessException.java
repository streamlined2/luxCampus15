package org.training.campus.onlineshop.dao;

public class DataAccessException extends RuntimeException {

	public DataAccessException(String msg) {
		super(msg);
	}

	public DataAccessException(Exception e) {
		super(e);
	}

}
