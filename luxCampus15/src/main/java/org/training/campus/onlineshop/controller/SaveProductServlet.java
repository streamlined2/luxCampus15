package org.training.campus.onlineshop.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.training.campus.onlineshop.entity.Product;

@WebServlet(name = "SaveProductServlet", description = "saves product", loadOnStartup = 1, urlPatterns = "/saveproduct")
public class SaveProductServlet extends AbstractServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		updateSaveProduct(req);
		resp.sendRedirect(req.getContextPath() + "/products");
	}

	private void updateSaveProduct(HttpServletRequest req) throws ServletException {
		Boolean createFlag = (Boolean) req.getSession().getAttribute(CREATE_PRODUCT_ATTRIBUTE);
		if (createFlag == null)
			throw new ServletException("no create product attribute found");

		Product product = (Product) req.getSession().getAttribute(PRODUCT_ATTRIBUTE);
		if (product == null)
			throw new ServletException("no product attribute found");

		updateProductWithParameters(req, product);

		if (createFlag) {
			getProductDao().persist(product);
		} else {
			getProductDao().merge(product);
		}

	}

	private void updateProductWithParameters(HttpServletRequest req, Product product) throws ServletException {
		String name = defineParameter(req, PRODUCT_NAME_PARAMETER, "missing product name parameter");
		String price = defineParameter(req, PRODUCT_PRICE_PARAMETER, "missing product price parameter");
		String creationDate = defineParameter(req, PRODUCT_CREATION_DATE_PARAMETER,
				"missing product creation date parameter");

		product.setName(name);
		product.setPrice(new BigDecimal(price));
		product.setCreationDate(LocalDate.parse(creationDate, DateTimeFormatter.ISO_LOCAL_DATE));
	}

}
