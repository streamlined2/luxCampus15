package org.training.campus.onlineshop.controller;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.training.campus.onlineshop.dao.JdbcProductDao;
import org.training.campus.onlineshop.entity.Product;

public abstract class AbstractServlet extends HttpServlet {

	private static final Locale DEFAULT_LOCALE = Locale.forLanguageTag("uk");
	private static final String PRODUCT_DAO = "productDao";
	private static final String DATA_JNDI_REF = "jdbc/onlineshop";
	protected static final String LOCALE_ATTRIBUTE = "locale";
	protected static final String PRODUCTS_ATTRIBUTE = "products";
	protected static final String CREATE_PRODUCT_ATTRIBUTE = "createProduct";
	protected static final String PRODUCT_ATTRIBUTE = "product";
	protected static final String ITEM_PARAMETER = "item";
	protected static final String PRODUCT_NAME_PARAMETER = "name";
	protected static final String PRODUCT_PRICE_PARAMETER = "price";
	protected static final String PRODUCT_CREATION_DATE_PARAMETER = "creationDate";

	@Override
	public void init() throws ServletException {
		super.init();
		initLocale();
		initProductDao();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	protected Locale getLocale() {
		return (Locale) getServletContext().getAttribute(LOCALE_ATTRIBUTE);
	}

	private DataSource obtainDataSource() throws ServletException {
		try {
			Context envContext = (Context) new InitialContext().lookup("java:/comp/env");
			return (DataSource) envContext.lookup(DATA_JNDI_REF);
		} catch (NamingException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}

	private void initLocale() {
		if (getServletContext().getAttribute(LOCALE_ATTRIBUTE) == null) {
			Locale.setDefault(DEFAULT_LOCALE);
			getServletContext().setAttribute(LOCALE_ATTRIBUTE, DEFAULT_LOCALE);
		}
	}

	private void initProductDao() throws ServletException {
		if (getServletContext().getAttribute(PRODUCT_DAO) == null) {
			getServletContext().setAttribute(PRODUCT_DAO, new JdbcProductDao(obtainDataSource()));
		}
	}

	protected JdbcProductDao getProductDao() {
		return (JdbcProductDao) getServletContext().getAttribute(PRODUCT_DAO);
	}

	protected void fetchProducts(HttpServletRequest req) {
		req.getSession().setAttribute(PRODUCTS_ATTRIBUTE, getProductDao().getAll());
	}

	protected Product getProductFromList(HttpServletRequest req) throws ServletException {
		String itemParameter = req.getParameter(ITEM_PARAMETER);
		if (itemParameter == null)
			throw new ServletException("missing item parameter of product to operate on");

		List<Product> products = (List<Product>) req.getSession().getAttribute(PRODUCTS_ATTRIBUTE);
		if (products == null)
			throw new ServletException("missing product list attribute");

		int index = Integer.parseInt(itemParameter);
		if (index < 0 || index > products.size())
			throw new ServletException(
					String.format("wrong index %d, should be within [0,%d]", index, products.size() - 1));

		return products.get(index);
	}
	
	protected String defineParameter(HttpServletRequest req, String paramName, String exceptionMessage) throws ServletException {
		String parameter = req.getParameter(paramName);
		if (parameter == null) {
			throw new ServletException(exceptionMessage);
		}
		return parameter;
	}

}
