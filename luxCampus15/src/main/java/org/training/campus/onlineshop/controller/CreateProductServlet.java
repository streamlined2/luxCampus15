package org.training.campus.onlineshop.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.training.campus.onlineshop.entity.Product;

@WebServlet(name = "CreateProductServlet", description = "creates product", loadOnStartup = 1, urlPatterns = "/products/add")
public class CreateProductServlet extends AbstractServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getSession().setAttribute(CREATE_PRODUCT_ATTRIBUTE, Boolean.TRUE);
		req.getSession().setAttribute(PRODUCT_ATTRIBUTE, new Product());
		getServletContext().getRequestDispatcher("/WEB-INF/pages/new-edit-product.jsp").forward(req, resp);
	}

}
