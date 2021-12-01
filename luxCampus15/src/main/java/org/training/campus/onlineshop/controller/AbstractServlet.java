package org.training.campus.onlineshop.controller;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;

public abstract class AbstractServlet extends HttpServlet {

	private DataSource dataSource;
	
	protected Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	@Override
	public void init() throws ServletException {
		super.init();
		try {
			Context envContext = (Context) new InitialContext().lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/online-shop");
		} catch (NamingException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
	}

}
