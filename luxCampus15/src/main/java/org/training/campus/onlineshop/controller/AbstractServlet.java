package org.training.campus.onlineshop.controller;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.training.campus.onlineshop.dao.ProductDao;

public abstract class AbstractServlet extends HttpServlet {

	private static final String DATASOURCE = "DATASOURCE";
	private static final String PRODUCT_DAO = "PRODUCT_DAO";
	private static final String JNDI_DATA = "jdbc/onlineshop";
	protected static final String PRODUCTS_ATTRIBUTE = "products";
	protected static final String ITEM_PARAMETER = "item";

	@Override
	public void init() throws ServletException {
		super.init();
		initDataSource();
		initProductDao();
	}

	private void initDataSource() throws ServletException {
		if (getServletContext().getAttribute(DATASOURCE) == null) {
			getServletContext().setAttribute(DATASOURCE, obtainDataSource());
		}
	}

	private DataSource obtainDataSource() throws ServletException {
		try {
			Context envContext = (Context) new InitialContext().lookup("java:/comp/env");
			return (DataSource) envContext.lookup(JNDI_DATA);
		} catch (NamingException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}

	private void initProductDao() {
		if (getServletContext().getAttribute(PRODUCT_DAO) == null) {
			getServletContext().setAttribute(PRODUCT_DAO, new ProductDao(getDataSource()));
		}
	}

	protected DataSource getDataSource() {
		return (DataSource) getServletContext().getAttribute(DATASOURCE);
	}

	protected ProductDao getProductDao() {
		return (ProductDao) getServletContext().getAttribute(PRODUCT_DAO);
	}

	protected void fetchProducts(HttpSession session) {
		session.setAttribute(PRODUCTS_ATTRIBUTE, getProductDao().getAll());
	}
	
}
